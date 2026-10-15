# Matriz de versões

Recorte verificado em 2026-10-15. F = finalizado; P = Preview; I = Incubator; E = experimental de JVM; — = ausente. “Primeiro” registra o primeiro release público do recurso, inclusive preview; não confundir com finalização. APIs antigas não passaram necessariamente por preview. Consulte a documentação da versão alvo antes de recomendar novidades.

| Recurso | Primeiro | Finalizado | 17 | 21 | 25 |
|---|---|---|---|---|---|
| Records | 14 P | 16 | F | F | F |
| Sealed classes/interfaces | 15 P | 17 | F | F | F |
| Pattern matching instanceof | 14 P | 16 | F | F | F |
| Switch expressions | 12 P | 14 | F | F | F |
| Pattern matching switch | 17 P | 21 | P | F | F |
| Record patterns | 19 P | 21 | — | F | F |
| Text blocks | 13 P | 15 | F | F | F |
| String.formatted | 15 | 15 | F | F | F |
| var local / parâmetro lambda | 10 / 11 | 10 / 11 | F | F | F |
| Enhanced for | 5 | 5 | F | F | F |
| Unnamed variables/patterns | 21 P | 22 | — | P | F |
| Flexible constructor bodies | 22 P | 25 | — | — | F |
| Module import declarations | 23 P | 25 | — | — | F |
| Compact source files/instance main (nomes anteriores) | 21 P | 25 | — | P | F |
| Optional / Stream / Collectors | 8 | 8 | F | F | F |
| Stream.toList | 16 | 16 | F | F | F |
| Sequenced collections | 21 | 21 | — | F | F |
| Stream gatherers | 22 P | 24 | — | — | F |
| Collections.unmodifiable* (views) | 1.2 | 1.2 | F | F | F |
| List.of / Set.of / Map.of | 9 | 9 | F | F | F |
| List.copyOf / Set.copyOf / Map.copyOf | 10 | 10 | F | F | F |
| Generics | 5 | 5 | F | F | F |
| Comparator / comparing, thenComparing | 1.2 / 8 | 1.2 / 8 | F | F | F |
| java.time | 8 | 8 | F | F | F |
| BigDecimal | 1.1 | 1.1 | F | F | F |
| Virtual threads | 19 P | 21 | — | F | F |
| Platform threads (Thread) | 1.0 | 1.0 | F | F | F |
| ExecutorService | 5 | 5 | F | F | F |
| ExecutorService.close / AutoCloseable | 19 | 19 | — | F | F |
| CompletableFuture | 8 | 8 | F | F | F |
| Structured concurrency | 19 I | Não finalizado no 25 | — | P (1ª) | P (5ª) |
| Scoped values | 20 I | 25 | — | P | F |
| ThreadLocal | 1.2 | 1.2 | F | F | F |
| synchronized / volatile / monitores | 1.0 | 1.0; JMM revisado em 5 | F | F | F |
| java.util.concurrent / atomics / locks | 5 | 5 | F | F | F |
| Garbage collection | JVM original | Depende do coletor | Ver JVM | Ver JVM | Ver JVM |
| Compact object headers | 24 E | 25 (produto HotSpot) | — | — | F, opt-in |
| JFR no OpenJDK | 11 (havia Oracle JDK antes) | 11 via JEP 328 | F | F | F |
| JMH | Projeto externo, não release Java SE | Versionado separadamente | Dependência | Dependência | Dependência |
| String Templates | 21 P; 22 2ª P | Nunca | — | P | Retirado |

Não atribua a JMH um “Since Java” fictício. Verifique a versão do artefato e seu JDK mínimo; a primeira versão externa não determina disponibilidade em Java SE. GC também não é uma única API com um release de finalização.

Preview pode mudar de sintaxe entre releases; uma JEP com status Delivered pode entregar apenas um preview. Incubator usa módulos/APIs incubadoras; não equivale a recurso final. Flags experimentais HotSpot não são `--enable-preview`. “Retirado” não significa “deprecado ainda disponível”. Não declare String Templates permanentemente abandonado como conceito; a proposta foi retirada e não está no 25.

Fontes: [mudanças da linguagem](https://docs.oracle.com/en/java/javase/25/language/java-language-changes-release.html), [API 25](https://docs.oracle.com/en/java/javase/25/docs/api/index.html), [catálogo de fontes e JEPs](official-java-references.md). Detalhes de versões de GC em [performance](performance-and-jvm.md).
