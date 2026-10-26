# Mapeamento de Conformidade LGPD (Lei nº 13.709/2018) — SecureDesk

> [!NOTE]
> **Projeto:** SecureDesk — Plataforma de Diagnóstico e Governança Progressiva de Cibersegurança para Pequenos Parques Tecnológicos (NIST CSF 2.0 × C2M2)  
> **Artefato do TG1:** Item 1.7 do `PLANO_DESENVOLVIMENTO.md` / Entregável **OEM 6a** (*"Mapear os requisitos da LGPD aplicáveis ao tratamento de dados dos usuários do sistema e especificar como o sistema os atende"*)

---

## 1. Contextualização e Papéis dos Agentes de Tratamento (Art. 5º, VI a IX)

O **SecureDesk** realiza tratamento de dados em duas dimensões distintas que precisam ser separadas na Monografia (Capítulo 4):
1. **Dados Corporativos / Técnicos da PME (Não sujeitos à LGPD diretamente, salvo quando identificarem pessoa natural):** CNPJ, razão social, porte, setor, topologia de rede, respostas sobre maturidade de processos (`tb_diagnostico`, `tb_score_funcao`, `tb_gap`).
2. **Dados Pessoais de Usuários e Colaboradores (Sujeitos à LGPD — Art. 5º, I):** Dados de identificação dos profissionais que operam a plataforma ou são designados como responsáveis técnicos por ativos/tarefas.

### 1.1 Enquadramento Legal (Controlador × Operador)
| Papel LGPD | Quem exerce no contexto do SecureDesk | Responsabilidade Legal |
|---|---|---|
| **Controlador (Art. 5º, VI)** | A **PME Cliente** cadastrada em `tb_empresa` (quando cadastra seus colaboradores e designa responsáveis internos por ativos e tarefas). | Definir a finalidade do uso da plataforma dentro da sua organização e garantir a base legal perante seus funcionários. |
| **Operador / Co-controlador da Plataforma (Art. 5º, VII)** | O provedor da plataforma **SecureDesk**. | Executar o tratamento estritamente para viabilizar o diagnóstico NIST × C2M2, implementar medidas técnicas de segurança (Art. 46) e manter trilha auditável de consentimento (Art. 8º, § 2º). |
| **Titular dos Dados (Art. 5º, V)** | O **Usuário** cadastrado em `tb_usuario` (`ADMINISTRADOR`, `EDITOR`, `VISUALIZADOR`) e o colaborador nominalmente citado em `tb_ativo_ti.responsavel`. | Pessoa natural a quem se referem os dados pessoais objeto de tratamento. |

---

## 2. Inventário de Dados Pessoais Tratados pelo Sistema (RoPA Simplificado — Art. 37)

A tabela abaixo mapeia **exatamente** todas as colunas das 17 tabelas JPA do SecureDesk que armazenam dados pessoais (nenhum **dado pessoal sensível** do Art. 5º, II — origem racial, convicção religiosa, saúde, biometria ou opinião política — é coletado):

