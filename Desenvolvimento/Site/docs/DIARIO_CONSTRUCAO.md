# Diário de Construção e Arquitetura — SecureDesk (TCC ADS Fatec ZL)

> [!NOTE]
> Este documento registra de forma contínua e auditável tudo o que está sendo construído, configurado, especificado e decidido no desenvolvimento do **SecureDesk** (Framework Simplificado de Cibersegurança e Modelo de Maturidade NIST CSF 2.0 × C2M2 para PMEs brasileiras com até 100 ativos).

---

## 1. Visão Geral da Arquitetura Atual

- **Produto:** SecureDesk (`br.edu:securedesk`)
- **Stack:** Java 21 + Spring Boot 4.1.1 (Spring Web/Thymeleaf, Spring Data JPA, Persistência compatível com MySQL/MariaDB em `dev` e `prod`, H2 in-memory isolado em `test`, Lombok).
- **Pacote raiz:** `br.edu.securedesk`
- **Módulos de Domínio (30 classes em `entity/`):**
  1. `core`: `Empresa`, `PorteEmpresa`, `SetorEmpresa`, `Usuario`, `PerfilUsuario`, `TermoConsentimentoLgpd`
  2. `ativo`: `AtivoTI`, `TipoAtivo`
  3. `diagnostico`: `FuncaoNist`, `SiglaFuncaoNist`, `NivelMil`, `QuestaoDiagnostico`, `OpcaoResposta`, `Diagnostico`, `StatusDiagnostico`, `RespostaDiagnostico`, `ScoreFuncao`
  4. `governanca`: `Gap`, `Prioridade`, `NivelImpacto`, `CustoMitigacao`, `TarefaRemediacao`, `StatusTarefa`, `Evidencia`
  5. `compliance`: `TemplatePolitica`, `PoliticaEmpresa`, `StatusPolitica`, `HistoricoRevisaoPolitica`, `Notificacao`, `TipoNotificacao`

---

## 2. Histórico Detalhado de Entregas

### [19/10/2026] — Fase 0: Higiene do Repositório e Configuração de Ambiente

1. **Controle de Versão (`.gitignore` e limpeza de índice):**
   - Criado `Desenvolvimento/Site/.gitignore` ignorando `target/`, `.idea/`, `.vscode/`, `.maestri/` e logs.
   - Executado `git rm -r --cached target/ .idea/ .vscode/` para remover 70+ arquivos compilados e de IDE que causavam conflito a cada build, mantendo os arquivos locais intactos.
   - Removido o diretório duplicado/legado `Desenvolvimento/06 - Desenvolvimento/`.

2. **Padronização do Nome do Produto (`SecureDesk`):**
   - `pom.xml`: atualizado `<artifactId>` de `microsec` para `securedesk`, `<name>` para `SecureDesk` e adicionada descrição oficial do TCC.
   - `application.properties`: atualizado `spring.application.name=securedesk`.
   - Renomeado `MicrosecApplicationTests.java` para `SecureDeskApplicationTests.java`.

3. **Perfis de Banco de Dados (`dev` e `prod`):**
   - Adicionada dependência `com.h2database:h2` (`runtime`) ao `pom.xml`.
   - `src/main/resources/application.properties`: define `spring.profiles.active=dev`, `UTF-8` e `spring.jpa.open-in-view=false`.
   - `src/main/resources/application-dev.properties`: banco H2 em memória (`jdbc:h2:mem:securedesk;MODE=MySQL`), `ddl-auto=update`, console H2 ativo em `/h2-console` e cache do Thymeleaf desativado.
   - `src/main/resources/application-prod.properties`: conexão parametrizada por variáveis de ambiente (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) para MySQL/MariaDB com `ddl-auto=validate`.
   - **Validação:** `./mvnw -q test` executado com sucesso (17 tabelas geradas pelo Hibernate no H2 e contexto Spring Boot carregado em ~6,3s).

4. **Organização do Time Maestri:**
   - Atualizados via `maestri role write` os prompts definitivos de papel para `Flash 1` (Dados/DER/SQL/Requisitos), `Flash 2` (Web/Thymeleaf/Controllers/QA) e `GPT-OSS 120B` (Arquitetura/Score C2M2/Segurança).

---

## 3. Entregas da 1ª Leva (Fase 1 — Especificação do TG1 & Fase 2 — Protótipo)

