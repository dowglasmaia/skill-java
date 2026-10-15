# Arquitetura e padrões: critério antes de estrutura

Inspecione uma funcionalidade existente antes de propor pacotes. Em alteração pontual, preserve camadas e direção de dependências; problemas externos ao escopo são sugestões separadas.

| Abordagem/padrão | Sinal de necessidade | Alternativa menor / quando evitar |
|---|---|---|
| Clean Code | Nomes opacos, efeitos escondidos, repetição perigosa | Clareza contextual; não metas arbitrárias de linhas |
| DDD | Invariantes, vocabulários e limites de negócio complexos | Modelo simples para CRUD |
| Clean Architecture | Políticas precisam independência de frameworks/adapters | Não duplicar DTOs/mappers em toda fronteira sem benefício |
| Hexagonal | Casos de uso precisam adapters ou isolamento de infraestrutura | Uma porta por fronteira significativa, não por classe |
| Package by Feature | Mudanças atravessam arquivos do mesmo domínio | Preserve layout existente em correção |
| Strategy | Algoritmos intercambiáveis selecionados por requisito | if/switch/método para regras simples estáveis |
| Factory | Construção depende de seleção/invariantes complexas | Construtor ou fábrica estática simples |
| Builder | Muitos opcionais ou construção incremental legível | Construtor pequeno/record; evitar estado inválido |
| Adapter | Integrar contrato externo incompatível | Chamada direta se contratos coincidem |
| Decorator | Adicionar comportamento componível sem mudar contrato | Método/helper para necessidade única |
| Command | Representar ação para fila, histórico ou desfazer | Chamada normal sem identidade/ciclo de vida da ação |
| Template Method | Esqueleto estável e variações controladas | Composição/Strategy evita acoplamento por herança |
| Repository | Acesso a aggregates/coleções de domínio | Preserve repository do framework; não criar wrapper vazio |
| Specification | Predicados reutilizados e combinados | Predicate/método; confira tradução SQL e custo |
| Dependency Injection | Dependências e ciclo de vida explícitos | Construtor manual em app pequeno; não adicionar contêiner por hábito |

Em projeto novo, considere pacotes por funcionalidade e encapsulamento por pacote; adicione fronteiras conforme domínio, integração e equipes. Pacotes, módulos Maven/Gradle e JPMS são decisões distintas. Evite ciclos; testes de arquitetura cabem quando existe regra relevante a proteger.

Java moderno não implica reatividade, microserviços ou event sourcing. Ligue recomendação a problema observado, impacto, alternativa menor e custo de migração.
