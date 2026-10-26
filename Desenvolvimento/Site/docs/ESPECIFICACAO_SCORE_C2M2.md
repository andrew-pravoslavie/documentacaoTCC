# Especificação do Algoritmo de Score Cumulativo C2M2 × NIST CSF 2.0

---

## 1. Fundamentação Matemática do Modelo Híbrido

O modelo híbrido C2M2 × NIST CSF 2.0 combina **seis funções** do NIST Cybersecurity Framework 2.0 (Governança – **GV**, Identificação – **ID**, Proteção – **PR**, Detecção – **DE**, Resposta – **RS**, Recuperação – **RC**) com o **modelo de maturidade** do C2M2 (Capability Maturity Model Integration) estruturado em **quatro níveis (MIL 0 → MIL 1 → MIL 2 → MIL 3)**.

### 1.1. Progresão Cumulativa

Para cada função `f ∈ {GV, ID, PR, DE, RS, RC}` existe um conjunto de **práticas** `P_{f,l}` associadas a cada nível `l ∈ {0,1,2,3}`.  
A regra **cumulativa estrita** determina que o nível `MIL_f` alcançado por uma empresa em uma função `f` só pode ser `l` se **todas** as práticas dos níveis `0 … l` estiverem plenamente atendidas:

```
MIL_f = max { l ∈ {0,1,2,3} |  ∀ p ∈ ⋃_{k=0}^{l} P_{f,k} :  resposta(p) = "concluída" }
```

Esta regra garante a **trava** que impede “pular” níveis (ex.: não é possível alcançar MIL 2 sem ter concluído integralmente todas as práticas de MIL 1).

### 1.2. Score Percentual de Progresso

Mesmo que a trava cumulativa ainda não permita avançar ao próximo nível, queremos exibir no dashboard um **indicador de progresso parcial**. Para cada prática `p` associada ao nível `l` da função `f` definimos um peso `w_p` (geralmente `1` ou proporcional à importância). O **percentual de progresso** da função `f` é:

```
percentualProgresso_f = 100 × ( Σ_{p atendida} w_p ) / ( Σ_{p ∈ ⋃_{k=0}^{3} P_{f,k}} w_p )
```

Este valor varia de `0` a `100` e pode ser apresentado como barra ou radar.

---

## 2. Fórmulas Exatas

### 2.1. Contribuição de Cada Questão/Opção

Cada questão do questionário está mapeada a uma prática `p`. As opções de resposta são associadas a um **nível de maturidade** (`mil`) – por exemplo, `0`, `1`, `2` ou `3`. A contribuição da questão `q` para o nível da função `f` é:

```
contrib_q = 1  se mil(q) ≥ l  para o nível atual l da função f
contrib_q = 0  caso contrário
```

O aglomerado de todas as questões de `f` determina se a condição de **cumulatividade** está satisfeita.

### 2.2. Regra Cumulativa Estrita (milAlcancado)

Para uma função `f`:

```
mileAlcancado_f = max { l |   ∀ p ∈ ⋃_{k=0}^{l} P_{f,k} :  mil(p) ≥ l }
```

onde `mil(p)` é o nível máximo atribuído à prática `p` pelas respostas.

### 2.3. Score Percentual da Função

```
scorePercentual_f = 100 × ( Σ_{p ∈ ⋃_{k=0}^{3} P_{f,k}} mil(p) ) / ( 3 × |⋃_{k=0}^{3} P_{f,k}| )
```

(3 é o nível máximo – MIL 3). Este cálculo reflete a média ponderada das práticas.

### 2.4. Cálculo do MIL Global da Empresa

O **MIL global** (`milGlobal`) pode ser expresso de duas formas, conforme necessidade:

1. **Média simples:**
   ```
   milGlobal = ( Σ_{f} milAlcancado_f ) / 6
   ```
2. **Média ponderada por número de práticas:**
   ```
   milGlobal = ( Σ_{f} ( milAlcancado_f × |P_f| ) ) / ( Σ_{f} |P_f| )
   ```
   onde `|P_f|` é a cardinalidade total de práticas da função `f`.

### 2.5. Score Global (%)

Para refletir o avanço total, combinamos os scores percentuais por função:

