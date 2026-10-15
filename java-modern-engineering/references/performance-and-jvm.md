# Performance e JVM

Estabeleça carga, baseline, hardware/container, JDK/vendor/flags e objetivo: latência p95/p99, throughput, heap ou startup. Investigue complexidade, estruturas, N+1/índices de banco e serialização antes de micro-otimizar sintaxe.

Meça CPU, alocações, boxing, retenção, GC, contenção, filas e I/O. Ganho local pode piorar cauda ou memória; compare mesma carga/recursos com aquecimento. Mais throughput não implica menor latência.

## Garbage Collectors

GC gerencia objetos inacessíveis; não fecha conexões/arquivos nem resolve retenção por referências vivas. Existe desde as primeiras JVMs; cada coletor tem história/disponibilidade próprias.

| Coletor HotSpot | Histórico | Decisão |
|---|---|---|
| Serial | Histórico, anterior ao 17; presente em 17/21/25 | Carga/heap pequenos; medir pausas |
| Parallel | Histórico, anterior ao 17; presente em 17/21/25 | Throughput com tolerância a pausas |
| G1 | Experimental 6u14, suportado 7u4, padrão em 9 nas configurações servidor usuais | Mantenha configuração existente inicialmente; metas de pausa não são garantias |
| ZGC | Experimental 11, produção 15; geracional 21, padrão geracional 23, não geracional removido 24 | Baixa pausa com custo/heap avaliados; não transferir flags do 17 cegamente ao 25 |
| Shenandoah | Experimental 12, produção 15; geracional experimental 24, produto 25 | Depende da distribuição/plataforma; OpenJDK não garante presença em todo vendor |

EVITAR trocar GC/heap por intuição. PREFERIR medir, por exemplo `-Xlog:gc*:file=gc.log:time,uptime,level,tags`, analisar pausas/ocupação e alterar uma variável por vez. EXCEÇÃO: requisito operacional pode orientar escolha inicial seguida de teste. Considere memória nativa, stacks, metaspace e container além de Xmx.

## Compact Object Headers — 24 experimental, 25 produto

Otimização HotSpot em arquiteturas suportadas; ausente 17/21, produto opt-in no 25. EVITAR lógica que dependa de tamanho fixo de objeto ou anunciar economia percentual universal. PREFERIR ensaio com `-XX:+UseCompactObjectHeaders` no 25, comparando heap, GC e throughput. No 24 exigia desbloquear experimental. Alinhamento/tamanho/distribuição de objetos influenciam resultado. [JEP 519](https://openjdk.org/jeps/519).

## Java Flight Recorder — OpenJDK 11 final; 17/21/25

Eventos para investigar CPU, alocação, locks, I/O e GC; já existia antes em Oracle JDK. Capture período representativo:
```text
jcmd <pid> JFR.start name=diagnosis settings=profile duration=60s filename=diagnosis.jfr
jfr summary diagnosis.jfr
```
EVITAR habilitar todos os eventos indefinidamente. PREFERIR gravação delimitada/configuração apropriada; overhead e dados dependem dos eventos. Mantenha arquivos potencialmente sensíveis no ambiente autorizado. JFR disponível não implica que todo evento novo é estável. [JEP 328](https://openjdk.org/jeps/328).

## JMH — projeto externo, não recurso Java SE

Use para hipótese estreita de código quente após profiling. Confira versão do artefato/JDK mínimo e annotation processor; não adicione ao runtime da aplicação. Fragmento em classe JMH configurada:
```java
@Benchmark
public int sum() { return values.stream().mapToInt(Integer::intValue).sum(); }
```
EVITAR nanoTime em loop com entrada constante e resultado descartado. PREFERIR @State/@Setup, parâmetros representativos, warmup, measurement e forks; retorne resultado/use Blackhole contra eliminação. O fragmento exige dependência/imports JMH. EXCEÇÃO: banco/rede/p95/p99 precisam teste de sistema; microbenchmark não substitui carga.

Fontes: [JMH](https://openjdk.org/projects/code-tools/jmh/), [fonte JMH](https://github.com/openjdk/jmh), [guia GC 25](https://docs.oracle.com/en/java/javase/25/gctuning/), [JEP 248](https://openjdk.org/jeps/248), [JEP 377](https://openjdk.org/jeps/377), [JEP 439](https://openjdk.org/jeps/439), [JEP 490](https://openjdk.org/jeps/490), [JEP 379](https://openjdk.org/jeps/379), [JEP 521](https://openjdk.org/jeps/521).
