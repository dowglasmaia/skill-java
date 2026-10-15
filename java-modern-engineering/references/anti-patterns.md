# Antipadrões e alternativa mínima

Use para revisão/refatoração solicitada, sem reformar código não relacionado.

| Sinal | Risco | Alternativa |
|---|---|---|
| Record em toda entidade | Identidade/proxy/persistência incompatíveis | Classe de domínio; record para valor apropriado |
| Interface + Impl universal | Indireção sem fronteira real | Classe concreta; contrato na porta/variação real |
| Stream com estado/efeitos | Ordem e concorrência obscuras | Loop explícito ou pipeline puro |
| parallelStream para rede/banco | Saturação e falhas opacas | Executor com limite/timeout/ownership |
| Virtual Threads para CPU | Ganho esperado sem capacidade extra | Reduzir complexidade, medir paralelismo limitado |
| Optional.get sem garantia | Falha acidental por ausência | map/flatMap/orElseThrow |
| unmodifiableList como cópia | Backing externo ainda muda | copyOf/cópia defensiva adequada a null |
| Dinheiro como double | Erro decimal/rounding | Decimal/unidade mínima com moeda |
| Comparator por subtração | Overflow quebra ordenação | comparingInt/Long e desempate |
| Factory/Strategy para regra única | Arquivos/indireção sem valor | Método coeso |
| Modernizar durante bug | Regressão e revisão ampliadas | Patch mínimo com regressão |
| Preview/STR como padrão | Falha no alvo | API finalizada confirmada |
| Trocar locks/GC sem medir | Custo sem resolver causa | Evidência de contenção/pausas |
| Ignorar Future/interrupção | Falha perdida/trabalho órfão | Coletar falha, propagar cancelamento |

Pares concretos em [engineering-pairs](../examples/engineering-pairs.md).
