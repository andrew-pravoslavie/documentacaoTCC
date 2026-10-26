# SecureDesk — Framework de Cibersegurança para Pequenos Parques Tecnológicos (PMEs)

Repositório oficial de documentação, monografia e código-fonte do Trabalho de Conclusão de Curso (TCC) em Análise e Desenvolvimento de Sistemas — **FATEC Zona Leste**.

## 👥 Equipe
- Bruno Cavalcante
- Gabriel Andrew
- Lucas Oliveira
- Rafael Melo

## 📁 Estrutura do Repositório

```text
documentacaoTCC/
├── Monografia/                     # Documentação conceitual e redação do TCC (Obsidian Vault)
│   ├── 00 - Gestão & Dashboard/   # Cronogramas, atas e gestão de equipe
│   ├── 01 - Pré-Projeto & Escopo/ # Justificativa, delimitação do pequeno parque (< 100 ativos)
│   ├── 02 - Referencial Teórico/  # NIST CSF 2.0, C2M2, ISO/IEC 27001/27002, PMEs
│   ├── 03 - Base de Conhecimento/ # Ativos de TI, ameaças, vulnerabilidades e pilares
│   ├── 04 - Framework & Modelo Híbrido/ # Arquitetura NIST CSF 2.0 × C2M2, alvo MIL 2
│   ├── 05 - Redação da Monografia/ # Capítulos do trabalho acadêmico (normas ABNT)
│   └── 99 - Anexos & Templates/   # Termos, modelos conceituais e fichamentos
│
└── Desenvolvimento/
    └── Site/                      # Aplicação Web (Spring Boot 4 / Java 21 / Thymeleaf)
        ├── docs/                  # Entregáveis formais de Engenharia de Software (TG1)
        │   ├── DER.md             # Diagrama Entidade-Relacionamento e Dicionário de Dados
        │   ├── REQUISITOS.md      # Especificação de Requisitos de Software (RF/RNF/Casos de Uso)
        │   ├── QUESTIONARIO_DIAGNOSTICO.md # 30 questões fechadas (NIST × C2M2 × ISO 27002)
        │   ├── ESPECIFICACAO_SCORE_C2M2.md # Fórmulas matemáticas do score cumulativo e planilha
        │   ├── TEMPLATES_POLITICAS.md # Minutas editáveis de PSI, Controle de Acesso e Backup 3-2-1
        │   ├── PLANO_TESTES.md    # Plano de testes unitários, integração e UAT
        │   ├── MAPEAMENTO_LGPD.md # Mapeamento técnico e legal (OEM 6a)
        │   ├── PLANO_DESENVOLVIMENTO.md # Planejamento faseado e registro de progresso
        │   └── DIARIO_CONSTRUCAO.md     # Histórico de arquitetura e decisões técnicas
        ├── src/                   # Código-fonte Java e templates Thymeleaf
        ├── pom.xml                # Configuração Maven e dependências
        └── tcc-projeto.html       # Apresentação executiva da proposta do TCC
```

## 🛠️ Stack Tecnológica (Sistema Web)
- **Backend:** Java 21 LTS, Spring Boot 4.1.1, Spring Data JPA, Spring Security
- **Banco de Dados:** H2 Database em memória (perfil `dev`) e MySQL/MariaDB (perfil `prod`)
- **Frontend:** Server-Side Rendering com Thymeleaf, Bootstrap 5 e Chart.js
- **Validação de Testes:** JUnit 5, Mockito e Spring MockMvc (`./mvnw test`)
