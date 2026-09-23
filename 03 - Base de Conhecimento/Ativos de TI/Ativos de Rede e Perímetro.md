---
aliases: [Rede, Roteadores, Firewalls, Wi-Fi]
tags: [ativos, rede, perimetro]
---

# Ativos: Rede e Perímetro

## Descrição no Parque (<100 ativos)
Roteadores da operadora, switches (gerenciáveis ou unmanaged), pontos de acesso Wi-Fi e roteadores de borda/firewalls.

## Vulnerabilidades Típicas
- Uso de senha padrão nos roteadores da operadora (ex: `admin/admin`).
- Rede Wi-Fi única compartilhada entre funcionários, visitantes e dispositivos pessoais.
- Portas de conexão remota (ex: porta 3389 RDP) abertas diretamente na WAN para acesso externo.
- Falta de atualização de firmware dos roteadores e pontos de acesso.

## Controles Essenciais (Rumo ao MIL 2)
- [ ] Troca obrigatória de credenciais de fábrica em todos os equipamentos de rede.
- [ ] Criação de SSID e VLAN isolada para rede de "Visitantes" (Guest), sem acesso à rede interna.
- [ ] Proibição expressa de portas RDP/SSH expostas diretamente para a internet (uso obrigatório de VPN).
- [ ] Atualização periódica de firmware dos equipamentos de rede.