### [19/10/2026] — Item 1.1: Documento de Requisitos (`docs/REQUISITOS.md`)
- **Responsável:** Argon (Supervisor)
- **Conteúdo construído:**
  - Definição formal de atores (`ADMINISTRADOR`, `EDITOR`, `VISUALIZADOR`).
  - **19 Requisitos Funcionais (`RF01` a `RF19`)** cobrindo Core/LGPD, Inventário de Ativos (≤ 100), Diagnóstico NIST × MIL, Acompanhamento/Radar/Matriz de Prioridade, Governança (Kanban/Evidências) e Compliance (Templates PSI/Acesso/Backup e Alertas).
  - **6 Regras de Negócio (`RN01` a `RN06`)**, destacando `RN01` (trava de 100 dispositivos), `RN02` (progressão cumulativa C2M2) e `RN03` (patamar alvo MIL 2).
  - **6 Requisitos Não-Funcionais (`RNF01` a `RNF06`)**, Diagrama de Casos de Uso em Mermaid e Matriz de Rastreabilidade cruzando OEM/OED do `tcc-projeto.html`.

### [19/10/2026] — Item 1.2: DER e Relatório de Inconsistências (`docs/DER.md`)
- **Responsável:** Flash 1
- **Conteúdo construído:**
  - Diagrama `erDiagram` completo em Mermaid cobrindo as **17 tabelas JPA** e todos os seus relacionamentos, PKs e FKs.
  - Dicionário de Dados detalhado das 17 tabelas e 13 Enums.
  - **Relatório de 8 Inconsistências Código × Monografia** (preparando insumo técnico para as decisões da Seção 3):
    1. `TipoAtivo` com 6 valores no Java vs. 4 categorias na Monografia;
    2. `Diagnostico.milMedio` (contínuo) vs. regra cumulativa estrita C2M2;
    3. Ausência de trava de domínio para o limite de 100 ativos;
    4. Desconexão entre `QuestaoDiagnostico` e categorias de ativos;
    5. Falta de coluna `controle_iso_referencia` em `tb_questao_diagnostico` para rastrear ABNT NBR ISO/IEC 27001/27002;
    6. Falta de constraint `UNIQUE (diagnostico_id, questao_id)` em `tb_resposta_diagnostico`;
    7. Assimetria bidirecional entre `Diagnostico` e `Gap`;
    8. Delimitação de escopo dos Módulos 3 e 4 (TCC 1 vs. TCC 2).

### [19/10/2026] — Item 1.4: Especificação do Motor de Score Cumulativo C2M2 (`docs/ESPECIFICACAO_SCORE_C2M2.md`)
- **Responsável:** GPT-OSS 120B (revisado e refinado por Argon)
- **Conteúdo construído:**
  - Formalização matemática da trava cumulativa por função $f \in \{\text{GV, ID, PR, DE, RS, RC}\}$ ($\text{MIL}_f \in \{0,1,2,3\}$) combinada ao indicador contínuo `scorePercentual_f` ($0\%$ a $100\%$).
  - Regra de geração automática de `Gap` para práticas com $\text{MIL} < 2$ e matriz de priorização (`Impacto × Distância / Custo`) destacando *Quick Wins* (`Impacto ALTO` + `Custo BAIXO` = `Prioridade.CRITICA`).
  - Fórmulas prontas para Google Sheets / Excel / Calc para validação do **OED 1**.
  - **8 Casos de Teste Determinísticos** (incluindo o caso de "buraco cumulativo" onde 4 questões estão em MIL 3 e 1 em MIL 0, travando o MIL formal em 0 com 80% de progresso).

### [19/10/2026] — Fase 2: Protótipo Navegável (`PageController` + Fragmentos Thymeleaf + Testes MockMvc)
- **Responsável:** Flash 2
- **Conteúdo construído:**
  - `src/main/resources/templates/fragments/layout.html`: extração dos fragmentos parametrizados `th:fragment="sidebar(activePage)"` e `th:fragment="topbar(pageTitle, breadcrumb)"`.
  - Refatoração das 6 telas internas (`dashboard.html`, `diagnostico.html`, `acompanhamento.html`, `governanca.html`, `compliance.html`, `configuracoes.html`) para consumir os fragmentos e exibir variáveis dinâmicas do `Model` (com fallback estático), além do fluxo de login (`POST /login` → `redirect:/dashboard`).
  - `src/main/java/br/edu/securedesk/controller/PageController.java`: mapeamento de todas as 8 rotas GET + POST `/login`, populando via `@ModelAttribute` dados realistas de PME ("InnovaTech Soluções Ltda.", 42/100 ativos, scores GV/ID/PR/DE/RS/RC, MIL atual vs. MIL 2 alvo).
  - `src/test/java/br/edu/securedesk/controller/PageControllerTest.java`: 9 testes automatizados com `@SpringBootTest` + `@AutoConfigureMockMvc` validando HTTP 200/302, views e atributos do Model em todas as rotas.
  - **Validação:** `./mvnw -q test` executado com **100% de sucesso** (10 testes passando: 9 em `PageControllerTest` + 1 em `SecureDeskApplicationTests`).

---

## 4. Entregas da 2ª Leva (Fechamento da Fase 1 — Especificação do TG1)

