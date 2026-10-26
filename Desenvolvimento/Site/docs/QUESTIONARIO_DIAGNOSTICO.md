# Instrumento de Diagnóstico de Cibersegurança e Maturidade — SecureDesk

> [!NOTE]
> **Projeto:** SecureDesk — Framework Simplificado de Cibersegurança e Maturidade (NIST CSF 2.0 × C2M2) para Pequenos Parques Tecnológicos (≤ 100 ativos)  
> **Curso:** Tecnologia em Análise e Desenvolvimento de Sistemas — FATEC Zona Leste (2026–2027)  
> **Artefato do TG1:** Item 1.3 do `PLANO_DESENVOLVIMENTO.md` / Entregável OED 3 (Questionário de Onboarding e Diagnóstico)  
> **Alinhamento:** NIST CSF 2.0 (Funções GV, ID, PR, DE, RS, RC) × C2M2 v2.1 (MIL 0 a MIL 3, Alvo MIL 2) × ABNT NBR ISO/IEC 27001 e 27002:2022  
> **Versão:** 1.0 — Consolidada para 30 questões fechadas (5 por função)

---

## 1. Metodologia e Estrutura do Instrumento

O **Instrumento de Diagnóstico do SecureDesk** foi projetado para avaliar com precisão a postura e o nível de maturidade cibernética de Pequenas e Médias Empresas (PMEs) que gerenciam parques computacionais de pequeno porte (até 100 dispositivos).

### 1.1 Características Estruturais
- **Extensão Balanceada:** 30 questões fechadas no total, distribuídas rigorosamente em **5 questões para cada uma das 6 funções do NIST CSF 2.0** (`GV`, `ID`, `PR`, `DE`, `RS`, `RC`).
- **Escala de Maturidade C2M2 (Maturity Indicator Level):** Para cada questão, são fornecidas **4 alternativas graduadas e mutuamente exclusivas**, mapeando diretamente a escala de capacidade:
  * **`MIL_0` (Inexistente / Não Realizado — peso 0.0):** Ausência completa da prática; processo inexistente ou descontrolado.
  * **`MIL_1` (Inicial / Ad-hoc — peso 1.0):** Prática informal, executada esporadicamente dependendo de iniciativa individual, sem registro ou padronização.
  * **`MIL_2` (Padronizado / Atribuído — peso 2.0 — *Patamar Alvo Recomendado*):** Prática documentada, formalizada, atribuída a responsáveis claros e executada de maneira consistente em toda a PME.
  * **`MIL_3` (Otimizado / Institucionalizado — peso 3.0):** Prática avançada com automação contínua, governança métrica e monitoramento estatístico.
- **Rastreabilidade Dupla:** Cada questão conecta-se a uma **subcategoria primária do NIST CSF 2.0** e a controles correspondentes da norma **ABNT NBR ISO/IEC 27002:2022**, servindo de base de evidência formal para a Monografia (OEM 5 e OED 3).
- **Geração Determinística de Gaps:** Sempre que a alternativa selecionada for inferior a `MIL_2` (`MIL_0` ou `MIL_1`), o sistema gera automaticamente uma vulnerabilidade na tabela `tb_gap`, parametrizada por `NivelImpacto`, `CustoMitigacao`, cálculo de `Prioridade` e texto objetivo de `Recomendacao`.

---

## 2. Questionário Detalhado por Função

```
                      ┌──────────────────────────────────────────────┐
                      │    QUESTIONÁRIO SECUREDESK (30 QUESTÕES)     │
                      └──────────────────────┬───────────────────────┘
          ┌───────────────┬──────────────────┼──────────────────┬───────────────┐
          ▼               ▼                  ▼                  ▼               ▼
     1. GOVERNAR    2. IDENTIFICAR     3. PROTEGER        4. DETECTAR     5. RESPONDER / 6. RECUPERAR
      (GV-01..05)    (ID-01..05)        (PR-01..05)        (DE-01..05)     (RS-01..05 / RC-01..05)
```

---

### Função 1: GOVERNAR (`GV`) — Governança, Contexto Organizacional e Gestão de Terceiros

#### `GV-01` — Política de Segurança da Informação (PSI) e Diretrizes de Uso Aceitável
- **Enunciado:** A sua empresa possui diretrizes ou normas escritas que orientam os colaboradores sobre o que é permitido e proibido no uso dos computadores, e-mails e internet da organização?
- **Explicação de Ajuda (`explicacao_ajuda`):** A Política de Segurança da Informação (PSI) define regras claras sobre senhas, uso de pendrives, navegação na internet e proibição de instalação de programas não autorizados. Ter isso por escrito evita dúvidas e confere respaldo legal ao negócio.
- **Referência Normativa:** NIST CSF 2.0: `GV.PO-01` | ABNT NBR ISO/IEC 27002:2022: Controle `5.1` (Políticas para segurança da informação).
- **Categorias de Ativos Relacionadas:** Organizacional / Todos os Ativos.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Não temos nenhuma regra, manual ou orientação escrita sobre o uso de computadores, internet ou dados da empresa.
  * **`MIL_1` (Inicial / Ad-hoc):** Repassamos orientações verbais básicas ("não abra links estranhos", "crie senhas boas"), mas não há documento formalizado ou assinado.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Possuímos uma Política de Segurança da Informação (PSI) simplificada e escrita, assinada por todos os colaboradores na admissão (termo de ciência e compromisso).
  * **`MIL_3` (Otimizado):** A PSI é revisada formalmente todo ano pela direção, possui auditorias periódicas de conformidade e penalidades disciplinares regulamentadas.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Adotar o modelo base de Política de Segurança da Informação (PSI) do SecureDesk, adaptar às regras da empresa e coletar a assinatura de ciência de todos os funcionários.

---

#### `GV-02` — Atribuição Formal de Papéis e Responsabilidade por Cibersegurança
- **Enunciado:** Existe uma pessoa ou fornecedor formalmente designado pela diretoria como responsável por cuidar da segurança dos computadores, servidores e rede da empresa?
- **Explicação de Ajuda (`explicacao_ajuda`):** Sem uma pessoa encarregada formalmente, a manutenção de TI fica sem dono e ninguém assume a responsabilidade quando surgem falhas graves de segurança.
- **Referência Normativa:** NIST CSF 2.0: `GV.RR-01` | ABNT NBR ISO/IEC 27002:2022: Controle `5.2` (Papéis e responsabilidades na segurança da informação).
- **Categorias de Ativos Relacionadas:** Organizacional / Todos os Ativos.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Ninguém na empresa é formalmente responsável pela segurança da tecnologia; problemas são resolvidos por qualquer colaborador que entenda um pouco de informática.
  * **`MIL_1` (Inicial / Ad-hoc):** Um colaborador ou técnico externo atende chamados quando algo quebra, mas não tem atribuição formal de zelar proativamente pela segurança cibernética.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Há um profissional de TI interno ou uma empresa parceira de suporte gerenciado (MSP) formalmente encarregada pela gestão e segurança de todo o parque tecnológico.
  * **`MIL_3` (Otimizado):** Papéis e alçadas de decisão são documentados na estrutura da empresa, com reuniões periódicas e apresentação de relatórios de risco cibernético para a diretoria.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Formalizar a nomeação do responsável por TI e segurança (interno ou prestador terceirizado), registrando suas responsabilidades de inspeção preventiva, atualizações e resposta a falhas.

---

