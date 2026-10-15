# Orientação a objetos pragmática

Use para modelagem; não como autorização para reescrever o domínio.

- Proteja invariantes na construção e nas operações, não só no controller. Uma conta pode expor `withdraw(amount)` em vez de `setBalance(value)`, validando saldo, moeda e valor.
- Entidades têm identidade e ciclo de vida; valores se comparam por significado. Records podem conter comportamento, mas sua representação transparente e igualdade por componentes precisam ser adequadas. Não converta entidades JPA por economia de getters.
- Encapsular inclui coleções e elementos: copiar a lista impede alteração estrutural externa, mas Item mutável continua compartilhado. Escolha valores imutáveis, cópia de elementos ou operações controladas conforme o contrato.
- Prefira composição quando a relação é “usa”, e herança quando existe substituição real. InvoiceService usa TaxCalculator; não herda de um serviço de utilidades para reutilizar código.
- Interface com uma implementação pode ser legítima em porta externa, fronteira entre módulos ou contrato publicado. Não crie Service/ServiceImpl somente por hábito ou para possibilitar mock.
- Construtores recebem dependências obrigatórias; evite service locators e objetos parcialmente inicializados. Preserve a injeção do framework existente.
- Igualdade/hashCode precisam permanecer consistentes e estáveis durante permanência em HashMap/HashSet. Evite campo mutável como chave sem estratégia explícita.
- Use menor visibilidade necessária. Não torne método público só para testes; teste o contrato ou melhore coesão quando o desenho dificultar testes relevantes.

## SOLID como critérios

| Princípio | Pergunta útil | Excesso a evitar |
|---|---|---|
| SRP | Há motivos independentes para alterar este componente? | Uma classe por passo trivial |
| OCP | Existe eixo de variação real que precisa de extensão? | Strategy/factory para possibilidade imaginária |
| LSP | Subtipo preserva pré/pós-condições e expectativas? | Subclasse que proíbe operação válida da base |
| ISP | Consumidores dependem de operações que não usam? | Interfaces sem fronteira real |
| DIP | Política depende de detalhe volátil externo? | Interfaces em cada classe de valor estável |

Nomes devem refletir o domínio; prefira operação com intenção a método genérico com flags booleanas. DDD ajuda quando linguagem e invariantes são complexas; CRUD simples não exige aggregates, factories e specifications por convenção. Veja [pares 7 e 10](../examples/engineering-pairs.md).
