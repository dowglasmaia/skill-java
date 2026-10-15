# Checklists por tarefa

Leia só a seção pertinente. São critérios, não exigência de relatar todos os itens.

## Bug fix
- Esperado e causa raiz compreendidos?
- Efeitos colaterais e testes inspecionados?
- Regressão adicionada/ajustada quando viável?
- Menor correção preserva contratos, arquitetura e versão?
- Validação executada e limites informados?

## Código novo
- Cabe em responsabilidade existente?
- Classe/interface/camada tem necessidade real?
- Nome, assinatura, visibilidade e invariantes claros?
- Erros, mutabilidade, concorrência e ciclo de vida tratados?
- Recurso finalizado no alvo e convenções respeitadas?
- Teste relevante sem espelhar implementação?

## Refatoração
- Escopo/benefício concretos, sem migração implícita?
- Baseline e compatibilidade preservados?
- Ordem, null, erros, mutabilidade e transações equivalentes?
- Etapas permitem atribuir regressões?
- Dependências/abstrações realmente necessárias?

## Code review
- Defeito demonstrável ou preferência?
- Compatibilidade, invariantes, contratos e segurança corretos?
- Falhas, cancelamento e recursos limitados?
- Evidência sustenta alegação de performance?
- Findings com localização, consequência e correção proporcional?
