// Java 21+, sem preview. main sem argumentos nao usa rede.
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.io.IOException;

public class Java21Examples {
    sealed interface Result permits Success, Failure {}
    record Success(String body) implements Result {}
    record Failure(String reason) implements Result {}

    static String describe(Result result) {
        return switch (result) {
            case Success(String body) -> body;
            case Failure(String reason) -> "Failure: " + reason;
        };
    }

    // Exemplo de lote limitado. Em servidor, compartilhe limite/client no
    // escopo do downstream; um semaphore por request nao limita carga global.
    static List<String> fetch(List<URI> uris) throws Exception {
        if (uris.isEmpty()) return List.of();
        if (uris.size() > 32) throw new IllegalArgumentException("Batch limit is 32");
        var permits = new Semaphore(4);
        try (var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
             var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<String>> tasks = new ArrayList<>();
            for (URI uri : uris) {
                tasks.add(() -> {
                    if (!permits.tryAcquire(1, TimeUnit.SECONDS)) {
                        throw new TimeoutException("Downstream saturated");
                    }
                    try {
                        var request = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(3)).GET().build();
                        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        if (response.statusCode() < 200 || response.statusCode() >= 300) {
                            throw new IOException("HTTP " + response.statusCode());
                        }
                        return response.body();
                    } finally {
                        permits.release();
                    }
                });
            }
            var futures = executor.invokeAll(tasks, 5, TimeUnit.SECONDS);
            var result = new ArrayList<String>();
            for (var future : futures) {
                if (future.isCancelled()) throw new TimeoutException("Batch deadline exceeded");
                result.add(future.get()); // Falhas das tarefas sao observadas.
            }
            return List.copyOf(result);
        }
        // close espera termino: interrupcao e timeout de I/O precisam cooperar.
        // BodyHandlers.ofString materializa o corpo: use limite de bytes/
        // streaming quando payload remoto nao for limitado pelo contrato.
    }

    public static void main(String[] args) throws Exception {
        assert describe(new Success("ok")).equals("ok");
        assert describe(new Failure("missing")).equals("Failure: missing");
        var list = new ArrayList<>(List.of(1, 2, 3));
        assert list.getFirst() == 1;
        assert list.getLast() == 3;
        assert list.reversed().equals(List.of(3, 2, 1));
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            assert executor.submit(() -> Thread.currentThread().isVirtual()).get();
        }
        assert fetch(List.of()).isEmpty();
        if (args.length > 0) {
            var uris = java.util.Arrays.stream(args).map(URI::create).toList();
            System.out.println(fetch(uris));
        }
        System.out.println("Java21Examples OK");
    }
}
