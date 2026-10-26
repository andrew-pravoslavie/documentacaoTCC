package br.edu.securedesk.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes automatizados com Spring Boot Test + MockMvc para validar o protótipo navegável
 * (Fase 2 do plano de desenvolvimento do SecureDesk).
 * Garante que todas as 8 telas retornam HTTP 200 e que o fluxo de autenticação redireciona para o dashboard.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET / deve retornar HTTP 200 e renderizar landing page (core/index)")
    void testGetIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("core/index"))
                .andExpect(content().string(containsString("SecureDesk")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    @DisplayName("GET /login deve retornar HTTP 200 e renderizar tela de login completa com campos de email e senha (core/login)")
    void testGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("core/login"))
                .andExpect(content().string(containsString("SecureDesk")))
                .andExpect(content().string(containsString("id=\"loginForm\"")))
                .andExpect(content().string(containsString("id=\"login-email\"")))
                .andExpect(content().string(containsString("id=\"login-password\"")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    @DisplayName("POST /login deve redirecionar para /dashboard (HTTP 302)")
    void testPostLogin() throws Exception {
        mockMvc.perform(post("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @DisplayName("GET /dashboard deve retornar HTTP 200 e renderizar dashboard com dados da PME (core/dashboard)")
    void testGetDashboard() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("core/dashboard"))
                .andExpect(model().attributeExists("empresaNome", "scoreGlobalFormatado", "totalAtivos", "milMedio"))
                .andExpect(content().string(containsString("InnovaTech Soluções")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    @DisplayName("GET /diagnostico deve retornar HTTP 200 e renderizar questionário NIST/MIL (diagnostico/diagnostico)")
    void testGetDiagnostico() throws Exception {
        mockMvc.perform(get("/diagnostico"))
                .andExpect(status().isOk())
                .andExpect(view().name("diagnostico/diagnostico"))
                .andExpect(content().string(containsString("Diagnóstico de Maturidade")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    @DisplayName("GET /acompanhamento deve retornar HTTP 200 e renderizar radar NIST x MIL (diagnostico/acompanhamento)")
    void testGetAcompanhamento() throws Exception {
        mockMvc.perform(get("/acompanhamento"))
                .andExpect(status().isOk())
                .andExpect(view().name("diagnostico/acompanhamento"))
                .andExpect(model().attributeExists("scoreGvMil", "scoreIdMil", "scorePrMil", "scoreDeMil", "scoreRsMil", "scoreRcMil"))
                .andExpect(content().string(containsString("Radar de Maturidade NIST")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    @DisplayName("GET /governanca deve retornar HTTP 200 e renderizar kanban de remediação (governanca/governanca)")
    void testGetGovernanca() throws Exception {
        mockMvc.perform(get("/governanca"))
                .andExpect(status().isOk())
                .andExpect(view().name("governanca/governanca"))
                .andExpect(model().attributeExists("tarefasPendentes", "tarefasConcluidas"))
                .andExpect(content().string(containsString("Governança Operacional")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    @DisplayName("GET /compliance deve retornar HTTP 200 e renderizar repositório de políticas (compliance/compliance)")
    void testGetCompliance() throws Exception {
        mockMvc.perform(get("/compliance"))
                .andExpect(status().isOk())
                .andExpect(view().name("compliance/compliance"))
                .andExpect(content().string(containsString("Compliance e Políticas")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    @DisplayName("GET /configuracoes deve retornar HTTP 200 e renderizar configurações (core/configuracoes)")
    void testGetConfiguracoes() throws Exception {
        mockMvc.perform(get("/configuracoes"))
                .andExpect(status().isOk())
                .andExpect(view().name("core/configuracoes"))
                .andExpect(model().attributeExists("empresaNome", "totalAtivos", "trava100Ativos"))
                .andExpect(content().string(containsString("Configurações")))
                .andExpect(content().string(containsString("</html>")));
    }
}