#### `GV-03` — Gestão de Riscos e Contratos com Prestadores de TI e Nuvem
- **Enunciado:** Os contratos com técnicos externos, desenvolvedores e fornecedores de serviços em nuvem possuem cláusulas de confidencialidade (NDA) e regras de segurança?
- **Explicação de Ajuda (`explicacao_ajuda`):** Muitos vazamentos de dados ocorrem através de prestadores terceirizados que têm acessos privilegiados mas não assinam acordos de confidencialidade nem se comprometem com a proteção dos dados.
- **Referência Normativa:** NIST CSF 2.0: `GV.SC-04` | ABNT NBR ISO/IEC 27002:2022: Controles `5.19` (Segurança da informação nas relações com fornecedores) e `5.20` (Abordagem da segurança da informação em contratos com fornecedores).
- **Categorias de Ativos Relacionadas:** Servidores Locais, Ativos de Rede, Mobile/Nuvem.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Compartilhamos senhas de servidores e roteadores com técnicos e prestadores externos sem nenhum contrato, assinatura de termo de sigilo ou controle de prazo.
  * **`MIL_1` (Inicial / Ad-hoc):** Temos contratos de prestação de serviços convencionais, mas sem cláusulas explícitas sobre segurança cibernética, guarda de senhas ou responsabilidade por vazamento de dados.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Todos os prestadores de TI e fornecedores de software assinam termo de confidencialidade (NDA) e termo de compromisso com a LGPD, utilizando credenciais temporárias ou individuais.
  * **`MIL_3` (Otimizado):** Mantemos processo contínuo de auditoria e qualificação técnica de fornecedores, exigindo certificações de conformidade (como SOC 2 ou ISO 27001) para serviços críticos.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `MEDIO`
  * `Prioridade`: `ALTA`
  * `Recomendacao`: Aditivar os contratos de fornecedores e técnicos de TI externos, inserindo cláusula de confidencialidade (NDA), dever de sigilo, proibição de repasse de senhas e conformidade expressa com a LGPD.

---

#### `GV-04` — Conscientização e Treinamento de Colaboradores em Ameaças Cibernéticas
- **Enunciado:** Os colaboradores da empresa recebem treinamento prático sobre como identificar golpes virtuais, e-mails falsos (phishing) e criação de senhas fortes?
- **Explicação de Ajuda (`explicacao_ajuda`):** O fator humano é a porta de entrada mais explorada por criminosos cibernéticos. Um único clique em anexo falso pode comprometer toda a rede da empresa com vírus de resgate (ransomware).
- **Referência Normativa:** NIST CSF 2.0: `PR.AT-01` / `GV.PO-02` | ABNT NBR ISO/IEC 27002:2022: Controle `6.3` (Conscientização, educação e treinamento em segurança da informação).
- **Categorias de Ativos Relacionadas:** Endpoints, Mobile/Nuvem, Organizacional.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Nunca realizamos nenhum tipo de treinamento, aviso ou palestra de segurança da informação para nossos funcionários.
  * **`MIL_1` (Inicial / Ad-hoc):** Avisamos os funcionários informalmente apenas quando alguém quase cai em um golpe ou quando surge um ataque na imprensa.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Realizamos treinamento básico de conscientização em cibersegurança na admissão de todo novo colaborador e reciclagem anual com cartilha prática de boas práticas.
  * **`MIL_3` (Otimizado):** Programa contínuo de conscientização que inclui campanhas mensais e simulações regulares de phishing para medir a evolução da taxa de cliques da equipe.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Promover uma sessão semestral de 30 minutos de conscientização contra engenharia social e distribuir cartilha ilustrada abordando phishing, boletos falsos e cuidados com pendrives.

---

#### `GV-05` — Conformidade com a LGPD e Proteção de Dados Pessoais
- **Enunciado:** A empresa possui conhecimento e controle dos dados pessoais de clientes e colaboradores que armazena, atendendo aos requisitos mínimos da LGPD?
- **Explicação de Ajuda (`explicacao_ajuda`):** A Lei Geral de Proteção de Dados (Lei 13.709/2018) exige que as empresas saibam quais dados pessoais coletam, onde ficam guardados, com quem compartilham e que adotem medidas para evitar vazamentos.
- **Referência Normativa:** NIST CSF 2.0: `GV.OC-03` | ABNT NBR ISO/IEC 27002:2022: Controles `5.31` (Requisitos legais, regulamentares e contratuais) e `5.34` (Privacidade e proteção de dados pessoais).
- **Categorias de Ativos Relacionadas:** Organizacional / Todos os Ativos.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Não sabemos quais dados pessoais a empresa armazena nem conhecemos as exigências da LGPD aplicáveis ao nosso negócio.
  * **`MIL_1` (Inicial / Ad-hoc):** Baixamos um modelo genérico de termo de privacidade na internet, mas não mapeamos onde os dados residem nem nomeamos um ponto de contato.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Possuímos levantamento simplificado dos dados pessoais coletados (RH, financeiro, clientes), termos de consentimento/privacidade formais e responsável indicado para solicitações de titulares.
  * **`MIL_3` (Otimizado):** Registro das Operações de Tratamento (ROPA) mantido atualizado, avaliação formal de impacto (RIPD/DPIA) para novos projetos e auditoria jurídica contínua.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `MEDIO`
  * `Prioridade`: `ALTA`
  * `Recomendacao`: Elaborar o inventário simplificado de dados pessoais tratados na PME, publicar o Aviso de Privacidade no site/contratos e utilizar o registro de consentimento LGPD integrado do SecureDesk.

---

### Função 2: IDENTIFICAR (`ID`) — Inventário de Ativos, Softwares, Dados e Riscos

#### `ID-01` — Inventário Centralizado de Hardware e Dispositivos Tecnológicos
- **Enunciado:** A sua empresa mantém uma lista ou planilha atualizada com todos os computadores, servidores, roteadores e impressoras da rede?
- **Explicação de Ajuda (`explicacao_ajuda`):** Não é possível proteger aquilo que você não sabe que existe. Manter o controle do pequeno parque tecnológico (≤ 100 ativos) é o primeiro passo para garantir a segurança dos equipamentos.
- **Referência Normativa:** NIST CSF 2.0: `ID.AM-01` | ABNT NBR ISO/IEC 27002:2022: Controle `5.9` (Inventário de informações e outros ativos associados).
- **Categorias de Ativos Relacionadas:** Endpoints, Servidores Locais, Ativos de Rede.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Não sabemos quantos computadores ou equipamentos de rede existem na empresa; não há nenhuma listagem.
  * **`MIL_1` (Inicial / Ad-hoc):** Temos conhecimento visual ou anotações antigas e incompletas sobre algumas máquinas da equipe.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Mantemos um inventário atualizado (em planilha ou no módulo de Ativos do SecureDesk) contendo todos os dispositivos gerenciados (hostname, tipo, usuário responsável e localização física).
  * **`MIL_3` (Otimizado):** Inventário dinâmico automatizado por ferramenta de descoberta em tempo real, que gera alertas imediatos sempre que um novo dispositivo desconhecido se conecta à rede.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Realizar levantamento físico dos equipamentos da empresa e cadastrar todos os dispositivos no módulo de Inventário de Ativos do SecureDesk, mantendo a lista atualizada a cada admissão ou compra.

---

