---
aliases: [Endpoints, Estações de Trabalho, Notebooks]
tags: [ativos, endpoints]
---

# Ativos: Endpoints e Estações de Trabalho

## Descrição no Parque (<100 ativos)
Compreende desktops corporativos e notebooks utilizados pelos funcionários administrativos, operacionais e de gestão.

## Vulnerabilidades Típicas
- Usuários executando rotinas com contas de privilégio de **Administrador Local**.
- Antivírus desativado, gratuito ou com assinaturas desatualizadas.
- Falta de bloqueio automático de tela após inatividade.
- Uso indevido de pendrives pessoais sem restrição de portas USB.

## Controles Essenciais (Rumo ao MIL 2)
- [ ] Implementar usuário padrão (não administrador) para todas as tarefas cotidianas.
- [ ] Instalação de antivírus corporativo gerenciado centralmente.
- [ ] Criptografia de disco (ex: BitLocker) em todos os notebooks que saem da empresa.
- [ ] Política de bloqueio de tela automático em 5 minutos.
