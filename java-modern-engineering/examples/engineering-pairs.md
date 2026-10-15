# Biblioteca de decisões: EVITAR → PREFERIR

Os trechos são fragmentos ilustrativos, com tipos/variáveis do domínio conforme o contexto. Os quatro arquivos .java vinculados abaixo são programas completos. Exemplos 1–8 e 10 têm alvo 17+; exemplo 9 exige 21+. Não copie um antipadrão como recomendação.

## 1. Switch que produz valor

EVITAR quando a única intenção é atribuir o resultado:
~~~java
String result;
switch (status) {
    case ACTIVE: result = "Active"; break;
    case INACTIVE: result = "Inactive"; break;
    default: result = "Unknown";
}
~~~
PREFERIR:
~~~java
String result = switch (status) {
    case ACTIVE -> "Active";
    case INACTIVE -> "Inactive";
    default -> "Unknown";
};
~~~
JUSTIFICATIVA: reduz estado intermediário e fall-through acidental.
EXCEÇÕES: statement para efeitos colaterais ou fall-through deliberado. Em enum fechado, não invente default que esconda novas variantes.

## 2. Pattern matching instanceof

EVITAR:
~~~java
if (value instanceof Customer) {
    Customer customer = (Customer) value;
    process(customer);
}
~~~
PREFERIR:
~~~java
if (value instanceof Customer customer) {
    process(customer);
}
~~~
JUSTIFICATIVA: elimina repetição e cast manual; variável só existe onde o teste garante o tipo.
EXCEÇÕES: polimorfismo pode evitar a seleção por tipo inteira. Null não casa.

## 3. Record para DTO

EVITAR para um simples valor de transporte:
~~~java
final class CustomerResponse {
    private final UUID id;
    private final String name;
    private final String email;
    CustomerResponse(UUID id, String name, String email) {
        this.id = id; this.name = name; this.email = email;
    }
    UUID getId() { return id; }
    String getName() { return name; }
    String getEmail() { return email; }
    // Igualdade, hashCode e toString ainda exigiriam implementacao.
}
~~~
PREFERIR, quando acessores por componente e igualdade de valor são o contrato:
~~~java
public record CustomerResponse(UUID id, String name, String email) {}
~~~
JUSTIFICATIVA: expressa o contrato de dados sem métodos repetitivos.
EXCEÇÕES: não troca automática de API (getName versus name), nem de entidade JPA/objeto com identidade. Record pode ter comportamento, mas é superficialmente imutável; elementos mutáveis continuam mutáveis.

## 4. Strings

EVITAR quando a fragmentação dificulta ler a frase:
~~~java
String message = "Customer " + customerName +
    " has " + totalOrders + " orders";
~~~
PREFERIR nesse contexto:
~~~java
String message = "Customer %s has %d orders".formatted(customerName, totalOrders);
~~~
JUSTIFICATIVA: frase fica reunida; não é afirmação de performance.
EXCEÇÕES: concatenação curta é clara e frequentemente mais barata. StringBuilder serve a construção incremental em loop. String.format com Locale explícito evita locale implícito quando relevante. Text blocks para várias linhas; SQL exige parâmetros, não interpolação.

## 5. Optional

EVITAR:
~~~java
Optional<Customer> customer = repository.findById(id);
if (customer.isPresent()) return customer.get();
throw new CustomerNotFoundException(id);
~~~
PREFERIR:
~~~java
return repository.findById(id)
    .orElseThrow(() -> new CustomerNotFoundException(id));
~~~
JUSTIFICATIVA: expressa a regra de ausência diretamente.
EXCEÇÕES: fluxo com vários efeitos pode precisar de if explícito; preserve contratos legados. Evite Optional em parâmetros obrigatórios, entidades persistentes e retorno de coleção quando vazio basta.

## 6. Cópia não modificável

EVITAR quando o objeto precisa de snapshot:
~~~java
this.items = items;
// Ou view que ainda acompanha mutacao externa:
this.items = Collections.unmodifiableList(items);
~~~
PREFERIR:
~~~java
this.items = List.copyOf(items);
~~~
JUSTIFICATIVA: alterações estruturais na lista original não afetam o snapshot.
EXCEÇÕES: copyOf rejeita null e não copia elementos mutáveis; uma view viva pode ser requisito. Para null permitido, copie para ArrayList e envolva em unmodifiableList; para imutabilidade profunda, modele/copiem-se também elementos.

## 7. Composição e interfaces

EVITAR herança de implementação sem relação de substituição:
~~~java
class InvoiceService extends TaxCalculator {
    BigDecimal gross(BigDecimal net, BigDecimal rate) {
        return net.add(calculate(net, rate));
    }
}
~~~
PREFERIR:
~~~java
final class InvoiceService {
    private final TaxCalculator taxes;
    InvoiceService(TaxCalculator taxes) { this.taxes = taxes; }
    BigDecimal gross(BigDecimal net, BigDecimal rate) {
        return net.add(taxes.calculate(net, rate));
    }
}
~~~
JUSTIFICATIVA: o serviço usa cálculo de imposto; não é um calculador substituível. A [versão completa](Java17Examples.java) valida a dependência.
EXCEÇÕES: hierarquia legítima com contrato substituível ou Template Method já adotado pode ser adequada.