#### `ID-02` — Inventário e Homologação de Softwares Instalados
- **Enunciado:** A empresa possui uma lista de programas e aplicativos homologados e autorizados para uso nos computadores de trabalho?
- **Explicação de Ajuda (`explicacao_ajuda`):** Softwares pirateados, jogos e utilitários não verificados frequentemente contêm cavalos de troia que abrem portas de acesso para invasores.
- **Referência Normativa:** NIST CSF 2.0: `ID.AM-02` | ABNT NBR ISO/IEC 27002:2022: Controles `5.9` (Inventário de ativos) e `8.19` (Instalação de software em sistemas operacionais).
- **Categorias de Ativos Relacionadas:** Endpoints, Servidores Locais.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Qualquer colaborador tem liberdade para baixar e instalar qualquer programa gratuito ou pirata na sua máquina de trabalho.
  * **`MIL_1` (Inicial / Ad-hoc):** Pedimos aos colaboradores que não instalem programas estranhos, mas não verificamos o que está instalado nas máquinas.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Existe uma lista formal de softwares permitidos e homologados para a empresa, e os usuários não possuem direitos administrativos para instalar programas por conta própria.
  * **`MIL_3` (Otimizado):** Ferramenta centralizada de gerenciamento que bloqueia a execução de qualquer software que não esteja na lista branca (application whitelisting) e controla o licenciamento.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `MEDIO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `ALTA` *(Quick Win imediato)*
  * `Recomendacao`: Definir o pacote padrão de softwares homologados por setor (ex.: pacote Office, navegador, ERP), remover programas desnecessários e revogar permissões de instalação dos usuários.

---

#### `ID-03` — Mapeamento de Serviços e Recursos em Nuvem (Shadow IT)
- **Enunciado:** Todos os serviços na nuvem (e-mails, Google Drive, OneDrive, sistemas web, ERP) utilizados na empresa são de contas corporativas controladas pela organização?
- **Explicação de Ajuda (`explicacao_ajuda`):** Quando funcionários usam contas de e-mail ou nuvem pessoais para trabalhar (Shadow IT), a empresa perde o controle dos dados e corre sério risco de vazamento quando o colaborador se desliga.
- **Referência Normativa:** NIST CSF 2.0: `ID.AM-03` | ABNT NBR ISO/IEC 27002:2022: Controle `5.23` (Segurança da informação para uso de serviços em nuvem).
- **Categorias de Ativos Relacionadas:** Mobile & Nuvem (SaaS).
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Cada funcionário usa seu e-mail pessoal ou contas particulares na nuvem (Dropbox, Google Drive particular) para guardar arquivos do trabalho.
  * **`MIL_1` (Inicial / Ad-hoc):** Temos um domínio de e-mail próprio, mas muitos documentos da empresa continuam salvos em contas pessoais de funcionários sem controle da diretoria.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Mantemos catálogo formal de todos os serviços em nuvem contratados sob contas corporativas centralizadas, proibindo expressamente o uso de contas pessoais para armazenar dados da empresa.
  * **`MIL_3` (Otimizado):** Gestão centralizada de nuvem com logon único corporativo (SSO), bloqueio de serviços não autorizados no firewall e auditoria contínua de movimentação de dados.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Cadastrar e gerenciar todos os e-mails e armazenamentos em nuvem sob contas corporativas controladas pelo administrador, revogando o uso de armazenamentos pessoais.

---

#### `ID-04` — Localização e Classificação de Dados Confidenciais e Críticos
- **Enunciado:** A empresa sabe exatamente em quais servidores, pastas ou sistemas estão armazenadas as informações mais valiosas e confidenciais do negócio?
- **Explicação de Ajuda (`explicacao_ajuda`):** Saber onde ficam os dados mais críticos (folha de pagamento, contratos, segredos de negócio, dados bancários) permite aplicar regras rígidas de segurança exatamente onde é mais necessário.
- **Referência Normativa:** NIST CSF 2.0: `ID.AM-05` | ABNT NBR ISO/IEC 27002:2022: Controles `5.12` (Classificação da informação) e `5.13` (Rotulagem da informação).
- **Categorias de Ativos Relacionadas:** Servidores Locais, Mobile/Nuvem.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Arquivos confidenciais ficam misturados com documentos comuns em pastas compartilhadas abertas para qualquer pessoa da empresa acessar.
  * **`MIL_1` (Inicial / Ad-hoc):** Alguns gestores colocam senhas em planilhas ou pastas isoladas, mas não há um mapeamento ou critério padrão de confidencialidade.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Mapeamento documentado de todos os repositórios de dados confidenciais (financeiro, RH, clientes), com controle estrito de permissões concedidas apenas a quem realmente necessita.
  * **`MIL_3` (Otimizado):** Classificação automática da informação integrada ao ambiente de arquivos (rotulagem e criptografia automática de documentos sensíveis com controle de vazamento — DLP).
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Mapear as pastas e sistemas com dados sensíveis, remover o acesso de usuários não autorizados e restringir o compartilhamento apenas aos cargos responsáveis.

---

#### `ID-05` — Avaliação Periódica de Vulnerabilidades e Riscos Técnicos
- **Enunciado:** A empresa realiza revisões periódicas para verificar se há computadores com sistemas antigos sem suporte, roteadores desatualizados ou senhas padrão de fábrica?
- **Explicação de Ajuda (`explicacao_ajuda`):** Equipamentos desatualizados (como Windows 7 ou roteadores com firmware de 5 anos atrás) possuem falhas públicas que permitem invasões automáticas em minutos.
- **Referência Normativa:** NIST CSF 2.0: `ID.RA-01` | ABNT NBR ISO/IEC 27002:2022: Controle `8.8` (Gestão de vulnerabilidades técnicas).
- **Categorias de Ativos Relacionadas:** Endpoints, Servidores Locais, Ativos de Rede.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Nunca verificamos a segurança dos equipamentos; utilizamos computadores e roteadores com configurações padrão de fábrica até que queimem.
  * **`MIL_1` (Inicial / Ad-hoc):** Verificamos se há algum problema técnico somente quando uma máquina apresenta pane ou quando um usuário reclama de vírus.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Realizamos avaliação técnica preventiva periódica (ao menos semestralmente, com checklists ou pelo SecureDesk) para identificar sistemas descontinuados e falhas evidentes de segurança.
  * **`MIL_3` (Otimizado):** Varreduras automatizadas contínuas de vulnerabilidades com softwares especializados (scanners de rede) e plano estruturado de correção com SLA formal.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `MEDIO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `ALTA` *(Quick Win imediato)*
  * `Recomendacao`: Executar o diagnóstico do SecureDesk trimestralmente e realizar checklist técnico para substituir ou atualizar sistemas sem suporte do fabricante.

---

### Função 3: PROTEGER (`PR`) — Salvaguardas, Controle de Acesso e Defesas Técnicas

#### `PR-01` — Controle de Contas, Senhas e Princípio do Menor Privilégio
- **Enunciado:** Os colaboradores usam contas individuais de usuário comum nos computadores, ou utilizam senhas compartilhadas e acessos com privilégio de Administrador?
- **Explicação de Ajuda (`explicacao_ajuda`):** Trabalhar diariamente como Administrador da máquina permite que qualquer arquivo malicioso baixado pelo navegador infecte silenciosamente todo o sistema operacional sem pedir confirmação.
- **Referência Normativa:** NIST CSF 2.0: `PR.AA-01` e `PR.AA-05` | ABNT NBR ISO/IEC 27002:2022: Controles `5.15` (Controle de acesso) e `8.2` (Direitos de acesso privilegiado).
- **Categorias de Ativos Relacionadas:** Endpoints, Servidores Locais.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Todos os computadores possuem login automático sem senha, ou todos usam a mesma senha genérica com privilégio total de Administrador da máquina.
  * **`MIL_1` (Inicial / Ad-hoc):** Cada usuário tem seu próprio login, mas todas as contas continuam configuradas como Administrador local.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Contas de usuário são individuais e nominais, configuradas com perfil de "Usuário Padrão" (sem permissão de administrador); acessos de administrador são restritos exclusivamente aos técnicos de TI.
  * **`MIL_3` (Otimizado):** Gestão centralizada de identidades (Active Directory / Azure AD) com política de senhas fortes forçada pelo sistema e cofre de senhas para credenciais privilegiadas.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Alterar as contas dos colaboradores nas estações de trabalho de "Administrador" para "Usuário Padrão" e exigir senhas individuais com pelo menos 10 caracteres.

---

#### `PR-02` — Autenticação Multifator (MFA / 2FA) em E-mails e Acessos Críticos
- **Enunciado:** O acesso aos e-mails corporativos, contas em nuvem e VPN da empresa exige obrigatoriamente um segundo fator de confirmação (MFA / código no celular)?
- **Explicação de Ajuda (`explicacao_ajuda`):** A autenticação de dois fatores bloqueia mais de 99% dos ataques de invasão de contas, pois mesmo que o invasor descubra a senha, não conseguirá entrar sem o código do celular do usuário.
- **Referência Normativa:** NIST CSF 2.0: `PR.AA-03` | ABNT NBR ISO/IEC 27002:2022: Controles `5.17` (Informações de autenticação) e `8.5` (Autenticação segura).
- **Categorias de Ativos Relacionadas:** Mobile/Nuvem, Ativos de Rede.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Todos os acessos a e-mails e sistemas da nuvem utilizam apenas usuário e senha simples, sem qualquer confirmação em segundo fator.
  * **`MIL_1` (Inicial / Ad-hoc):** Apenas os diretores ou colaboradores que quiseram ativaram o segundo fator por conta própria em suas contas individuais.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** A autenticação multifator (MFA) é obrigatória e configurada para 100% dos usuários corporativos em e-mails, acessos remotos (VPN) e sistemas de nuvem.
  * **`MIL_3` (Otimizado):** MFA avançado baseado em chaves físicas (FIDO2) ou aplicativos corporativos com regras de acesso condicional (bloqueio automático de acessos fora do país ou em dispositivos desconhecidos).
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Habilitar a política de obrigatoriedade de MFA (autenticação por aplicativo no celular) no painel de administração do Microsoft 365, Google Workspace e painéis web do negócio.

