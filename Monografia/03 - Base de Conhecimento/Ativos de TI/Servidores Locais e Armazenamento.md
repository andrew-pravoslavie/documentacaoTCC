---
aliases: [Servidores, Storage, NAS]
tags: [ativos, servidores]
---

# Ativos: Servidores Locais e Armazenamento

## Descrição no Parque (<100 ativos)
Servidores físicos (ou pequenas VMs) instalados no ambiente da PME: controladores de domínio (Active Directory), servidores de arquivos (SMB/Samba), servidores de bancos de dados locais e storages NAS.

## Vulnerabilidades Típicas
- Acesso aberto ou pastas de rede com permissão "Everyone: Full Control".
- Falta de no-break (UPS) ou equipamentos em salas sem chave/climatização.
- Falta de isolamento entre o controlador de domínio e a navegação web de funcionários.
- Backups armazenados na mesma rede local que os dados de produção (suscetíveis a ransomware).

## Controles Essenciais (Rumo ao MIL 2)
- [ ] Restringir acesso físico ao servidor (rack trancado).
- [ ] Aplicação do princípio do menor privilégio para compartilhamento de pastas.
- [ ] Regra de backup 3-2-1 (3 cópias, 2 mídias diferentes, 1 cópia imutável/externa ou na nuvem).
- [ ] Desativação de serviços e protocolos obsoletos (SMBv1, Telnet, RDP aberto para a internet).
