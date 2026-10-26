---
aliases: [Recuperar, RC, Continuidade]
tags: [framework, nist, recuperar]
---

# Função 6: RECUPERAR (RC) em Parques de até 100 Ativos

## Objetivo
Restaurar os sistemas, dados e operações do negócio após uma interrupção ou ataque cibernético.

## Práticas por Nível de Maturidade

| Código | Prática Simplificada | MIL 1 (Ad-hoc) | MIL 2 (Recomendado) | MIL 3 (Institucionalizado) |
| :--- | :--- | :--- | :--- | :--- |
| **RC-01** | **Rotina de Backup dos Dados** | Backup feito manualmente em um pendrive quando alguém lembra. | Backup diário automatizado com pelo menos 1 cópia imutável ou em nuvem. | Replicação contínua com RPO/RTO mínimo e failover automatizado. |
| **RC-02** | **Teste de Restauração (Restore Test)** | O teste é feito no dia em que o servidor quebra. | Simulação trimestral/semestral documentada de restauração de backup. | Testes automatizados contínuos de integridade de recuperação. |