---

#### `PR-03` — Proteção de Endpoints (Antivírus Corporativo / EDR Centralizado)
- **Enunciado:** Todos os computadores, notebooks e servidores da empresa possuem uma solução corporativa de antivírus gerenciada e atualizada diariamente?
- **Explicação de Ajuda (`explicacao_ajuda`):** Ter um painel centralizado garante que o responsável técnico saiba em tempo real se alguma máquina ficou desprotegida ou se um vírus foi bloqueado na rede.
- **Referência Normativa:** NIST CSF 2.0: `PR.PS-01` | ABNT NBR ISO/IEC 27002:2022: Controle `8.7` (Proteção contra malware).
- **Categorias de Ativos Relacionadas:** Endpoints, Servidores Locais.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Nossas máquinas não têm antivírus ou usam versões antigas gratuitas cuja licença expirou há muito tempo.
  * **`MIL_1` (Inicial / Ad-hoc):** Cada computador tem um antivírus gratuito instalado pelo próprio usuário, sem gerenciamento centralizado ou garantia de atualização.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** 100% dos computadores e servidores possuem antivírus corporativo gerenciado centralmente por console em nuvem, com proteção em tempo real e atualizações automáticas diárias.
  * **`MIL_3` (Otimizado):** Solução avançada de EDR (Endpoint Detection and Response) com inteligência artificial para detecção comportamental de ataques e contenção automática de máquinas suspeitas.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `MEDIO`
  * `Prioridade`: `ALTA`
  * `Recomendacao`: Contratar e instalar uma solução de antivírus corporativo com console centralizada na nuvem, garantindo cobertura de todas as estações e servidores do parque.

---

#### `PR-04` — Gestão Regular de Atualizações e Patches de Segurança (SO e Softwares)
- **Enunciado:** O sistema operacional e programas dos computadores são mantidos sempre atualizados com as correções de segurança mais recentes?
- **Explicação de Ajuda (`explicacao_ajuda`):** A imensa maioria dos ataques de ransomware explora falhas já conhecidas há meses pelo fabricante, que já possuíam correções gratuitas prontas para instalação.
- **Referência Normativa:** NIST CSF 2.0: `PR.PS-02` | ABNT NBR ISO/IEC 27002:2022: Controles `8.8` (Gestão de vulnerabilidades técnicas) e `8.19` (Instalação de software em sistemas operacionais).
- **Categorias de Ativos Relacionadas:** Endpoints, Servidores Locais, Ativos de Rede.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** O Windows Update e atualizações de programas estão permanentemente desativados para evitar reinicializações nos computadores.
  * **`MIL_1` (Inicial / Ad-hoc):** As atualizações são aplicadas apenas quando o usuário aceita voluntariamente os avisos na tela ou quando o computador começa a travar.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Atualizações automáticas de segurança ativadas e configuradas para aplicação semanal ou mensal obrigatória fora do horário de expediente em todas as estações e servidores.
  * **`MIL_3` (Otimizado):** Sistema centralizado de gerenciamento de patches (RMM) que testa previamente as atualizações, audita o nível de conformidade de cada máquina e força o reinício programado.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Configurar o Windows Update e softwares essenciais para baixar e instalar atualizações de segurança automaticamente em horários programados.

---

#### `PR-05` — Proteção de Rede, Firewall e Separação de Wi-Fi de Visitantes
- **Enunciado:** A rede da empresa é protegida por firewall e a rede Wi-Fi dos visitantes/celulares pessoais é separada da rede dos computadores e servidores de trabalho?
- **Explicação de Ajuda (`explicacao_ajuda`):** Deixar clientes ou colaboradores usarem a mesma rede Wi-Fi onde estão os servidores e arquivos da empresa permite que qualquer celular infectado transmita malwares diretamente para os computadores corporativos.
- **Referência Normativa:** NIST CSF 2.0: `PR.IR-01` | ABNT NBR ISO/IEC 27002:2022: Controles `8.20` (Segurança de redes), `8.22` (Segregação de redes) e `8.23` (Segurança dos serviços de rede).
- **Categorias de Ativos Relacionadas:** Ativos de Rede.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Usamos um roteador caseiro com a senha padrão de fábrica ("admin/admin") e todos (clientes, celulares e servidores) compartilham a mesmíssima rede sem senha ou com senha pública.
  * **`MIL_1` (Inicial / Ad-hoc):** Alteramos a senha do roteador, mas os celulares pessoais e os computadores da empresa continuam conectados na mesma rede sem separação.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** O roteador/firewall tem senhas administrativas fortes e a rede Wi-Fi possui divisão clara: uma rede interna protegida para as máquinas da empresa e uma rede exclusiva de "Visitantes" sem acesso aos servidores.
  * **`MIL_3` (Otimizado):** Rede corporativa dividida em VLANs dedicadas (servidores, estações, VoIP, IoT) protegidas por firewall de última geração (NGFW) com inspeção profunda de pacotes.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Criar uma rede Wi-Fi isolada para visitantes e celulares pessoais no roteador corporativo, alterando imediatamente a senha de fábrica do equipamento.

---

### Função 4: DETECTAR (`DE`) — Monitoramento, Anomalias e Visibilidade de Incidentes

#### `DE-01` — Alertas Automáticos de Ameaças e Infecções em Tempo Real
- **Enunciado:** A equipe responsável pela TI recebe avisos ou alertas imediatos no e-mail/celular quando um vírus, malware ou ataque é bloqueado em alguma máquina da empresa?
- **Explicação de Ajuda (`explicacao_ajuda`):** Saber de uma tentativa de invasão no momento exato em que ela ocorre permite agir rapidamente antes que o ataque se espalhe para o restante da empresa.
- **Referência Normativa:** NIST CSF 2.0: `DE.CM-01` | ABNT NBR ISO/IEC 27002:2022: Controle `8.16` (Monitoramento de atividades).
- **Categorias de Ativos Relacionadas:** Endpoints, Servidores Locais.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Ninguém recebe avisos; se uma máquina for invadida ou infectada, a empresa só descobre quando as operações travam ou arquivos são sequestrados.
  * **`MIL_1` (Inicial / Ad-hoc):** O aviso de infecção aparece apenas na tela do colaborador que estava usando o computador na hora.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** O sistema de antivírus/proteção está configurado para enviar alertas automáticos por e-mail ao administrador de TI sempre que uma ameaça for identificada ou bloqueada.
  * **`MIL_3` (Otimizado):** Monitoramento contínuo integrado a um centro de operações de segurança (SOC gerenciado ou SIEM) que analisa severidade e correlação de alertas em tempo real.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Acessar o console do antivírus corporativo e cadastrar o e-mail do responsável de TI para receber alertas imediatos de detecção de malwares graves.

---

