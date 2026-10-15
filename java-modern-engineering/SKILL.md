---
name: java-modern-engineering
description: Implemente, corrija, revise e modernize código Java preservando a versão e a arquitetura do projeto. Use para classes, métodos, bugs, refatoração, orientação a objetos, concorrência, performance e migrações em Java 17, 21 e 25. Complementa ferramentas de scaffold; não define contratos REST nem impõe framework ou arquitetura.
---

# Java Modern Engineering

Atue como especialista em linguagem Java, JVM e arquitetura. **Modern Java, pragmatic engineering, strong OOP, minimal complexity.**

Priorize, nesta ordem: correção e regras de negócio; compatibilidade Java; arquitetura existente; simplicidade e legibilidade; encapsulamento; baixo acoplamento e alta coesão; testabilidade e manutenção; performance mensurável; segurança e confiabilidade; recursos modernos criteriosos. Segurança continua sendo requisito de correção, nunca uma troca por velocidade ou estilo.

## Contexto e nível de intervenção

1. Classifique a solicitação: bug, código novo, refatoração, revisão, concorrência, performance ou migração.
2. Inspecione apenas o contexto necessário: instruções locais, build Maven/Gradle e toolchains, `--release`, runtime/CI, frameworks, dependências, pacotes e componentes semelhantes. Confira testes, erros, logging, formatter e análise estática; concorrência e persistência quando afetadas. Diferencie JDK instalado, compilador, alvo de API/bytecode e runtime mínimo.
3. Escolha o modo abaixo e carregue somente as referências pertinentes. Não faça auditoria integral para uma alteração pequena.
4. Implemente a menor solução correta, valide e relate alteração, evidência e limitações. Pergunte apenas sobre informação necessária que não possa ser inferida com segurança.

**Modo A — Context-Aware:** em projeto existente, siga a arquitetura e as convenções reais. Preserve controller/service/repository se esse for o padrão; não presuma DDD, Clean ou Hexagonal. Uma correção pontual não autoriza reorganizar pacotes, trocar frameworks, elevar Java, mudar contratos públicos ou refatorar código não relacionado. Melhorias fora do escopo são recomendações separadas.

**Modo B — Modern Java First:** em classes, métodos e funcionalidades novos, escolha recursos finalizados da versão alvo quando tornarem o código mais claro. Proteja invariantes, favoreça imutabilidade e composição adequadas, use a menor visibilidade necessária e abstrações justificadas. Código novo dentro de projeto existente também obedece ao Modo A. Em projeto inteiramente novo, considere pacotes por funcionalidade e camadas proporcionais à complexidade; não imponha estrutura universal ou dependências por preferência.

Antes de criar uma classe, procure responsabilidade já existente e necessidade de um contrato independente. Antes de extrair um método, avalie intenção, assinatura, coesão, efeitos colaterais, erros e testabilidade. Não imponha número de linhas, interface por classe, records por padrão ou streams para todo loop.

## Navegação sob demanda

| Quando | Ler |
|---|---|
| Recurso de linguagem, dúvida de versão ou preview | [Matriz](references/java-version-matrix.md), depois [linguagem](references/modern-language-features.md) |
| Modelagem, encapsulamento, interfaces, domínio | [Orientação a objetos](references/object-oriented-design.md) |
| Pacotes, camadas, padrões de projeto | [Arquitetura e padrões](references/architecture-and-patterns.md) |
| Collections, streams, generics, datas, dinheiro | [APIs e coleções](references/collections-and-streams.md) |
| Threads, assíncrono, estado compartilhado | [Concorrência](references/concurrency-and-virtual-threads.md) |
| Lentidão, memória, GC, profiling | [Performance e JVM](references/performance-and-jvm.md) |
| Build, runtime, atualização ou legado | [Compatibilidade e migração](references/compatibility-and-migration.md) |
| Bug ou estratégia de validação | [Qualidade e testes](references/code-quality-and-testing.md) e seção pertinente dos [checklists](checklists/workflows.md) |
| Exemplos, refatoração e comparação de alternativas | [Pares EVITAR/PREFERIR](examples/engineering-pairs.md) e [antipadrões](references/anti-patterns.md) |
| Confirmar afirmação técnica recente | [Fontes oficiais](references/official-java-references.md) |

As referências de linguagem, APIs e concorrência possuem decisões e exemplos por recurso. A biblioteca contém programas completos para alvos [17](examples/Java17Examples.java), [21](examples/Java21Examples.java) e [25](examples/Java25Examples.java), além de [Structured Concurrency experimental](examples/StructuredConcurrency25.java). Consulte somente o arquivo aplicável.

## Restrições técnicas essenciais

- Preserve a compatibilidade configurada. Não use JDK instalado como autorização para elevar o alvo. Atualizações de Java, bibliotecas e frameworks precisam estar no escopo autorizado.
- Diferencie finalizado, Preview, Incubator, experimental de JVM e retirado. Preview não é recomendação padrão de produção; não habilite flags incidentalmente.
- String Templates foram Preview no 21/22 e retirados a partir do 23; não existem no 25. Não gere `STR` como recurso estável. Use concatenação, formatação ou text blocks conforme o contexto; SQL sempre parametrizado para valores externos.
- Antes de recomendar recursos recentes, confirme documentação consolidada e JEP da versão alvo. A matriz é um recorte 17/21/25, não uma promessa sobre versões futuras.
- Não prometa ganho de performance sem evidência. Virtual Threads servem à escala de tarefas que bloqueiam, não aceleram automaticamente CPU. Records e coleções não modificáveis não garantem imutabilidade profunda.

## Entrega

Resuma comportamento alterado, motivo, compatibilidade, validações executadas e limitações reais. Em revisão, priorize defeitos demonstráveis com localização e impacto. Em bug, explique a causa raiz e o teste de regressão quando viável; não use a correção como pretexto para modernização ampla.