| Tabela / Entidade JPA | Coluna / Atributo | Categoria do Dado | Finalidade Específica (Art. 6º, I) | Base Legal (Art. 7º) | Retenção e Descarte (Art. 15 e 16) |
|---|---|---|---|---|---|
| `tb_usuario` (`Usuario`) | `nome`, `sobrenome` | Dado Pessoal Identificável | Identificação visual no painel, atribuição de autoria em diagnósticos, tarefas do Kanban e revisões de políticas. | **Consentimento** (Art. 7º, I) e **Execução de Contrato / Preliminar** (Art. 7º, V) | Enquanto a conta/empresa estiver ativa (`ativo = true`) ou até solicitação de exclusão/anonimização. |
| `tb_usuario` (`Usuario`) | `email` (`UNIQUE`) | Dado Pessoal Identificável | Credencial de login (`username`) e canal para alertas de revisão de políticas e segurança. | **Execução de Contrato** (Art. 7º, V) e **Legítimo Interesse / Prevenção à Fraude** (Art. 7º, IX e Art. 11, II, *g*) | Enquanto a conta estiver ativa. |
| `tb_usuario` (`Usuario`) | `senha` | Credencial de Segurança | Autenticação segura no sistema. Armazenada **exclusivamente como hash criptográfico BCrypt** (irreversível). | **Prevenção à Fraude e Segurança do Titular** | Substituída a cada troca de senha; eliminada na exclusão da conta. |
| `tb_termo_consentimento_lgpd` (`TermoConsentimentoLgpd`) | `ip_origem`, `data_aceite`, `versao_termo`, `aceito` | Dado Técnico / Log de Auditoria Legal | Prova jurídica do consentimento informado (ônus da prova do controlador — Art. 8º, § 2º) e conformidade com o Marco Civil da Internet (Lei 12.965/2014). | **Cumprimento de Obrigação Legal/Regulatória** (Art. 7º, II) | Guarda mínima de 6 meses (Art. 15 do Marco Civil) ou durante a vigência da relação contratual para fins de auditoria. |
| `tb_ativo_ti` (`AtivoTI`) | `responsavel` | Dado Pessoal Indireto (ou Setor) | Indicar o custodiante interno do ativo de TI para fins de controle patrimonial e resposta a incidentes (ISO 27002 controle 5.9). | **Legítimo Interesse do Controlador** (Art. 7º, IX) — recomendando-se na interface usar cargo/setor quando possível. | Editável/removível a qualquer momento pelo `ADMINISTRADOR` ou `EDITOR` da PME. |
| `tb_evidencia` (`Evidencia`) | `usuario_upload_id`, `caminho_arquivo` | Rastro de Auditoria Interna | Comprovar a implementação dos controles de segurança rumo ao patamar **MIL 2**. | **Execução de Contrato** (Art. 7º, V) | Orientação expressa na UI para **não enviar documentos com dados pessoais de terceiros** nas evidências técnicas. |

---

## 3. Atendimento aos Princípios da LGPD (Art. 6º) no Projeto do Sistema

| Princípio (Art. 6º) | Requisito Legal | Como o SecureDesk implementa na Arquitetura |
|---|---|---|
| **I. Finalidade** | Tratamento para propósitos legítimos, específicos, explícitos e informados. | Os dados do usuário são usados exclusivamente para autenticação, trilha de auditoria do diagnóstico de maturidade e atribuição de tarefas internas de segurança. Não há compartilhamento com terceiros nem uso para marketing/perfilamento. |
| **II. Adequação** | Compatibilidade do tratamento com as finalidades informadas. | Os relacionamentos de `Usuario` no banco (`tb_diagnostico.usuario_responsavel_id`, `tb_tarefa_remediacao.responsavel_id`, `tb_historico_revisao_politica.revisor_id`) refletem diretamente o requisito do **C2M2 MIL 2** (*"práticas atribuídas a responsáveis definidos"*). |
| **III. Necessidade (Minimização)** | Limitação do tratamento ao mínimo necessário para a realização de suas finalidades. | A entidade `Usuario` **não coleta CPF, RG, endereço residencial, data de nascimento, gênero ou telefone pessoal**. Coleta apenas `nome`, `sobrenome` e `email` corporativo. |
| **IV e VI. Livre Acesso e Transparência** | Consulta facilitada e informações claras sobre o tratamento. | Tela de `Configurações` (`/configuracoes`) exibe claramente os dados cadastrais do titular, seu perfil (`ADMINISTRADOR`, `EDITOR`, `VISUALIZADOR`) e o status do Termo LGPD assinado. |
| **VII e VIII. Segurança e Prevenção** | Medidas técnicas e administrativas aptas a proteger os dados pessoais de acessos não autorizados. | Hash de senha com **BCrypt** (`RNF01`), controle de acesso baseado em papéis (**RBAC** via `PerfilUsuario`), proteção contra **CSRF** nos formulários Thymeleaf e **isolamento multi-tenant** estrito por `empresa_id` (`RN04`). |
| **X. Responsabilização e Prestação de Contas** | Demonstração da adoção de medidas eficazes e capazes de comprovar a observância das normas. | Entidade dedicada `TermoConsentimentoLgpd` (`tb_termo_consentimento_lgpd`) + módulo de Compliance com template nativo de PSI e Controle de Acesso alinhados à LGPD. |

