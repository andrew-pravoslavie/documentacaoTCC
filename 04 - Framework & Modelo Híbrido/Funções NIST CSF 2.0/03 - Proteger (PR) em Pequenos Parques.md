---
aliases: [Proteger, PR, Salvaguardas]
tags: [framework, nist, proteger]
---

# Função 3: PROTEGER (PR) em Parques de até 100 Ativos

## Objetivo
Implementar salvaguardas que impeçam o sucesso ou mitiguem o impacto de ataques contra os ativos.

## Práticas por Nível de Maturidade

| Código | Prática Simplificada | MIL 1 (Ad-hoc) | MIL 2 (Recomendado) | MIL 3 (Institucionalizado) |
| :--- | :--- | :--- | :--- | :--- |
| **PR-01** | **Controle de Acessos & Privilégios** | Todos usam conta de administrador da máquina. | Usuário padrão para tarefas cotidianas; menor privilégio. | Gestão de acessos privilegiados (PAM) com gravação de sessão. |
| **PR-02** | **Autenticação Multifator (MFA)** | MFA apenas em contas pessoais voluntárias. | MFA obrigatório para todos os e-mails e acessos VPN/nuvem. | MFA baseado em hardware (FIDO2) com acesso condicional adaptativo. |
| **PR-03** | **Proteção de Endpoints** | Antivírus gratuito individual em algumas máquinas. | Antivírus corporativo centralizado com proteção em tempo real. | EDR corporativo integrado a resposta automatizada de ameaças. |
| **PR-04** | **Gestão de Atualizações (Patches)** | Usuário clica em "lembrar mais tarde" nas atualizações. | Windows Update / atualizações configuradas para instalação periódica. | Pipeline automatizado de testes e homologação de patches. |