EVITAR criar GreetingService + GreetingServiceImpl + GreetingServiceFactory para simplesmente concatenar saudação.
PREFERIR:
~~~java
final class Greeting {
    String message(String name) { return "Hello, " + name; }
}
~~~
JUSTIFICATIVA: nenhuma fronteira ou variação exige contrato adicional.
EXCEÇÕES: interface com uma implementação é válida para porta de infraestrutura, contrato público ou isolamento arquitetural real.

## 8. Streams versus loops

EVITAR acumulador externo para transformação pura:
~~~java
List<String> names = new ArrayList<>();
customers.forEach(c -> { if (c.active()) names.add(c.name()); });
~~~
PREFERIR quando o resultado pode ser não modificável:
~~~java
List<String> names = customers.stream()
    .filter(Customer::active).map(Customer::name).toList();
~~~
JUSTIFICATIVA: pipeline explicita seleção e projeção.
EXCEÇÕES: toList muda mutabilidade; use toCollection(ArrayList::new) se precisa preservar lista modificável.

EVITAR estado auxiliar dentro de stream para encontrar primeiro índice inválido.
PREFERIR:
~~~java
for (int i = 0; i < names.size(); i++) {
    if (names.get(i).isBlank()) return i;
}
return -1;
~~~
JUSTIFICATIVA: índice e retorno antecipado ficam claros.
EXCEÇÕES: para lista encadeada, use Iterator com contador para evitar get repetido quadrático; para só saber existência, anyMatch é mais direto.

## 9. Concorrência de I/O

EVITAR:
~~~java
uris.parallelStream().forEach(this::blockingHttpCall);
~~~
PREFERIR a implementação de [Java21Examples.fetch](Java21Examples.java): executor de virtual thread por tarefa, lote de até 32, semaphore de 4 acessos, timeout de aquisição/conexão/requisição, prazo do lote e falhas observadas. O método é exemplo standalone; reutilize client/limite adequadamente em serviço.
~~~java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    var futures = executor.invokeAll(tasks, 5, TimeUnit.SECONDS);
    for (var future : futures) {
        if (future.isCancelled()) throw new TimeoutException("Batch expired");
        consume(future.get());
    }
}
~~~
JUSTIFICATIVA: concorrência de tarefas bloqueantes com ownership e coleta de erro.
EXCEÇÕES: não acelera automaticamente CPU; framework reativo existente não requer reescrita. Threads não criam conexões infinitas. close pode esperar além do prazo se o trabalho ignorar interrupção; limite e timeout precisam alcançar o recurso de I/O.

## 10. Overengineering

EVITAR, para uma única regra fixa:
~~~java
interface ShippingStrategy { int fee(int subtotal); }
final class StandardShippingStrategy implements ShippingStrategy {
    public int fee(int subtotal) { return subtotal >= 10000 ? 0 : 500; }
}
final class ShippingFactory {
    ShippingStrategy create() { return new StandardShippingStrategy(); }
}
// Mais um service delegando factory -> strategy sem outra responsabilidade.
~~~
PREFERIR, com valores em centavos e regra de negócio definida:
~~~java
static int shippingFee(int subtotalCents) {
    if (subtotalCents < 0) throw new IllegalArgumentException("Negative subtotal");
    return subtotalCents >= 10000 ? 0 : 500;
}
~~~
JUSTIFICATIVA: uma regra coesa não precisa de mecanismo de extensão.
EXCEÇÕES: tarifas independentes, plugins ou seleção dinâmica real podem justificar Strategy/Factory; mantenha o padrão existente se já resolve requisito.

## Programas e execução

- [Java17Examples.java](Java17Examples.java): switch, instanceof, record/cópia, Optional, strings, SQL parametrizável, streams/loop, composição, decimal e Clock.
- [Java21Examples.java](Java21Examples.java): sealed, record patterns, pattern switch, sequenced collections e virtual threads.
- [Java25Examples.java](Java25Examples.java): arquivo compacto, instance main, module import, flexible constructors, unnamed patterns, gatherers e scoped values.
- [StructuredConcurrency25.java](StructuredConcurrency25.java): somente preview 25, isolado dos exemplos de produção.

Compile cada programa em diretório de saída separado com javac --release 17, 21 ou 25 conforme o arquivo e execute java -ea para ativar as verificações. O exemplo StructuredConcurrency25 precisa --enable-preview na compilação e execução, somente no JDK 25. Compilar --release 17/21 no JDK 25 não equivale a testar runtime 17/21.
