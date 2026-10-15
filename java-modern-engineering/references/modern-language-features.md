# Linguagem e modelagem

Versões de introdução, finalização e compatibilidade estão na [matriz](java-version-matrix.md). Os trechos são fragmentos inseridos em método/classe apropriados, com tipos de domínio exemplificativos; exemplos completos compiláveis estão em [Java17Examples](../examples/Java17Examples.java), [Java21Examples](../examples/Java21Examples.java) e [Java25Examples](../examples/Java25Examples.java).

## Records — 14 preview, 16 final; 17/21/25

Carregadores transparentes de valores, com componentes, construtor canônico, acessores e igualdade derivados. Use para DTOs e valores cuja representação seja o contrato. Evite substituir entidades JPA, hierarquias abertas ou identidade/mutação de domínio sem avaliar persistência e serialização. Podem ter comportamento e validação; não são restritos a DTOs.

EVITAR: classe DTO com dezenas de linhas de igualdade/getters manuais, ou `record Basket(List<String> items) {}` expondo uma lista mutável recebida.

PREFERIR, quando o contrato exige snapshot não modificável:
```java
record Basket(java.util.List<String> items) {
    Basket { items = java.util.List.copyOf(items); }
}
```
JUSTIFICATIVA: componentes finais são apenas superficialmente imutáveis; copiar a coleção não copia elementos mutáveis. EXCEÇÕES: uma classe comum preserva representação privada, identidade e integração com frameworks que exigem proxies/mutação. Não prometa menor alocação por usar record. [JEP 395](https://openjdk.org/jeps/395).

## Sealed classes e interfaces — 15 preview, 17 final; 17/21/25

Fecham um conjunto de subtipos. Use para alternativas de domínio controladas; evite em pontos de extensão por plugins. Subtipos diretos devem ser final, sealed ou non-sealed (records são finais), no mesmo módulo nomeado ou mesmo pacote no módulo não nomeado.
```java
sealed interface Result permits Ok, Failure {}
record Ok(String value) implements Result {}
record Failure(String reason) implements Result {}
```
EVITAR: classe aberta com tags de texto e casts espalhados. PREFERIR: variantes tipadas quando realmente finitas. JUSTIFICATIVA: restringe estados e facilita exaustividade; adicionar variante pode exigir atualizar consumidores. EXCEÇÃO: contrato aberto deliberado. [JEP 409](https://openjdk.org/jeps/409).

## Pattern matching instanceof — 14 preview, 16 final; 17/21/25

Testa e vincula variável com escopo controlado pelo fluxo.
```java
// EVITAR
if (value instanceof String) { consume((String) value); }
// PREFERIR
if (value instanceof String text) { consume(text); }
```
Remove cast duplicado; não substitui polimorfismo de domínio por cadeias de testes de tipo. Null não casa com o padrão. Use cast explícito onde o contrato já garante o tipo e não há teste necessário. [JEP 394](https://openjdk.org/jeps/394).

## Switch expressions — 12 preview, 14 final; 17/21/25

Produzem um valor e exigem exaustividade; `yield` retorna de um bloco de ramo.
```java
String result = switch (status) {
    case ACTIVE -> "Active";
    case INACTIVE -> "Inactive";
    default -> "Unknown";
};
```
EVITAR: variável temporária atribuída em cada case com break, quando só se calcula um valor. JUSTIFICATIVA: reduz estados intermediários e fall-through acidental. EXCEÇÕES: switch statement para efeitos; fall-through intencional e documentado ainda pode justificar forma tradicional. Não acrescente default que esconda novos valores de enum quando o contrato pede tratamento explícito. [JEP 361](https://openjdk.org/jeps/361).

## Pattern switch — 17 preview, 21 final; produção em 21/25

Seleciona por tipo/padrão, com guardas `when` e ramo `case null` explícito quando necessário.
```java
String label = switch (value) {
    case null -> "absent";
    case String s when s.isBlank() -> "blank";
    case String s -> s;
    default -> value.toString();
};
```
EVITAR: case amplo antes do específico (dominância; não compila) ou assumir que default absorve null. PREFERIR: ramos específicos antes dos gerais e política de null explícita. EXCEÇÃO: comportamento polimórfico em uma hierarquia extensível pode ser mais coeso. Não copie sintaxe preview do 17 para 21. [JEP 441](https://openjdk.org/jeps/441).

## Record patterns — 19 preview, 21 final; 21/25

Desestruturam componentes durante casamento. Com `record Point(int x, int y) {}`:
```java
// EVITAR quando apenas extrair componentes
if (value instanceof Point p) { consume(p.x() + p.y()); }
// PREFERIR se a desestruturação melhora a leitura
if (value instanceof Point(int x, int y)) { consume(x + y); }
```
Evite padrões profundamente aninhados que escondam intenção. Não use record patterns no cabeçalho enhanced-for final do 21; essa forma de preview foi removida. EXCEÇÃO: acessor simples pode ser mais claro quando só um componente interessa. [JEP 440](https://openjdk.org/jeps/440).

## Text blocks — 13 preview, 15 final; 17/21/25

Literais multilinha com remoção de indentação incidental; não são strings raw nem interpolação.
```java
String sql = """
    SELECT id, name
    FROM customer
    WHERE id = ?
    """;
```
EVITAR: concatenação de linhas com escapes desnecessários ou `.formatted(userInput)` dentro de SQL. PREFERIR: text block e binding via PreparedStatement. EXCEÇÕES: literal curto cabe em uma linha. Verifique newline final e espaços em snapshots/protocolos; menor ruído não é ganho garantido de performance. [JEP 378](https://openjdk.org/jeps/378).

## String.formatted — 15 final; 17/21/25

Formatação por especificadores, equivalente à formatação de String com o receptor como formato.
```java
String message = "Customer %s has %d orders".formatted(customerName, totalOrders);
```
EVITAR: concatenação fragmentada que dificulta ler a frase. PREFERIR o exemplo quando melhora clareza; preserve `"id=" + id` para caso simples. Formatação tem parsing e pode custar mais. Para locale fixo use `String.format(Locale.ROOT, ...)`; para concatenação incremental em loop use StringBuilder. Logging deve usar a API parametrizada do projeto. Não garante escaping de SQL, HTML ou comandos. [String API](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html).

## var — 10 local, 11 parâmetros lambda; 17/21/25

Inferência estática local, não tipagem dinâmica.
```java
// EVITAR quando o retorno não revela o tipo: var data = service.execute();
// PREFERIR quando o inicializador explica o tipo:
var names = new java.util.ArrayList<String>();
```
Use tipo explícito quando documenta contrato ou abstração; `var` infere tipo concreto e pode afetar overloads após mudanças no inicializador. Não serve para campos, retorno ou parâmetro comum. Não misture estilos de parâmetros numa mesma lambda. Sem vantagem de performance inerente. [JEP 286](https://openjdk.org/jeps/286), [JEP 323](https://openjdk.org/jeps/323).

## Enhanced for — 5 final; 17/21/25

Iteração sobre array/Iterable sem índice manual.
```java
// EVITAR índice usado apenas para acesso: for (int i = 0; i < names.size(); i++) consume(names.get(i));
for (String name : names) { consume(name); }
```
Prefira quando o índice não participa da regra; evite remover estruturalmente da coleção durante esse loop. Iterator.remove, removeIf ou índice explícito podem ser adequados. Em lista encadeada, get(i) repetido pode criar custo quadrático. [JLS 14.14.2](https://docs.oracle.com/javase/specs/jls/se25/html/jls-14.html#jls-14.14.2).

## Unnamed variables e patterns — 21 preview, 22 final; estável em 25

`_` marca variável/padrão deliberadamente não utilizado, não um identificador legível depois.
```java
try { Integer.parseInt(input); }
catch (NumberFormatException _) { return false; }
```
EVITAR: nome artificial nunca lido ou descartar exceção cuja causa precisa ser reportada. PREFERIR `_` só quando ignorar é parte do contrato; `Point(int x, _)` ignora componente de record em padrão. Não use no alvo 21 sem preview explícito. EXCEÇÃO: dar nome é necessário para log/cause. [JEP 456](https://openjdk.org/jeps/456).

## Flexible constructor bodies — 22 preview, 25 final; 25

Permitem prólogo antes de super/this com restrições de contexto de construção inicial.
```java
class Positive extends Base {
    Positive(int value) {
        if (value <= 0) throw new IllegalArgumentException("value must be positive");
        super(value);
    }
}
```
EVITAR: publicar `this`, chamar método de instância ou ler estado não inicializado no prólogo. PREFERIR: validar/calcular argumentos antes do encadeamento; certas atribuições a campos da própria classe são permitidas, seguindo JLS. EXCEÇÃO: helper estático antes de Java 25. Não reformate construtores estáveis sem necessidade. [JEP 513](https://openjdk.org/jeps/513).

## Module import declarations — 23 preview, 25 final; 25

`import module java.base;` disponibiliza tipos públicos de pacotes exportados conforme regras de leitura do módulo. Não adiciona biblioteca ao build, não exige converter a aplicação em módulo e não altera dependências `requires` automaticamente.
```java
import module java.base;
// Em arquivo Java 25: List<String> names = List.of("Ana");
```
EVITAR: substituir imports existentes em massa ou assumir que imports resolvem módulos ausentes. PREFERIR onde simplifica exemplos ou segue convenção acordada. EXCEÇÃO: imports explícitos tornam origem/ambiguidade mais clara em serviços grandes. Sem otimização de runtime por si. [JEP 511](https://openjdk.org/jeps/511).

## Compact source files e instance main — 21 preview, 25 final; 25

Reduzem cerimônia para programas pequenos; exemplo completo de arquivo compacto Java 25:
```java
void main() { IO.println("Hello"); }
```
EVITAR: reestruturar classes de framework/bibliotecas como arquivos compactos. PREFERIR para introdução à linguagem e pequenos executáveis. EXCEÇÃO: classe explícita com public static main preserva convenções e compatibilidade. `IO.println` precisa do qualificador; não presuma import estático implícito. [JEP 512](https://openjdk.org/jeps/512).

## String Templates — histórico, sem finalização

[JEP 430](https://openjdk.org/jeps/430) entregou preview no 21; [JEP 459](https://openjdk.org/jeps/459), segundo preview no 22. A reavaliação para 23 foi retirada; não há String Templates no 25. EVITAR gerar chamadas ao processador STR como Java estável. PREFERIR concatenação curta, formatted/format, text blocks e StringBuilder conforme custo/clareza; para SQL, parâmetros vinculados. Não habilite preview para recuperar um recurso ausente. Confirme na [documentação consolidada](https://docs.oracle.com/en/java/javase/25/language/java-language-changes-release.html).
