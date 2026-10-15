# Concorrência e Virtual Threads

Versões e presença 17/21/25 na [matriz](java-version-matrix.md). As escolhas abaixo não autorizam trocar o modelo de execução do projeto.

| Recurso | Aplicação e exemplo preferível | Antipadrão, efeitos e exceções |
|---|---|---|
| Virtual Threads (19 P; 21 final) | Muitas tarefas com I/O bloqueante; `Executors.newVirtualThreadPerTaskExecutor()` com ciclo de vida, deadline e limite externo | Não criar pool fixo de virtual threads nem presumir CPU mais rápida. Tarefas ainda consomem memória; limite entrada, fan-out e downstream. Ausente 17. |
| Platform Threads (1.0 final) | Pools dimensionados para CPU ou integração que exige thread de plataforma | Uma thread por item ilimitado esgota recursos. Não redimensionar sem medir fila/capacidade; I/O de baixa concorrência pode continuar adequado. |
| ExecutorService (5 final; close desde 19) | Dono explícito: shutdown/awaitTermination no 17, try-with-resources no 21/25 | submit ignorando Future esconde falha; newFixedThreadPool tem fila não limitada. Use fila limitada/rejeição para carga ilimitada. Não feche executor compartilhado por requisição. |
| CompletableFuture (8 final) | Compor etapas: `fetch.thenCompose(this::loadDetails)`; executor explícito para bloqueio | supplyAsync com I/O no common pool prejudica outras tarefas. thenApply que retorna Future cria nesting. orTimeout não interrompe automaticamente o I/O subjacente. |
| Structured Concurrency (19 incubator; 21 preview; 25 quinta preview) | Experimento autorizado: subtarefas com join, falha e cancelamento coordenados | Não tratar como final nem copiar ShutdownOnFailure antigo para 25. Veja [exemplo preview 25](../examples/StructuredConcurrency25.java). Sem preview, preserve executores/futures estáveis com gestão explícita. |
| Scoped Values (20 incubator; 21 preview; 25 final) | Contexto de leitura delimitado: `ScopedValue.where(KEY, value).run(action)` | Não substitui parâmetro simples nem estado mutável compartilhado. get fora de binding falha. Não há herança automática em qualquer executor; integração estruturada tem regras próprias. |
| ThreadLocal (1.2 final) | Contexto legado por thread: set, try, finally remove | Sem remove em thread reutilizada vaza contexto. Cache pesado por virtual thread multiplica memória. Não assume propagação async; preserve integração do framework. |
| synchronized (1.0; JMM revisado 5) | Estado composto sob lock privado: `synchronized(lock) { balance = balance.add(amount); }` | Locks em strings internadas/objetos públicos causam interferência. Evite I/O sob lock. `volatile int count; count++` não é atômico. |
| Concorrência segura (JMM; atomics/locks/concurrent desde 5) | AtomicInteger.incrementAndGet, ConcurrentHashMap.compute por chave, lock para invariante multiobjeto | Coleção concorrente não torna containsKey/put ou várias chaves atômicas. final não garante imutabilidade profunda; publicação/acesso devem respeitar happens-before. |

## I/O com limites

O [exemplo Java 21](../examples/Java21Examples.java) usa lote limitado, HttpClient com connect/request timeouts, semaphore com timeout, prazo de invokeAll, cancelamento e coleta de falhas. Sem URLs, seu main não acessa rede.

Reutilize HttpClient no ciclo de vida apropriado. Dimensione concorrência pela capacidade do serviço/pool de conexões. Em servidor, o semaphore deve ser compartilhado no escopo do recurso; um por requisição não limita carga global. Valide status HTTP/payload.

Timeout de espera não garante encerramento imediato. Cancelamento é cooperativo; drivers precisam de seus próprios timeouts. ExecutorService.close espera tarefas terminarem, podendo superar deadline se ignorarem cancelamento. Não prometa limite rígido sem suporte das dependências.

Propague InterruptedException quando permitido; ao capturar sem propagar, restaure interrupt e encerre trabalho. Não transforme interrupção em sucesso vazio. Considere idempotência antes de retry após execução parcial.

## Pinning depende do JDK

No 21, bloqueio prolongado em synchronized pode prender virtual thread ao carrier; investigue com JFR antes de mudar locks. A [JEP 491](https://openjdk.org/jeps/491), entregue no 24 e presente no 25, remove pinning causado por monitores. Código nativo/foreign ainda exige análise. Não converta todo synchronized em ReentrantLock com orientação desatualizada.

Fontes: [JEP 444](https://openjdk.org/jeps/444), [Virtual Threads 25](https://docs.oracle.com/en/java/javase/25/core/virtual-threads.html), [JEP 505](https://openjdk.org/jeps/505), [StructuredTaskScope 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/concurrent/StructuredTaskScope.html), [JEP 506](https://openjdk.org/jeps/506), [ScopedValue 25](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/ScopedValue.html), [JLS 17](https://docs.oracle.com/javase/specs/jls/se25/html/jls-17.html), [ExecutorService](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/concurrent/ExecutorService.html), [CompletableFuture](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/concurrent/CompletableFuture.html).
