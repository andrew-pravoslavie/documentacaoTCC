package br.edu.securedesk.compliance.adapter.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração (MockMvc + H2) do ComplianceController: templates PSI/ACESSO/BACKUP,
 * revisão com histórico de versões e Flash Attributes (CRUD 4).
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ComplianceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Long idDaPolitica(String titulo) throws Exception {
        String html = mockMvc.perform(get("/compliance")).andReturn().getResponse().getContentAsString();
        int secaoPoliticas = html.indexOf("Políticas da Empresa</div>");
        Matcher link = Pattern.compile("/compliance/(\\d+)\"")
                .matcher(html.substring(html.indexOf(titulo, secaoPoliticas)));
        link.find();
        return Long.valueOf(link.group(1));
    }

    @Test
    @Order(1)
    @DisplayName("GET /compliance renderiza templates oficiais e estatísticas")
    void deveRenderizarCompliance() throws Exception {
        mockMvc.perform(get("/compliance"))
                .andExpect(status().isOk())
                .andExpect(view().name("compliance/compliance"))
                .andExpect(model().attributeExists("politicaForm", "templates", "politicas", "totalTemplates"))
                .andExpect(content().string(containsString("Compliance e Políticas")))
                .andExpect(content().string(containsString("Política de Backup e Recuperação (Regra 3-2-1)")));
    }

    @Test
    @Order(2)
    @DisplayName("Adotar template BACKUP cria política v1.0 e bloqueia adoção duplicada")
    void deveAdotarTemplateUmaVez() throws Exception {
        mockMvc.perform(post("/compliance/templates/BACKUP/adotar"))
                .andExpect(redirectedUrl("/compliance"))
                .andExpect(flash().attribute("sucesso", containsString("versão 1.0")));

        mockMvc.perform(get("/compliance"))
                .andExpect(content().string(containsString("Adotado")))
                .andExpect(content().string(containsString("Regra 3-2-1")));

        mockMvc.perform(post("/compliance/templates/BACKUP/adotar"))
                .andExpect(flash().attribute("erro", containsString("já foi adotado")));

        mockMvc.perform(post("/compliance/templates/INEXISTENTE/adotar"))
                .andExpect(flash().attribute("erro", containsString("desconhecido")));
    }

    @Test
    @Order(3)
    @DisplayName("Revisar política incrementa a versão e exibe o histórico de versões")
    void deveRevisarEMostrarHistorico() throws Exception {
        mockMvc.perform(post("/compliance/templates/PSI/adotar")).andExpect(flash().attributeExists("sucesso"));
        Long id = idDaPolitica("Política de Segurança da Informação (PSI)");

        mockMvc.perform(get("/compliance/{id}/editar", id))
                .andExpect(status().isOk())
                .andExpect(model().attribute("politicaEmEdicaoId", id))
                .andExpect(content().string(containsString("Revisar Política #" + id)));

        mockMvc.perform(post("/compliance/{id}/revisar", id)
                        .param("titulo", "Política de Segurança da Informação (PSI)")
                        .param("conteudoCustomizado", "1. OBJETIVO\nTexto revisado pela direção.")
                        .param("periodicidadeMesesRevisao", "12")
                        .param("revisorNome", "Bruno Calvacante")
                        .param("observacoesRevisao", "Inclusão de cláusula LGPD"))
                .andExpect(redirectedUrl("/compliance"))
                .andExpect(flash().attribute("sucesso", containsString("nova versão 1.1")));

        mockMvc.perform(get("/compliance/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Texto revisado pela direção.")))
                .andExpect(content().string(containsString("Inclusão de cláusula LGPD")))
                .andExpect(content().string(containsString("Bruno Calvacante")))
                .andExpect(content().string(containsString("v1.1")))
                .andExpect(content().string(containsString("v1.0")));
    }

    @Test
    @Order(4)
    @DisplayName("Criar política personalizada valida conteúdo e permite excluir com histórico")
    void deveCriarEExcluirPoliticaPersonalizada() throws Exception {
        mockMvc.perform(post("/compliance").param("titulo", "Política X").param("conteudoCustomizado", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("politicaForm", "conteudoCustomizado"))
                .andExpect(content().string(containsString("O conteúdo da política não pode ficar em branco.")));

        mockMvc.perform(post("/compliance")
                        .param("titulo", "Política de Uso de Dispositivos Móveis")
                        .param("conteudoCustomizado", "1. OBJETIVO\nRegular BYOD.")
                        .param("status", "EM_ELABORACAO")
                        .param("periodicidadeMesesRevisao", "6"))
                .andExpect(redirectedUrl("/compliance"))
                .andExpect(flash().attributeExists("sucesso"));

        Long id = idDaPolitica("Política de Uso de Dispositivos Móveis");
        mockMvc.perform(post("/compliance/{id}/excluir", id))
                .andExpect(flash().attributeExists("sucesso"));
        mockMvc.perform(get("/compliance/{id}", id))
                .andExpect(redirectedUrl("/compliance"))
                .andExpect(flash().attributeExists("erro"));
    }
}