---

## 4. Especificação Técnica da Entidade `TermoConsentimentoLgpd` e Fluxo `RN05`

Para atender ao **Art. 8º, § 2º da LGPD** (*"Cabe ao controlador o ônus da prova de que o consentimento foi obtido em conformidade com o disposto nesta Lei"*), o sistema implementa a tabela `tb_termo_consentimento_lgpd`:

```java
// src/main/java/br/edu/securedesk/entity/core/TermoConsentimentoLgpd.java
@Entity
@Table(name = "tb_termo_consentimento_lgpd")
public class TermoConsentimentoLgpd {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @Column(nullable = false, length = 20)
    private String versaoTermo; // Ex: "v1.0-2026"
    @Column(nullable = false)
    private Boolean aceito = true;
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAceite = LocalDateTime.now();
    @Column(length = 45)
    private String ipOrigem; // Suporta IPv4 e IPv6
}
```

### Fluxo de Controle de Consentimento e Direitos do Titular (Art. 18):
1. **Onboarding / 1º Login (`RN05`):** Ao autenticar-se, o sistema verifica se existe registro em `tb_termo_consentimento_lgpd` para `usuario.id` com a `versaoTermo` vigente e `aceito = true`. Caso não exista, exibe o modal/tela do Termo de Consentimento explicando em linguagem simples os dados coletados (`nome`, `email`, `IP`).
2. **Direito de Correção (Art. 18, III):** O titular pode atualizar seu nome e e-mail na tela `/configuracoes`.
3. **Direito de Revogação e Eliminação/Anonimização (Art. 8º, § 5º e Art. 18, VI):** Caso um usuário revogue o consentimento ou desligue-se da PME, o `ADMINISTRADOR` pode inativar a conta (`ativo = false`) ou acionar a rotina de **anonimização** (substituindo `nome = "Usuário Anonimizado"`, `sobrenome = null`, `email = "anon_" + id + "@removed.local"`), preservando a integridade referencial dos diagnósticos históricos da empresa (`tb_diagnostico` e `tb_historico_revisao_politica`) sem reter dados pessoais do ex-colaborador.

---

## 5. Resumo Executivo para Inclusão na Monografia (OEM 6a)

| Requisito da LGPD | Artigo(s) | Solução Adotada no SecureDesk | Requisito / Classe do Sistema |
|---|---|---|---|
| Minimização de dados coletados | Art. 6º, III | Coleta restrita a nome, sobrenome e e-mail corporativo (sem CPF/endereço pessoal) | `Usuario.java` / `RNF02` |
| Prova auditável de consentimento | Art. 7º, I e Art. 8º, § 2º | Gravação imutável de `versao_termo`, `data_aceite` e `ip_origem` (IPv4/IPv6) | `TermoConsentimentoLgpd.java` / `RF04` / `RN05` |
| Segurança técnica das credenciais | Art. 46 | Senhas criptografadas com hash adaptativo **BCrypt** (jamais em texto claro) | `Usuario.senha` / `RNF01` |
| Segregação de acesso e confidencialidade | Art. 46 e 49 | Perfis RBAC (`ADMINISTRADOR`, `EDITOR`, `VISUALIZADOR`) + isolamento por `empresa_id` | `PerfilUsuario.java` / `RN04` |
| Governança e Boas Práticas na PME | Art. 50 | Templates prontos de PSI, Controle de Acesso e Backup com cláusulas de proteção de dados | `TemplatePolitica.java` / `RF17` (OED 2) |