```
scoreGlobal = ( Σ_{f} scorePercentual_f ) / 6
```

Este campo corresponde ao atributo `Diagnostico.scoreGlobal` já existente.

### 2.6. Geração Automática de Gap e Matriz de Priorização (`Impacto × Custo`)

Para cada questão respondida cujo nível selecionado seja inferior ao alvo (`mil(q) < 2`, isto é, `MIL_0` ou `MIL_1` frente à meta `MIL_2`), o sistema gera automaticamente um registro na entidade `Gap` (`tb_gap`).

No domínio Java (`br.edu.securedesk.entity.governanca`), a priorização utiliza os seguintes Enums:
- **`NivelImpacto`**: `BAIXO` (1), `MEDIO` (2), `ALTO` (3)
- **`CustoMitigacao`**: `BAIXO` (1 — rápida implementação / baixo custo), `MEDIO` (2), `ALTO` (3 — alta complexidade / investimento)
- **`Prioridade`**: `BAIXA`, `MEDIA`, `ALTA`, `CRITICA`

#### Regra de Priorização (Quick Wins para PMEs):
Para pequenas e médias empresas com recursos restritos, controles de **Alto Impacto** no negócio, grande **Distância ao MIL 2** (`distancia = 2 - mil(q)`) e **Baixo Custo de Mitigação** devem ter prioridade máxima (**Quick Wins**):

```
IndicePrioridade = (PesoImpacto × (2 - mil(q))) / PesoCusto
```

Tabela de Decisão Direta (`NivelImpacto × CustoMitigacao` para `MIL_0` / `MIL_1`):

| Impacto no Negócio (`NivelImpacto`) | Custo Baixo (`BAIXO`) | Custo Médio (`MEDIO`) | Custo Alto (`ALTO`) |
|---|---|---|---|
| **Alto (`ALTO`)** | `CRITICA` *(Quick Win imediato)* | `ALTA` | `MEDIA` (ou `ALTA` se `MIL_0`) |
| **Médio (`MEDIO`)** | `ALTA` | `MEDIA` | `BAIXA` |
| **Baixo (`BAIXO`)** | `MEDIA` | `BAIXA` | `BAIXA` |


---

## 3. Fórmulas Equivalentes em Planilha (Excel / Google Sheets / LibreOffice Calc)

| Conceito                                     | Fórmula (Google Sheets)                                                                                                 |
|----------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| Total de práticas por função `f`             | `=COUNTA(Práticas_f)`                                                                                                   |
| Práticas atendidas (mil ≥ l)                 | `=COUNTIFS(Práticas_f!B:B, ">="&l, Práticas_f!C:C, f)`  *(coluna B = mil, C = função)*                                 |
| `milAlcancado_f` (cumulativo)                | `=MAX({0,1,2,3}* (COUNTIFS(Práticas_f!B:B, ">="&{0,1,2,3}, Práticas_f!C:C, f)=COUNTIFS(Práticas_f!C:C, f)))`         |
| `scorePercentual_f`                          | `=SUM(Práticas_f!B:B) / (3 * COUNTA(Práticas_f!B:B)) * 100`                                                            |
| `milGlobal` (média simples)                 | `=AVERAGE(milAlcancado_GV, milAlcancado_ID, milAlcancado_PR, milAlcancado_DE, milAlcancado_RS, milAlcancado_RC)`        |
| `scoreGlobal`                                | `=AVERAGE(scorePercentual_GV, scorePercentual_ID, scorePercentual_PR, scorePercentual_DE, scorePercentual_RS, scorePercentual_RC)` |
| `IndicePrioridade` (para cada gap)           | `=(PesoImpacto * (2 - Mil)) / PesoCusto`                                                                                 |

> **Nota:** Substitua os nomes das abas/colunas conforme a estrutura da sua planilha OED 1.

---

## 4. Matriz de Casos de Teste Determinísticos