#### `DE-02` — Auditoria e Monitoramento de Conexões Externas e Tentativas de Invasão
- **Enunciado:** O roteador ou firewall da empresa registra e monitora as tentativas de conexão externa vindas da internet (tentativas de login indevidas)?
- **Explicação de Ajuda (`explicacao_ajuda`):** Criminosos utilizam robôs para testar milhões de senhas em portas de roteadores abertas na internet. Monitorar e bloquear tentativas repetidas de senha impede o arrombamento virtual.
- **Referência Normativa:** NIST CSF 2.0: `DE.AE-02` | ABNT NBR ISO/IEC 27002:2022: Controles `8.20` (Segurança de redes) e `8.21` (Segurança dos serviços de rede).
- **Categorias de Ativos Relacionadas:** Ativos de Rede, Servidores Locais.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Não temos registros e desconhecemos quem tenta se conectar aos equipamentos de rede da empresa pela internet.
  * **`MIL_1` (Inicial / Ad-hoc):** Olhamos os registros de conexão do roteador apenas quando a internet fica muito lenta ou quando o equipamento trava.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** O roteador/firewall mantém registro de conexões externas e está configurado para bloquear automaticamente IPs após sucessivas tentativas incorretas de senha (proteção contra força bruta).
  * **`MIL_3` (Otimizado):** Análise contínua de tráfego de rede com sistema de prevenção de intrusão (IPS) e bloqueio geográfico de conexões provenientes de países sem relação comercial com a empresa.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `MEDIO`
  * `CustoMitigacao`: `MEDIO`
  * `Prioridade`: `MEDIA`
  * `Recomendacao`: Fechar portas desnecessárias expostas na internet (ex.: RDP 3389) e ativar a proteção de bloqueio automático por excesso de tentativas no firewall.

---

#### `DE-03` — Retenção de Logs de Eventos Críticos para Auditoria
- **Enunciado:** A empresa guarda registros históricos (logs) de quem acessou os servidores, pastas de arquivos e contas da nuvem por pelo menos 6 meses?
- **Explicação de Ajuda (`explicacao_ajuda`):** O Marco Civil da Internet (Lei 12.965/2014) e a LGPD exigem que registros de conexão e acesso a dados sejam guardados para permitir a apuração legal de incidentes e invasões.
- **Referência Normativa:** NIST CSF 2.0: `DE.CM-03` | ABNT NBR ISO/IEC 27002:2022: Controle `8.15` (Registro de eventos - Logging).
- **Categorias de Ativos Relacionadas:** Servidores Locais, Mobile/Nuvem.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Os registros de acessos do sistema estão desativados ou são apagados diariamente.
  * **`MIL_1` (Inicial / Ad-hoc):** Os registros ficam armazenados apenas nas máquinas locais, sem garantia de tempo de retenção e podendo ser apagados por qualquer usuário.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Os logs de logins de administradores, acessos a arquivos críticos e sistemas em nuvem são retidos e protegidos por pelo menos 6 meses.
  * **`MIL_3` (Otimizado):** Centralização automática de logs em servidor externo ou repositório em nuvem protegido contra alterações (armazenamento imutável / WORM) com auditoria de integridade.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `MEDIO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `ALTA` *(Quick Win imediato)*
  * `Recomendacao`: Expandir o tempo de retenção dos registros de eventos nos servidores e nos painéis de nuvem para garantir um histórico mínimo de 180 dias.

---

#### `DE-04` — Detecção de Atividades Estranhas em Contas Corporativas e E-mails
- **Enunciado:** O sistema de e-mails e nuvem avisa o administrador se uma conta corporativa for acessada a partir de outro país ou em horários anômalos?
- **Explicação de Ajuda (`explicacao_ajuda`):** Invasores frequentemente roubam credenciais e acessam e-mails corporativos da Rússia, China ou Nigéria na madrugada para desviar pagamentos e emitir boletos falsos a clientes.
- **Referência Normativa:** NIST CSF 2.0: `DE.AE-04` | ABNT NBR ISO/IEC 27002:2022: Controle `8.16` (Monitoramento de atividades).
- **Categorias de Ativos Relacionadas:** Mobile & Nuvem (SaaS).
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Não há nenhum controle; a empresa só descobre que o e-mail foi invadido quando um cliente telefona avisando que pagou um boleto falso recebido da nossa empresa.
  * **`MIL_1` (Inicial / Ad-hoc):** Alguns funcionários notam mensagens enviadas estranhas na sua caixa de saída e avisam o suporte.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Alertas nativos de segurança da nuvem habilitados para notificar a diretoria sobre logins incomuns, viagens impossíveis ou criação de regras ocultas de redirecionamento de e-mails.
  * **`MIL_3` (Otimizado):** Monitoramento contínuo de identidade com inteligência de ameaças que revoga sessões automaticamente se as senhas forem vazadas na dark web.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Ativar as regras de alerta no Microsoft 365 ou Google Workspace para avisar logins de outros países e proibir o redirecionamento automático de mensagens para e-mails externos.

---

#### `DE-05` — Canal Simplificado para Reporte de Incidentes pelos Funcionários
- **Enunciado:** Os colaboradores sabem com exatidão a quem comunicar imediatamente caso cliquem em um link suspeito, recebam um boleto falso ou percebam que o computador foi infectado?
- **Explicação de Ajuda (`explicacao_ajuda`):** Quando o colaborador tem medo de ser punido ou não sabe a quem recorrer, ele esconde o erro, permitindo que a invasão se alastre por toda a rede da PME.
- **Referência Normativa:** NIST CSF 2.0: `DE.AE-08` | ABNT NBR ISO/IEC 27002:2022: Controle `6.8` (Notificação de eventos de segurança da informação).
- **Categorias de Ativos Relacionadas:** Organizacional / Todos os Ativos.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Os colaboradores não sabem quem avisar e têm medo de relatar enganos por receio de demissão ou penalização.
  * **`MIL_1` (Inicial / Ad-hoc):** Os funcionários avisam informalmente algum colega ou técnico quando a máquina já parou de funcionar completamente.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Existe um canal oficial, simples e sem julgamento disciplinar (ex.: e-mail de suporte ou grupo de suporte) onde qualquer funcionário é encorajado a relatar incidentes e links suspeitos imediatamente.
  * **`MIL_3` (Otimizado):** Botão integrado no navegador e no aplicativo de e-mail ("Reportar Phishing") que isola automaticamente a mensagem e gera um chamado na central técnica.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `MEDIO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `ALTA` *(Quick Win imediato)*
  * `Recomendacao`: Comunicar à equipe o canal direto de reporte de incidentes (e-mail/WhatsApp do suporte) e incentivar o reporte rápido de mensagens falsas sem aplicação de penalidades ao colaborador.

---

### Função 5: RESPONDER (`RS`) — Contenção, Comunicação, Mitigação e Aprendizado

#### `RS-01` — Plano Prático de Resposta a Incidentes Cibernéticos
- **Enunciado:** A empresa possui um passo a passo escrito e divulgado sobre o que fazer caso a rede seja invadida ou arquivos sejam bloqueados por ransomware?
- **Explicação de Ajuda (`explicacao_ajuda`):** Na hora do ataque cibernético, o pânico toma conta. Ter uma folha com as primeiras atitudes e telefones de emergência evita decisões precipitadas que pioram o desastre.
- **Referência Normativa:** NIST CSF 2.0: `RS.MA-01` | ABNT NBR ISO/IEC 27002:2022: Controles `5.24` (Planejamento e preparação para a gestão de incidentes) e `5.25` (Avaliação e decisão sobre eventos de segurança).
- **Categorias de Ativos Relacionadas:** Organizacional / Todos os Ativos.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Não temos nenhum procedimento; se ocorrer um sequestro de dados ou invasão, ninguém saberá o que fazer.
  * **`MIL_1` (Inicial / Ad-hoc):** O técnico de TI tem algumas ideias na cabeça, mas nada está documentado ou alinhado previamente com os diretores da empresa.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Temos um Plano de Resposta a Incidentes de 1 a 2 páginas, com a ordem de quem acionar (TI, gerência, jurídico) e os primeiros passos para conter a invasão.
  * **`MIL_3` (Otimizado):** Playbooks específicos e detalhados para diferentes tipos de crise cibernética (ransomware, vazamento de clientes, indisponibilidade de nuvem) testados em simulações anuais com a diretoria.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Redigir o Plano de Resposta a Incidentes Simplificado (1 página) contendo os contatos imediatos da TI e a sequência padrão de ações em emergências cibernéticas.

---

