# Referências oficiais e protocolo de verificação

Recorte técnico: Java 17, 21 e 25. Conferência inicial: 2026-10-15. Não infira estado de outra versão a partir deste snapshot.

Antes de recomendar recurso recente:
1. Identifique o release alvo efetivo.
2. Leia especificação/API consolidada desse release e o marcador Preview/Since.
3. Confira a JEP, seu Release e o título (Delivered pode ser apenas preview).
4. Diferencie disponibilidade na plataforma, implementação HotSpot, suporte do vendor e flags.
5. Em conflito, priorize JLS/API final e documentação consolidada; registre incerteza e não habilite recurso experimental por suposição.

## Entradas primárias
- [JLS e JVMS](https://docs.oracle.com/javase/specs/)
- [Java 17](https://docs.oracle.com/en/java/javase/17/)
- [Java 21](https://docs.oracle.com/en/java/javase/21/)
- [Java 25](https://docs.oracle.com/en/java/javase/25/)
- [Mudanças de linguagem por release](https://docs.oracle.com/en/java/javase/25/language/java-language-changes-release.html)
- [Índice JEP](https://openjdk.org/jeps/0)
- [Projetos JDK](https://openjdk.org/projects/jdk/)
- [API 17](https://docs.oracle.com/en/java/javase/17/docs/api/index.html)
- [API 21](https://docs.oracle.com/en/java/javase/21/docs/api/index.html)
- [API 25](https://docs.oracle.com/en/java/javase/25/docs/api/index.html)

## JEPs por decisão
| Recurso | Fonte |
|---|---|
| Records | [395](https://openjdk.org/jeps/395) |
| instanceof | [394](https://openjdk.org/jeps/394) |
| Sealed | [409](https://openjdk.org/jeps/409) |
| Switch expressions | [361](https://openjdk.org/jeps/361) |
| Record patterns | [440](https://openjdk.org/jeps/440) |
| Pattern switch | [441](https://openjdk.org/jeps/441) |
| Text blocks | [378](https://openjdk.org/jeps/378) |
| var | [286](https://openjdk.org/jeps/286), [323](https://openjdk.org/jeps/323) |
| Unnamed variables/patterns | [443 preview](https://openjdk.org/jeps/443), [456 final](https://openjdk.org/jeps/456) |
| Virtual Threads | [444](https://openjdk.org/jeps/444), [491 pinning](https://openjdk.org/jeps/491) |
| Sequenced Collections | [431](https://openjdk.org/jeps/431) |
| Stream Gatherers | [485](https://openjdk.org/jeps/485) |
| Scoped Values | [506](https://openjdk.org/jeps/506) |
| Structured Concurrency, quinta preview | [505](https://openjdk.org/jeps/505) |
| Compact Object Headers | [519](https://openjdk.org/jeps/519) |
| Flexible Constructor Bodies | [513](https://openjdk.org/jeps/513) |
| Module Import Declarations | [511](https://openjdk.org/jeps/511) |
| Compact Source Files/Instance Main | [512](https://openjdk.org/jeps/512) |
| String Templates, histórico retirado | [430](https://openjdk.org/jeps/430), [459](https://openjdk.org/jeps/459) |
| JFR no OpenJDK | [328](https://openjdk.org/jeps/328) |

## Consolidação dos recursos recentes
- [ScopedValue 25: final](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/ScopedValue.html)
- [StructuredTaskScope 25: preview](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/concurrent/StructuredTaskScope.html)
- [Gatherers 25: Since 24](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/stream/Gatherers.html)
- [Flexible Constructor Bodies](https://docs.oracle.com/en/java/javase/25/language/flexible-constructor-bodies.html)
- [Module Import Declarations](https://docs.oracle.com/en/java/javase/25/language/module-import-declarations.html)
- [Virtual Threads 25](https://docs.oracle.com/en/java/javase/25/core/virtual-threads.html)
- [Mudanças do JDK 25, incluindo headers](https://docs.oracle.com/en/java/javase/25/migrate/significant-changes-jdk-25.html)
- [Opções do runtime 25](https://docs.oracle.com/en/java/javase/25/docs/specs/man/java.html)

Algumas páginas JEP retornaram HTTP 403 na conferência inicial; os links canônicos foram preservados como referências, e os pontos recentes foram confrontados com documentação consolidada Oracle. Não declare que todas as JEPs foram abertas com sucesso. Páginas de rascunho cr.openjdk.org não substituem APIs finais. Uma descrição genérica de migração pode conter texto defasado: consulte a página específica do recurso e a especificação final.