### [20/10/2026] — Item 1.7: Mapeamento de Conformidade LGPD (`docs/MAPEAMENTO_LGPD.md` — OEM 6a)
- **Responsável:** Argon (Supervisor)
- **Conteúdo construído:**
  - Enquadramento jurídico Controlador (PME cliente) × Operador (SecureDesk) × Titular (Usuário).
  - Inventário de Dados Pessoais (RoPA simplificado — Art. 37) cobrindo todas as colunas das 17 tabelas JPA, finalidades e bases legais (Art. 7º).
  - Mapeamento dos Princípios do Art. 6º (Finalidade, Adequação, Minimização sem coleta de CPF/endereço pessoal, Segurança BCrypt/RBAC/Multi-tenant e Prestação de Contas).
  - Especificação do fluxo de aceite obrigatório (`TermoConsentimentoLgpd` / `RN05`) e estratégia de anonimização para preservar histórico de auditoria sem reter dados pessoais em caso de revogação/desligamento (Art. 18).

### [20/10/2026] — Item 1.3: Instrumento de Diagnóstico NIST × C2M2 × ISO 27002 (`docs/QUESTIONARIO_DIAGNOSTICO.md` — OED 3)
- **Responsável:** Flash 1
- **Conteúdo construído:**
  - **30 questões fechadas** (5 por função: `GV-01..05`, `ID-01..05`, `PR-01..05`, `DE-01..05`, `RS-01..05`, `RC-01..05`), totalizando **120 opções de resposta** graduadas de `MIL_0` a `MIL_3`.
  - Mapeamento duplo por questão: subcategoria do **NIST CSF 2.0** + controle da **ABNT NBR ISO/IEC 27002:2022**.
  - Metadados completos para o motor de Gaps: `NivelImpacto`, `CustoMitigacao`, `Prioridade` (destacando *Quick Wins*) e `Recomendacao` prática.

### [20/10/2026] — Item 1.5: Templates Base de Políticas (`docs/TEMPLATES_POLITICAS.md` — OED 2)
- **Responsável:** GPT-OSS 120B
- **Conteúdo construído:**
  - **Template 1:** Política de Segurança da Informação (PSI) Geral (NIST `GV` / ISO 27001 5.2, ISO 27002 5.1 / LGPD Art. 46 e 50).
  - **Template 2:** Política de Controle de Acesso e Gestão de Credenciais (NIST `PR` / ISO 27002 5.15–5.18, 8.2, 8.3, 8.5, menor privilégio e MFA).
  - **Template 3:** Política de Backup e Recuperação de Dados — Regra 3-2-1 (NIST `RC` e `PR.DS` / ISO 27002 8.13, testes trimestrais de restauração e RTO/RPO).

### [20/10/2026] — Item 1.6: Plano de Testes de Software (`docs/PLANO_TESTES.md` — OEM 7a)
- **Responsável:** Flash 2
- **Conteúdo construído:**
  - Estratégia e Pirâmide de Testes (Unitários JUnit 5/Mockito, Integração `@SpringBootTest`/MockMvc/H2 e Aceitação UAT).
  - Especificação de `UT-SCORE-01` a `UT-SCORE-08` (regra cumulativa C2M2) e `UT-ATIVO-01` a `UT-ATIVO-04` (trava de 100 ativos `RN01`).
  - Especificação de testes de integração, isolamento multi-empresa (`RN04`), imutabilidade (`RN06`), LGPD (`RN05`) e roteiro de 6 etapas para o Estudo de Caso na empresa parceira com métricas SUS ($\ge 70$).

---

## 5. Persistência Real Unificada (MySQL / MariaDB) e Isolamento de Testes

### [31/10/2026] — Integração com Banco Relacional Persistente (`jdbc:mysql://`)
- **Responsável:** Argon (Supervisor)
- **Conteúdo construído:**
  - **Compatibilidade Simultânea MySQL + MariaDB:** Configurado `src/main/resources/application-dev.properties` e `application-prod.properties` utilizando o protocolo padrão MySQL (`jdbc:mysql://localhost:3306/securedesk`), driver `com.mysql.cj.jdbc.Driver` e dialeto `org.hibernate.dialect.MySQLDialect`. Essa configuração funciona de forma transparente tanto em ambientes com **MySQL Server** quanto em ambientes Arch Linux com **MariaDB** (`13.0.2-MariaDB`), mantendo 100% de aderência à especificação do TCC.
  - **Isolamento da Suíte de Testes (`src/test/resources/application.properties`):** Criado arquivo de propriedades exclusivo para o escopo de testes apontando para o H2 em memória (`jdbc:h2:mem:securedesk_test;MODE=MySQL`), garantindo que a execução de `./mvnw test` (76 testes automatizados) jamais limpe ou afete os dados reais persistidos no banco MySQL/MariaDB.