#### `RS-02` — Contenção Rápida e Isolamento de Computadores Infectados
- **Enunciado:** Todos os usuários e técnicos sabem que a primeira atitude diante de um vírus ou sequestro de dados é desconectar o computador da rede/Wi-Fi?
- **Explicação de Ajuda (`explicacao_ajuda`):** Os vírus modernos se propagam na velocidade da luz para outros computadores conectados na mesma rede local. Desconectar o cabo ou desligar o Wi-Fi imediatamente estanca o contágio.
- **Referência Normativa:** NIST CSF 2.0: `RS.MI-01` | ABNT NBR ISO/IEC 27002:2022: Controle `5.26` (Resposta a incidentes de segurança da informação).
- **Categorias de Ativos Relacionadas:** Endpoints, Servidores Locais, Ativos de Rede.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Ninguém sabe o que fazer; quando uma máquina trava, o usuário a deixa ligada na rede e outros colaboradores continuam acessando seus arquivos compartilhados.
  * **`MIL_1` (Inicial / Ad-hoc):** Alguns usuários desligam a máquina na tomada, o que pode apagar evidências da memória e corromper o disco de forma desnecessária.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Procedimento claro e divulgado: diante de qualquer comportamento estranho, o colaborador desconecta imediatamente o cabo de rede e desativa o Wi-Fi sem desligar o computador, avisando o suporte técnico.
  * **`MIL_3` (Otimizado):** Isolamento de rede automatizado acionado via agente de proteção de endpoints (EDR), desconectando logicamente a máquina infectada em menos de 60 segundos.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Afixar aviso visual e instruir os funcionários sobre a "Regra de Contenção": retirar o cabo de rede e desligar o Wi-Fi imediatamente ao menor sinal de infecção.

---

#### `RS-03` — Fluxo Formal de Comunicação e Notificação Legal de Violações (ANPD)
- **Enunciado:** A empresa possui processo definido para comunicar incidentes graves de segurança à diretoria e avaliar a necessidade de notificar a ANPD e clientes?
- **Explicação de Ajuda (`explicacao_ajuda`):** Pela LGPD, a Autoridade Nacional de Proteção de Dados (ANPD) e os titulares devem ser notificados sobre vazamentos de dados que possam acarretar risco ou dano relevante aos cidadãos.
- **Referência Normativa:** NIST CSF 2.0: `RS.CO-02` | ABNT NBR ISO/IEC 27002:2022: Controles `5.27` (Aprendizado com os incidentes) e `5.34` (Privacidade de dados pessoais).
- **Categorias de Ativos Relacionadas:** Organizacional / Todos os Ativos.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Se houver um vazamento de dados, a empresa oculta o ocorrido e não sabe quais são as consequências legais.
  * **`MIL_1` (Inicial / Ad-hoc):** A TI conversa informalmente com a diretoria, mas não há análise formal de risco nem avaliação sobre deveres legais de notificação.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Fluxo estabelecido de reporte imediato para a direção e protocolo formal para apuração de vazamentos de dados pessoais junto ao encarregado (DPO)/jurídico em consonância com a LGPD.
  * **`MIL_3` (Otimizado):** Matriz de comunicação de crises pré-definida com modelos prontos de notificação à ANPD, comunicados para clientes e assessoria jurídica de plantão.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Documentar a cadeia de comunicação de incidentes cibernéticos graves para garantir que a alta gestão avalie em até 48 horas se há dados pessoais violados.

---

#### `RS-04` — Limpeza Segura e Erradicação de Ameaças em Equipamentos
- **Enunciado:** Antes de liberar um computador atingido por vírus de volta para o trabalho, a empresa realiza a limpeza completa ou formatação segura do equipamento?
- **Explicação de Ajuda (`explicacao_ajuda`):** Tentar apenas "passar o antivírus" após um ataque grave muitas vezes deixa programas espiões escondidos que reinfectam a rede dias depois.
- **Referência Normativa:** NIST CSF 2.0: `RS.MI-02` | ABNT NBR ISO/IEC 27002:2022: Controle `5.26` (Resposta a incidentes de segurança da informação).
- **Categorias de Ativos Relacionadas:** Endpoints, Servidores Locais.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** A máquina infectada é apenas reiniciada e o usuário continua trabalhando normalmente sem nenhuma verificação ou limpeza.
  * **`MIL_1` (Inicial / Ad-hoc):** O técnico executa um escaneamento rápido de antivírus e devolve a máquina ao funcionário sem investigar a extensão do estrago.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Equipamentos atingidos por infecções graves passam por formatação completa e reinstalação limpa do sistema operacional a partir de imagem oficial, com redefinição imediata de todas as senhas de acesso do usuário.
  * **`MIL_3` (Otimizado):** Coleta e análise de amostras forenses para identificar indicadores de comprometimento (IoCs) em todos os demais equipamentos antes da liberação da máquina.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `MEDIO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `ALTA` *(Quick Win imediato)*
  * `Recomendacao`: Adotar como padrão operacional obrigatório a formatação e reinstalação limpa do sistema operacional em computadores que sofreram infecções graves de ransomware ou trojans.

---

#### `RS-05` — Análise Pós-Incidente e Registro de Lições Aprendidas
- **Enunciado:** Após solucionar um problema de segurança ou tentativa de ataque, a empresa realiza uma reunião breve para entender o que deu errado e melhorar suas defesas?
- **Explicação de Ajuda (`explicacao_ajuda`):** Não adianta apagar o incêndio sem descobrir quem riscou o fósforo. Analisar a causa raiz impede que a empresa seja vítima do mesmo golpe duas vezes.
- **Referência Normativa:** NIST CSF 2.0: `RS.MA-02` | ABNT NBR ISO/IEC 27002:2022: Controles `5.27` (Aprendizado com os incidentes de segurança) e `5.28` (Coleta de evidências).
- **Categorias de Ativos Relacionadas:** Organizacional / Todos os Ativos.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Resolvido o incidente, ninguém mais comenta sobre o assunto e tudo volta a funcionar da mesma forma que antes.
  * **`MIL_1` (Inicial / Ad-hoc):** Comentários informais de corredor sobre a dificuldade que foi recuperar o sistema, sem nenhuma alteração nos procedimentos.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Reunião documentada pós-incidente ("post-mortem" simples) para identificar a falha de entrada e implementar melhorias imediatas nos controles e treinamentos.
  * **`MIL_3` (Otimizado):** Registro formal em base de conhecimento de lições aprendidas, com indicadores de tempo de resposta (MTTR) e auditoria de eficácia das salvaguardas adicionadas.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `MEDIO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `ALTA` *(Quick Win imediato)*
  * `Recomendacao`: Estabelecer uma reunião de 30 minutos pós-incidente entre a TI e a gestão para registrar como o ataque entrou e ajustar as configurações de proteção.

---

### Função 6: RECUPERAR (`RC`) — Continuidade de Negócios, Backups e Restauração

#### `RC-01` — Rotina Automatizada de Backup e Adoção da Regra 3-2-1
- **Enunciado:** A empresa possui rotinas automáticas de backup diário dos arquivos importantes e bancos de dados, mantendo ao menos uma cópia fora da empresa ou na nuvem?
- **Explicação de Ajuda (`explicacao_ajuda`):** A Regra 3-2-1 estabelece manter 3 cópias dos dados, em 2 mídias diferentes, sendo 1 cópia fora do local da empresa (como em nuvem segura). Se o escritório pegar fogo ou for assaltado, os dados continuam salvos.
- **Referência Normativa:** NIST CSF 2.0: `RC.RP-01` e `PR.IP-04` | ABNT NBR ISO/IEC 27002:2022: Controle `8.13` (Cópias de segurança da informação - Backup).
- **Categorias de Ativos Relacionadas:** Servidores Locais, Mobile & Nuvem.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Não fazemos backup de nada; se o disco do servidor queimar ou for criptografado, perderemos todos os dados da empresa.
  * **`MIL_1` (Inicial / Ad-hoc):** Copiamos pastas manualmente para um pendrive ou HD externo de vez em quando, e o dispositivo fica conectado direto no computador.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Rotina diária de backup automatizada com software próprio, mantendo cópias locais e pelo menos uma cópia enviada diariamente para nuvem segura ou dispositivo externo protegido.
  * **`MIL_3` (Otimizado):** Replicação contínua em tempo real com alta disponibilidade em nuvem, garantindo perda de dados próxima de zero (RPO mínimo) e cópias geodistribuídas.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `MEDIO`
  * `Prioridade`: `ALTA`
  * `Recomendacao`: Implementar rotina diária automatizada de backup com envio para armazenamento em nuvem ou destino protegido contra acesso local direto.

