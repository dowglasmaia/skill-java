# Qualidade, bugs e testes

Em bug: entenda o esperado, reproduza/delimite defeito, investigue causa e efeitos, localize testes, acrescente regressão quando viável, aplique menor mudança e valide. Não altere nomes/pacotes/arquitetura/contratos alheios.

Teste comportamento: limites, null conforme contrato, vazio, erro, transação/rollback, concorrência/cancelamento quando relevantes. Preserve JUnit/TestNG, mocks e fixtures locais. Integração é necessária para defeito em SQL, mapeamento, serialização ou configuração; mock que reproduz implementação não prova isso.

Prefira Clock fixo, aleatoriedade controlada e sincronização explícita; sleeps arbitrários fragilizam concorrência. Sem acesso externo em teste unitário. Verifique mensagem/status quando contrato público, não detalhes internos sem necessidade.

Preserve exceções: não capture Exception para retornar sucesso/vazio; acrescente contexto sem perder causa. Evite log duplicado em todas as camadas; use API parametrizada/contexto do projeto sem dados sensíveis desnecessários.

Execute testes, formatter, lint e análise estática pertinentes já definidos. Use mvnw.cmd/gradlew.bat no Windows ou wrappers Unix. Comece focalizado; amplie conforme risco/regras locais. Relate comandos/resultados/bloqueios; compilação não é teste funcional.

Em review, priorize defeito demonstrável, compatibilidade, contratos e concorrência antes de preferência. Refatoração preserva ordem observável, mutabilidade, exceções e transações. Veja [checklists](../checklists/workflows.md).
