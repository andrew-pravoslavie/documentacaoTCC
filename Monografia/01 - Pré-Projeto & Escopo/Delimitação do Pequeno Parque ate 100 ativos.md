---
aliases: [Pequeno Parque de TI, Delimitação do Parque, 100 dispositivos]
tags: [escopo, infraestrutura, parque-ti]
---

# Delimitação do Pequeno Parque Tecnológico

## Critério de Porte Adotado
Adota-se como unidade de análise o **parque de equipamentos de tecnologia da informação**, em substituição a critérios exclusivamente econômicos (como faturamento anual ou número total de colaboradores registrados na CLT).

> [!important] Por que delimitar por volume de ativos?
> São a **dimensão física e a composição técnica do parque** — e não o faturamento contábil da empresa — que determinam:
> 1. A **superfície de ataque exposta** aos criminosos cibernéticos;
> 2. O **esforço técnico exigido** para configurar, monitorar e proteger os ativos;
> 3. A **viabilidade prática e operacional** de implantação dos controles de segurança.

---

## As 4 Categorias de Ativos Gerenciados (Até 100 dispositivos)

```
                              ┌─────────────────────────────────────┐
                              │  Pequeno Parque de TI (≤ 100 ativos) │
                              └──────────────────┬──────────────────┘
                 ┌───────────────────┬───────────┴───────────┬───────────────────┐
                 ▼                   ▼                       ▼                   ▼
        ┌─────────────────┐ ┌─────────────────┐     ┌─────────────────┐ ┌─────────────────┐
        │ 1. Endpoints    │ │ 2. Servidores   │     │ 3. Ativos de    │ │ 4. Mobile &     │
        │    Corporativos │ │    Locais & NAS │     │    Rede         │ │    Nuvem (SaaS) │
        └─────────────────┘ └─────────────────┘     └─────────────────┘ └─────────────────┘
```

1. **[[Ativos de TI/Endpoints e Estações de Trabalho|Endpoints Corporativos]]:**
   - Estações de trabalho (desktops) e notebooks usados pelos colaboradores.
   - Periféricos conectados à rede corporativa com capacidade de processamento/armazenamento.

2. **[[Ativos de TI/Servidores Locais e Armazenamento|Servidores Locais & Storage]]:**
   - Servidores de arquivos (File Servers), controladores de domínio (Active Directory / LDAP).
   - Sistemas de armazenamento acoplados em rede (NAS) e servidores de aplicação interna.

3. **[[Ativos de TI/Ativos de Rede e Perímetro|Ativos de Rede e Perímetro]]:**
   - Roteadores, switches gerenciáveis e não-gerenciáveis.
   - Roteadores de borda/gateways, firewalls (hardware ou appliances dedicados).
   - Pontos de acesso sem fio (Access Points Wi-Fi corporativos e de visitantes).

4. **[[Ativos de TI/Dispositivos Móveis e Serviços em Nuvem|Dispositivos Móveis Corporativos & Serviços em Nuvem]]:**
   - Smartphones e tablets corporativos (ou BYOD com acesso a dados corporativos).
   - Serviços em nuvem contratados essenciais: correio eletrônico corporativo (Google Workspace, Microsoft 365), armazenamento em nuvem (OneDrive, Google Drive), ERPs e CRMs SaaS.