---

#### `RC-02` — Testes Periódicos de Restauração de Dados (Restore Test)
- **Enunciado:** A empresa realiza testes práticos para confirmar se os arquivos do backup realmente abrem e podem ser recuperados em caso de emergência?
- **Explicação de Ajuda (`explicacao_ajuda`):** Quem tem backup e não testa, não tem backup. É muito comum descobrir que o arquivo de backup estava corrompido apenas no dia em que o servidor quebra.
- **Referência Normativa:** NIST CSF 2.0: `RC.RP-02` | ABNT NBR ISO/IEC 27002:2022: Controles `8.13` (Cópias de segurança) e `5.29` (Segurança da informação durante a interrupção).
- **Categorias de Ativos Relacionadas:** Servidores Locais, Mobile/Nuvem.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Nunca realizamos nenhum teste de restauração; só saberemos se funciona no dia em que precisarmos recuperar algo de verdade.
  * **`MIL_1` (Inicial / Ad-hoc):** Apenas olhamos se o tamanho do arquivo gerado parece normal no disco, sem nunca tentar abrir ou restaurar arquivos reais em outra máquina.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Realizamos simulações periódicas formais de restauração (ao menos semestralmente), recuperando dados em um computador de teste para certificar que tudo volta a funcionar sem erros.
  * **`MIL_3` (Otimizado):** Testes de restauração automatizados com verificação de integridade e validação de funcionamento dos bancos de dados conduzidos trimestralmente com relatório executivo.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `CRITICA` *(Quick Win imediato)*
  * `Recomendacao`: Agendar um teste prático semestral de restauração de backup em máquina isolada, validando a integridade dos arquivos e bancos de dados da PME.

---

#### `RC-03` — Definição de Prazos Limites de Paralisação e Perda de Dados (RTO e RPO)
- **Enunciado:** A diretoria da empresa sabe exatamente quanto tempo o negócio aguenta ficar parado sem faturar e qual o limite aceitável de perda de dados?
- **Explicação de Ajuda (`explicacao_ajuda`):** O RTO é o tempo máximo para os sistemas voltarem a funcionar (ex.: até 8 horas). O RPO é quanto tempo de digitação/pedidos se pode perder (ex.: compras das últimas 4 horas). Sem esses limites, a TI não sabe qual estrutura de backup contratar.
- **Referência Normativa:** NIST CSF 2.0: `RC.CO-03` | ABNT NBR ISO/IEC 27002:2022: Controles `5.29` (Segurança durante interrupções) e `5.30` (Prontidão das TIC para continuidade do negócio).
- **Categorias de Ativos Relacionadas:** Servidores Locais, Mobile/Nuvem, Organizacional.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Não fazemos ideia de quanto tempo levaria para recuperar o negócio; presumimos que se quebrar tudo, a recuperação será rápida por sorte.
  * **`MIL_1` (Inicial / Ad-hoc):** Estimativas vagas da equipe de suporte ("se quebrar a gente dá um jeito em alguns dias"), sem alinhamento com a diretoria.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** Metas formais de RTO (tempo máximo de paralisação admissível, ex.: até 1 dia útil) e RPO (perda máxima aceitável de dados, ex.: até 24 horas) alinhadas e aprovadas por escrito com a diretoria.
  * **`MIL_3` (Otimizado):** Contratos de suporte com SLA garantido de fornecedores externos, monitoramento contínuo dos tempos de recuperação e planos de contingência para RTO inferior a 2 horas.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `MEDIO`
  * `CustoMitigacao`: `BAIXO`
  * `Prioridade`: `ALTA` *(Quick Win imediato)*
  * `Recomendacao`: Formalizar com os gestores da PME a tolerância máxima a paralisações (RTO) e a perda aceitável de dados (RPO), adequando a rotina de backup a essas metas.

---

#### `RC-04` — Procedimentos de Contingência e Recursos Alternativos de Trabalho
- **Enunciado:** Se a internet principal cair, faltar energia ou o servidor pifar, a empresa tem alternativas prontas para continuar trabalhando em contingência?
- **Explicação de Ajuda (`explicacao_ajuda`):** Ter um link secundário de internet (ex.: modem 4G/5G), nobreaks nos servidores de arquivos e máquinas reservas impede que a PME feche as portas diante de panes comuns de infraestrutura.
- **Referência Normativa:** NIST CSF 2.0: `RC.RP-03` | ABNT NBR ISO/IEC 27002:2022: Controles `5.29` (Segurança durante interrupções) e `5.30` (Prontidão das TIC).
- **Categorias de Ativos Relacionadas:** Endpoints, Ativos de Rede, Mobile/Nuvem.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** Não temos nada; se a internet cair ou faltar luz por 10 minutos, toda a empresa para completamente de trabalhar.
  * **`MIL_1` (Inicial / Ad-hoc):** Quando a internet cai, alguns funcionários improvisam roteando a internet do celular pessoal para tentar emitir uma nota fiscal urgente.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** A empresa possui nobreak protegendo o servidor e o roteador, link de contingência (ou roteador celular corporativo homologado) e procedimentos documentados para operação mínima de emergência.
  * **`MIL_3` (Otimizado):** Plano formal de Continuidade do Negócio (PCN) com redundância completa de internet automática (dual WAN failover) e operação 100% capaz de migrar para trabalho remoto em minutos.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `MEDIO`
  * `CustoMitigacao`: `MEDIO`
  * `Prioridade`: `MEDIA`
  * `Recomendacao`: Prover nobreaks revisados para os ativos de rede/servidor principal e homologar um link secundário de internet de contingência (como conexão 4G/5G).

---

#### `RC-05` — Proteção dos Backups Contra Ataques de Ransomware (Imutabilidade e Isolamento)
- **Enunciado:** As cópias de backup ficam protegidas contra sequestro por ransomware (isoladas da rede ou protegidas contra exclusão e criptografia)?
- **Explicação de Ajuda (`explicacao_ajuda`):** Os criminosos cibernéticos de hoje procuram e destroem os backups antes de criptografar as máquinas da vítima. Se o backup estiver acessível na mesma rede, ele será destruído no ataque.
- **Referência Normativa:** NIST CSF 2.0: `PR.IP-04` e `RC.RP-01` | ABNT NBR ISO/IEC 27002:2022: Controle `8.13` (Cópias de segurança da informação).
- **Categorias de Ativos Relacionadas:** Servidores Locais, Mobile/Nuvem.
- **Opções de Resposta Graduadas:**
  * **`MIL_0` (Inexistente):** O disco de backup é uma pasta compartilhada aberta na rede interna; qualquer vírus que infecte uma máquina comum pode apagar ou criptografar o backup inteiro.
  * **`MIL_1` (Inicial / Ad-hoc):** O backup fica em um HD externo que permanece plugado no servidor o tempo todo com as mesmas senhas de acesso.
  * **`MIL_2` (Padronizado — Alvo Recomendado):** O backup possui proteção contra ransomware: ou é gravado em nuvem com retenção imutável (impossível de apagar por 30 dias), ou é salvo em mídia externa que é fisicamente desconectada da rede após o término da cópia (air-gap).
  * **`MIL_3` (Otimizado):** Cofre de dados isolado com arquitetura Zero Trust, chaves criptográficas segregadas em hardware e detecção automatizada de alterações anômalas no volume dos backups.
- **Metadados de Geração de Gap (se resposta < `MIL_2`):**
  * `NivelImpacto`: `ALTO`
  * `CustoMitigacao`: `MEDIO`
  * `Prioridade`: `ALTA`
  * `Recomendacao`: Implementar armazenamento imutável na nuvem (object lock) ou instituir rotina de rodízio de discos externos que fiquem desconectados fisicamente da rede após a gravação diária.

---

## 3. Matriz Resumo Cruzada (30 Questões × NIST CSF 2.0 × ISO 27002:2022)

