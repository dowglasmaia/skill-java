# Compatibilidade e migração

Separe JDK instalado, JDK do Maven/Gradle, toolchain do compilador, --release, JDK de testes e runtime mínimo (CI/container/deployment). Leia parent POM/profiles/propriedades efetivas quando relevantes. Toolchain e options.release têm papéis diferentes.

Maven, quando o contrato é Java 17:
```xml
<properties>
    <maven.compiler.release>17</maven.compiler.release>
</properties>
```
Gradle Kotlin DSL, exemplo se já usa toolchains:
```kotlin
java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }
tasks.withType<JavaCompile>().configureEach { options.release.set(17) }
```
Não imponha configuração sem entender plugins/módulos. JDK 25 com --release 17 limita sintaxe, APIs da plataforma e bytecode ao 17; não valida todas as bibliotecas externas nem substitui executar no runtime mínimo. Bytecode 25 não roda automaticamente no 17. source/target isolados não dão a mesma barreira de API.

## Migração autorizada

1. Fixe baseline funcional e contrato de runtime. Identifique reflexão, agentes, JNI, serialização, zonas e integrações afetadas.
2. Confira docs do alvo, frameworks/plugins e transitivas; use wrappers, não atualize tudo junto.
3. Separe troca de JDK da modernização sintática em etapas reversíveis.
4. Use jdeps/jdeprscan quando relevantes; --add-opens não é solução permanente sem justificativa.
5. Compile/teste/execute integrações no mínimo e alvo quando disponíveis. Compare performance se requisito.
6. Documente flags/compatibilidade e limitações. Não declare execução em 17 se só compilou com --release 17 no 25.

Preview exige release correspondente: `javac --enable-preview --release 25 ...` e `java --enable-preview ...` com JDK 25. Compilador novo não oferece preview antigo como compatibilidade. Incubator pode exigir --add-modules/pacotes distintos. Habilitação é escolha explícita de escopo, nunca efeito de copiar exemplo.

String Templates 21/22 não migram para 25: reescreva preservando escaping/semântica. StructuredTaskScope mudou entre previews; valide a API exata.

Fontes: [javac](https://docs.oracle.com/en/java/javase/25/docs/specs/man/javac.html), [migração](https://docs.oracle.com/en/java/javase/25/migrate/), [Maven release](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-release.html), [Gradle toolchains](https://docs.gradle.org/current/userguide/toolchains.html).
