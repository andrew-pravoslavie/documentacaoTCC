---
aliases: [Arquitetura do Modelo, Modelo Híbrido]
tags: [framework, modelo-hibrido, nist, c2m2]
---

# Arquitetura do Modelo Híbrido (NIST CSF 2.0 x C2M2)

## A Proposta de Integração Conceitual
O framework desenvolvido neste TCC organiza a segurança da informação para parques de até 100 ativos por meio de uma matriz bidimensional:

```
                  ┌──────────────────────────────────────────────────────────┐
                  │                 NÍVEIS DE MATURIDADE C2M2                │
                  │   MIL 0           MIL 1           MIL 2           MIL 3  │
┌─────────────────┼───────────────┬───────────────┬───────────────┬──────────┤
│ 1. GOVERNAR     │ Inexistente   │ Ad-hoc        │ Padronizado * │ Otimizado│
│ 2. IDENTIFICAR  │ Inexistente   │ Ad-hoc        │ Padronizado * │ Otimizado│
│ 3. PROTEGER     │ Inexistente   │ Ad-hoc        │ Padronizado * │ Otimizado│
│ 4. DETECTAR     │ Inexistente   │ Ad-hoc        │ Padronizado * │ Otimizado│
│ 5. RESPONDER    │ Inexistente   │ Ad-hoc        │ Padronizado * │ Otimizado│
│ 6. RECUPERAR    │ Inexistente   │ Ad-hoc        │ Padronizado * │ Otimizado│
└─────────────────┴───────────────┴───────────────┴───────────────┴──────────┘
                         * = Patamar Mínimo Recomendado no TCC
```

1. **Eixo Temático (Vertical):** As 6 funções do [[NIST CSF 2.0]], garantindo cobertura de ciclo completo de vida da segurança (Governança, Identificação, Proteção, Detecção, Resposta e Recuperação).
2. **Eixo de Progressão (Horizontal):** A escala de maturidade do [[C2M2 (Maturity Model)]] (MIL 0 a MIL 3), fornecendo um caminho claro e mensurável de evolução.