A tabela abaixo sintetiza o mapeamento completo das 30 questões do instrumento de diagnóstico, relacionando cada item com a subcategoria do NIST CSF 2.0, os controles correspondentes da ABNT NBR ISO/IEC 27002:2022, a categoria de ativo prioritária e a classificação de prioridade gerada quando o resultado fica abaixo do patamar recomendado (`MIL_2`).

| # | Código | Função NIST | Subcategoria NIST CSF 2.0 | Controle ISO/IEC 27002:2022 | Categoria de Ativo Prioritária | Nível Impacto | Custo Mitigação | Prioridade do Gap |
|---|---|---|---|---|---|---|---|---|
| 1 | **GV-01** | Governar | `GV.PO-01` | Controle `5.1` (Políticas para segurança) | Organizacional / Todos | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 2 | **GV-02** | Governar | `GV.RR-01` | Controle `5.2` (Papéis e responsabilidades) | Organizacional / Todos | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 3 | **GV-03** | Governar | `GV.SC-04` | Controles `5.19` e `5.20` (Gestão de fornecedores) | Servidores Locais, Rede, Nuvem | `ALTO` | `MEDIO` | `ALTA` |
| 4 | **GV-04** | Governar | `PR.AT-01` / `GV.PO-02` | Controle `6.3` (Conscientização e capacitação) | Endpoints, Nuvem, Organizacional | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 5 | **GV-05** | Governar | `GV.OC-03` | Controles `5.31` e `5.34` (Conformidade legal e LGPD) | Organizacional / Todos | `ALTO` | `MEDIO` | `ALTA` |
| 6 | **ID-01** | Identificar | `ID.AM-01` | Controle `5.9` (Inventário de ativos) | Endpoints, Servidores, Rede | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 7 | **ID-02** | Identificar | `ID.AM-02` | Controles `5.9` e `8.19` (Softwares homologados) | Endpoints, Servidores Locais | `MEDIO` | `BAIXO` | `ALTA` *(Quick Win)* |
| 8 | **ID-03** | Identificar | `ID.AM-03` | Controle `5.23` (Segurança em serviços de nuvem) | Mobile & Nuvem (SaaS) | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 9 | **ID-04** | Identificar | `ID.AM-05` | Controles `5.12` e `5.13` (Classificação de dados) | Servidores Locais, Nuvem | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 10 | **ID-05** | Identificar | `ID.RA-01` | Controle `8.8` (Gestão de vulnerabilidades técnicas) | Endpoints, Servidores, Rede | `MEDIO` | `BAIXO` | `ALTA` *(Quick Win)* |
| 11 | **PR-01** | Proteger | `PR.AA-01` / `PR.AA-05` | Controles `5.15` e `8.2` (Menor privilégio e acessos) | Endpoints, Servidores Locais | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 12 | **PR-02** | Proteger | `PR.AA-03` | Controles `5.17` e `8.5` (Autenticação multifator - MFA) | Mobile/Nuvem, Ativos de Rede | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 13 | **PR-03** | Proteger | `PR.PS-01` | Controle `8.7` (Proteção contra malware / EDR) | Endpoints, Servidores Locais | `ALTO` | `MEDIO` | `ALTA` |
| 14 | **PR-04** | Proteger | `PR.PS-02` | Controles `8.8` e `8.19` (Patches e atualizações) | Endpoints, Servidores, Rede | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 15 | **PR-05** | Proteger | `PR.IR-01` | Controles `8.20` e `8.22` (Firewall e segmentação Wi-Fi) | Ativos de Rede e Perímetro | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 16 | **DE-01** | Detectar | `DE.CM-01` | Controle `8.16` (Alertas centralizados de ameaças) | Endpoints, Servidores Locais | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 17 | **DE-02** | Detectar | `DE.AE-02` | Controles `8.20` e `8.21` (Auditoria de conexões de rede) | Ativos de Rede, Servidores | `MEDIO` | `MEDIO` | `MEDIA` |
| 18 | **DE-03** | Detectar | `DE.CM-03` | Controle `8.15` (Retenção de logs por 6 meses) | Servidores Locais, Nuvem | `MEDIO` | `BAIXO` | `ALTA` *(Quick Win)* |
| 19 | **DE-04** | Detectar | `DE.AE-04` | Controle `8.16` (Detecção de anomalias em nuvem) | Mobile & Nuvem (SaaS) | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 20 | **DE-05** | Detectar | `DE.AE-08` | Controle `6.8` (Canal de reporte de incidentes) | Organizacional / Todos | `MEDIO` | `BAIXO` | `ALTA` *(Quick Win)* |
| 21 | **RS-01** | Responder | `RS.MA-01` | Controles `5.24` e `5.25` (Plano prático de incidentes) | Organizacional / Todos | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 22 | **RS-02** | Responder | `RS.MI-01` | Controle `5.26` (Contenção e isolamento de rede) | Endpoints, Servidores, Rede | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 23 | **RS-03** | Responder | `RS.CO-02` | Controles `5.27` e `5.34` (Comunicação e deveres LGPD) | Organizacional / Todos | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 24 | **RS-04** | Responder | `RS.MI-02` | Controle `5.26` (Limpeza e reinstalação limpa) | Endpoints, Servidores Locais | `MEDIO` | `BAIXO` | `ALTA` *(Quick Win)* |
| 25 | **RS-05** | Responder | `RS.MA-02` | Controles `5.27` e `5.28` (Lições aprendidas pós-incidente) | Organizacional / Todos | `MEDIO` | `BAIXO` | `ALTA` *(Quick Win)* |
| 26 | **RC-01** | Recuperar | `RC.RP-01` / `PR.IP-04` | Controle `8.13` (Rotina diária de backup 3-2-1) | Servidores Locais, Nuvem | `ALTO` | `MEDIO` | `ALTA` |
| 27 | **RC-02** | Recuperar | `RC.RP-02` | Controles `8.13` e `5.29` (Testes semestrais de restore) | Servidores Locais, Nuvem | `ALTO` | `BAIXO` | `CRITICA` *(Quick Win)* |
| 28 | **RC-03** | Recuperar | `RC.CO-03` | Controles `5.29` e `5.30` (Definição de metas RTO / RPO) | Servidores, Nuvem, Gestão | `MEDIO` | `BAIXO` | `ALTA` *(Quick Win)* |
| 29 | **RC-04** | Recuperar | `RC.RP-03` | Controles `5.29` e `5.30` (Nobreaks e link de contingência) | Endpoints, Rede, Nuvem | `MEDIO` | `MEDIO` | `MEDIA` |
| 30 | **RC-05** | Recuperar | `PR.IP-04` / `RC.RP-01` | Controle `8.13` (Backup imutável / isolamento de rede) | Servidores Locais, Nuvem | `ALTO` | `MEDIO` | `ALTA` |

---

## 4. Diretrizes para o Povoamento do Banco de Dados (`data.sql`)

As 30 questões especificadas neste instrumento possuem correlação direta com as tabelas relacionais do sistema e alimentarão o arquivo de inicialização de dados (`src/main/resources/data.sql`):

1. **`tb_funcao_nist`:** As 6 funções (`GV`, `ID`, `PR`, `DE`, `RS`, `RC`) cadastradas com ordem de 1 a 6.
2. **`tb_questao_diagnostico`:** Cada questão inserida com seu respectivo `codigo` (`GV-01` a `RC-05`), `enunciado`, `explicacao_ajuda`, `mil_alvo_minimo = 'MIL_2'`, `ordem` e chave estrangeira `funcao_nist_id`.
3. **`tb_opcao_resposta`:** Para cada uma das 30 questões, exatamente **4 registros** associados com `nivel_mil` (`MIL_0`, `MIL_1`, `MIL_2`, `MIL_3`) e `peso_pontuacao` (`0.0`, `1.0`, `2.0`, `3.0`). Total de **120 opções de resposta**.
4. **Motor de Score e Gaps:** As metatags de `NivelImpacto`, `CustoMitigacao`, `Prioridade` e `Recomendacao` mapeiam os atributos utilizados pelo `ScoreService` ao identificar respostas abaixo de `MIL_2`, instanciando a entidade `Gap` e povoando a matriz de risco do dashboard da PME.
