# Templates de Políticas – Módulo de Compliance (OED 2)

> **Destinatário:** PMEs com até 100 ativos. Todos os campos marcados com `{{...}}` devem ser substituídos pelo usuário antes da adoção.

---

## Template 1 – Política de Segurança da Informação (PSI) – Geral

*Mapeamento:* NIST GV – Governança / ISO 27001 cláusula 5.2 e ISO 27002 controle 5.1 / LGPD Artigos 46 e 50.

```
Objetivo
--------
Garantir a confidencialidade, integridade e disponibilidade da informação da {{NOME_EMPRESA}}, atendendo aos requisitos da ISO 27001, ISO 27002 e LGPD.

Escopo
------
Aplica‑se a todos os ativos de informação cadastrados (≤ 100 ativos) sob responsabilidade da {{NOME_EMPRESA}}.

Diretrizes Gerais
-----------------
1. Todos os colaboradores devem tratar a informação com base no princípio do **necessário saber**.
2. Dados sensíveis devem ser classificados e protegidos conforme as categorias definidas.
3. O uso de dispositivos pessoais (BYOD) é permitido apenas mediante aprovação da {{RESPONSAVEL_SI}}.

Uso Aceitável de Ativos
------------------------
- Dispositivos de acesso à rede corporativa devem ser configurados com senhas fortes e criptografia de disco.
- É proibido instalar softwares não licenciados ou não autorizados.

Papéis e Responsabilidades
--------------------------
- **{{RESPONSAVEL_SI}}** – responsável pela gestão da segurança da informação, revisão de políticas e aprovação de incidentes.
- **Colaboradores** – seguir as diretrizes, relatar incidentes e participar de treinamentos periódicos.
- **Gestor de TI** – garantir a disponibilidade dos recursos de segurança (firewalls, antivírus, etc.).

Gestão de Incidentes
----------------------
1. Reportar imediatamente ao {{RESPONSAVEL_SI}} qualquer evento que possa comprometer a segurança.
2. Registro de incidente no sistema interno com prazo máximo de 48 h para início de investigação.
3. Plano de resposta inclui contenção, erradicação, recuperação e lições aprendidas.

Conformidade LGPD
-----------------
- Tratamento de dados pessoais deve observar os princípios da LGPD (finalidade, adequação, necessidade, transparência, segurança, prevenção e não‑discriminação).
- O {{RESPONSAVEL_SI}} deve manter registro de consentimentos e bases legais.

Sanções
-------
Descumprimentos podem acarretar advertência, suspensão de acesso ou desligamento, conforme a gravidade.

Ciclo de Revisão
---------------
- **Vigência:** {{DATA_VIGENCIA}}
- **Periodicidade de Revisão:** {{PERIODICIDADE_REVISAO}} meses
- Revisão conduzida pelo {{RESPONSAVEL_SI}} com aprovação da diretoria.
```

---

## Template 2 – Política de Controle de Acesso e Gestão de Credenciais

*Mapeamento:* NIST PR – Proteção / ISO 27002 controles 5.15‑5.18, 8.2, 8.3, 8.5.

```
Objetivo
--------
Estabelecer diretrizes para controle de acesso e gestão de credenciais que garantam o princípio do menor privilégio e a proteção contra acesso não autorizado.

Escopo
------
Aplica‑se a todos os usuários, contas de serviço e dispositivos que acessem recursos de {{NOME_EMPRESA}} (≤ 100 ativos).

Princípio do Menor Privilégio
-----------------------------
- Cada usuário recebe apenas os direitos necessários para desempenhar suas funções.
- Revisões trimestrais de permissões são realizadas pelo {{RESPONSAVEL_SI}}.

Ciclo de Vida de Contas
------------------------
1. **Admissão:** Criação de conta após aprovação da área de RH e associação ao perfil de acesso.
2. **Modificação:** Alterações de função geram revisão automática das permissões.
3. **Demissão:** Desativação imediata da conta no dia útil seguinte à saída.

Requisitos de Senha Forte
--------------------------
- Mínimo de 12 caracteres, contendo letras maiúsculas, minúsculas, números e símbolos.
- Troca obrigatória a cada 90 dias.
- Armazenamento em cofre de senhas criptografado.

MFA – Autenticação Multifator
------------------------------
- Obrigatória para acesso a ambientes críticos (nuvem, e‑mail corporativo, VPN, contas de administrador).
- Métodos aceitos: aplicativo autenticador, token hardware ou biometria.

Acesso Remoto e Dispositivos Móveis
-----------------------------------
- Conexões remotas devem ser realizadas via VPN com criptografia AES‑256.
- Dispositivos móveis devem ter senha de bloqueio e criptografia de armazenamento.

Monitoramento e Auditoria
-------------------------
- Logs de acesso são armazenados por no mínimo 180 dias.
- Revisões de logs são realizadas mensalmente pelo {{RESPONSAVEL_SI}}.

Ciclo de Revisão
---------------
- **Vigência:** {{DATA_VIGENCIA}}
- **Periodicidade de Revisão:** {{PERIODICIDADE_REVISAO}} meses
- Atualizações aprovadas pelo responsável de segurança.
```

---

## Template 3 – Política de Backup e Recuperação de Dados (Regra 3‑2‑1)

*Mapeamento:* NIST RC – Recuperação / NIST PR.DS – Proteção de Dados e ISO 27002 controle 8.13.

```
Objetivo
--------
Definir processos de backup e recuperação que garantam a disponibilidade e integridade dos dados críticos da {{NOME_EMPRESA}}.

Escopo
------
Abrange todos os dados classificados como críticos (financeiros, operacionais e de clientes) armazenados nos sistemas da empresa (≤ 100 ativos).

Regra 3‑2‑1
-----------
- **3 cópias** de cada conjunto de dados críticos.
- **2 mídias** diferentes (ex.: disco local + armazenamento em nuvem).
- **1 cópia off‑site** em repositório imutável (S3 Glacier, Azure Blob Immutable) para proteção contra ransomware.

Periodicidade de Backups
-----------------------
- **Diário:** Dados de transação e bases de dados operacionais.
- **Semanal:** Sistemas de arquivos de usuários.
- **Mensal:** Arquivos de longo prazo e logs de auditoria.

Criptografia de Backups
------------------------
- Todos os backups são criptografados com AES‑256 utilizando chaves gerenciadas pelo {{RESPONSAVEL_SI}}.

Testes de Restauração
----------------------
- Testes de restauração são realizados **trimestralmente** para validar a integridade dos backups.
- Resultados documentados em relatório de teste (necessário para atingir MIL 2).

Objetivos de Recuperação (RTO / RPO)
-----------------------------------
- **RTO (Recovery Time Objective):** Máximo de 4 horas para sistemas críticos.
- **RPO (Recovery Point Objective):** Máximo de 1 hora para bases de dados transacionais.

Responsabilidades
----------------
- **{{RESPONSAVEL_SI}}** – define políticas, supervisiona execuções e valida testes.
- **Equipe de TI** – executa backups conforme cronograma e mantém mídia off‑site.

Ciclo de Revisão
---------------
- **Vigência:** {{DATA_VIGENCIA}}
- **Periodicidade de Revisão:** {{PERIODICIDADE_REVISAO}} meses
- Revisões incluem avaliação de novos requisitos regulatórios e de tecnologia.
```

---

*Este documento foi elaborado com base nas notas da ISO 27001/27002, no NIST CSF 2.0 e nas diretrizes de LGPD, adaptado para a realidade de PMEs com até 100 ativos.*
