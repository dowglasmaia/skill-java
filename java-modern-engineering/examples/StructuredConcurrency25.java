// SOMENTE Java 25 PREVIEW. Exemplo experimental, nao padrao de producao.
// javac --enable-preview --release 25 StructuredConcurrency25.java
// java --enable-preview -ea StructuredConcurrency25
import java.util.concurrent.StructuredTaskScope;

public class StructuredConcurrency25 {
    public static void main(String[] args) throws InterruptedException {
        try (var scope = StructuredTaskScope.open()) {
            var first = scope.fork(() -> "customer");
            var second = scope.fork(() -> "orders");
            scope.join();
            assert (first.get() + ":" + second.get()).equals("customer:orders");
        }
        System.out.println("StructuredConcurrency25 preview OK");
    }
}