| # | Descrição do Cenário | Dados de Entrada (questões da função) | Expectativa de `milAlcancado_f` (Cumulativo) | Expectativa de `scorePercentual_f` | Expectativa de `milGlobal` | Expectativa de `scoreGlobal` | Observações |
|---|----------------------|-----------------------------|--------------------------------------------|-----------------------------------|----------------------------|------------------------------|-------------|
| 1 | **Empresa iniciada (MIL 0)** – todas as questões em `MIL_0`. | Todas as respostas com `mil = 0`. | `0` (`MIL_0`) para todas as funções. | `0.0%` em todas as funções. | `0.00` | `0.0%` | Verifica linha de base inicial. |
| 2 | **Nível MIL 1 completo** – todas as questões da função respondidas com pelo menos `MIL_1` (nenhuma em `MIL_0`), mas nem todas em `MIL_2`. | Todas as questões com `mil = 1`. | `1` (`MIL_1`) para todas as funções. | `33.33%` (1/3) em todas as funções. | `1.00` | `33.33%` | Confirma fechamento do degrau MIL 1. |
| 3 | **Buraco Cumulativo (Trava C2M2)** – 4 de 5 questões da função estão em `MIL_3`, mas **1 questão** está em `MIL_0`. | `[3, 3, 3, 3, 0]` na função `PR`. | `0` (`MIL_0`) na função `PR` (trava cumulativa!). | `80.0%` (`12 / 15`) na função `PR`. | Cai na função afetada | `80.0%` na função `PR` | **Caso crítico para a banca:** prova que a média seria `2.4` (`MIL 2`), mas pela regra cumulativa C2M2 o nível formal trava em `MIL 0` até sanar o buraco, enquanto o `scorePercentual` mostra os `80%` de progresso! |
| 4 | **Buraco no MIL 2** – 4 de 5 questões em `MIL_2`, e 1 questão em `MIL_1`. | `[2, 2, 2, 2, 1]` na função `ID`. | `1` (`MIL_1`) na função `ID`. | `60.0%` (`9 / 15`) na função `ID`. | `1.00` | `60.0%` | Todas atendem ≥ MIL 1 (fecha MIL 1), mas falta 1 questão para fechar o MIL 2. |
| 5 | **Patamar Alvo Recomendado (MIL 2)** – todas as questões respondidas com `MIL_2`. | `[2, 2, 2, 2, 2]` em todas as 6 funções. | `2` (`MIL_2`) em todas as funções (0 Gaps!). | `66.67%` (ou `100%` da meta MIL 2). | `2.00` | `66.67%` | Ponto de conformidade recomendado para PMEs (ISO 27001/27002). |
| 6 | **Excelência (MIL 3)** – todas as questões em `MIL_3`. | `[3, 3, 3, 3, 3]` em todas as 6 funções. | `3` (`MIL_3`) para todas as funções. | `100.0%` em todas as funções. | `3.00` | `100.0%` | Verifica teto máximo da escala. |
| 7 | **Desbalanceamento Realista de PME** – `GV=1, ID=2, PR=1, DE=0, RS=0, RC=1`. | Vetor de funções com maturidades distintas. | `GV:1, ID:2, PR:1, DE:0, RS:0, RC:1` | Proporcional a cada função. | `0.83` (média dos MILs) ou `0` (mínimo estrito) | Média das 6 funções | Caso típico do estudo de caso em PME brasileira. |
| 8 | **Geração e Priorização de Gaps (Quick Win vs Longo Prazo)** – Questão A (`MIL_0`, Impacto `ALTO`, Custo `BAIXO`) vs Questão B (`MIL_1`, Impacto `BAIXO`, Custo `ALTO`). | Duas questões abaixo de `MIL_2`. | Ambas geram `Gap` (`resolvido = false`). | – | – | – | Questão A recebe `Prioridade.CRITICA` (Quick Win); Questão B recebe `Prioridade.BAIXA`. |

> Cada caso de teste acima será espelhado na planilha de validação (OED 1) e implementado em `ScoreServiceTest` (JUnit 5).

---

## 5. Referências

- **C2M2 — Cybersecurity Capability Maturity Model v2.1** (U.S. Department of Energy — DOE).
- **NIST Cybersecurity Framework (CSF) 2.0** — Funções Govern (GV), Identify (ID), Protect (PR), Detect (DE), Respond (RS) e Recover (RC).
- **ABNT NBR ISO/IEC 27001 e 27002** — Controles de referência para o patamar alvo MIL 2.
- **PLANO_DESENVOLVIMENTO.md** — Item 1.4 / OED 1.

