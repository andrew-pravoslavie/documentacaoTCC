package br.edu.securedesk.ativo.adapter.controller;

import br.edu.securedesk.ativo.domain.TipoAtivo;
import br.edu.securedesk.ativo.infrastructure.persistence.JpaAtivoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração WebMvc (MockMvc) para o AtivoController (/ativos).
 * Valida HTTP 200/302, bindings do formulário Thymeleaf e comportamento da trava de 100 ativos (RN01).
 */
@SpringBootTest
@AutoConfigureMockMvc
class AtivoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaAtivoRepository jpaAtivoRepository;

    @BeforeEach
    void setUp() {
        jpaAtivoRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /ativos deve retornar HTTP 200 e exibir a tela de inventário com cota 0/100")
    void deveListarAtivosComStatus200() throws Exception {
        mockMvc.perform(get("/ativos"))
                .andExpect(status().isOk())
                .andExpect(view().name("ativo/ativos"))
                .andExpect(model().attributeExists("ativos", "ativoForm", "totalAtivos", "limiteAtivos", "ativosTexto"))
                .andExpect(model().attribute("ativosTexto", "0/100"))
                .andExpect(content().string(containsString("Inventário do Pequeno Parque Tecnológico")));
    }

    @Test
    @DisplayName("POST /ativos válido dentro do limite deve salvar ativo e redirecionar (HTTP 302) com flash de sucesso")
    void deveCadastrarAtivoValidoERedirecionar302() throws Exception {
        mockMvc.perform(post("/ativos")
                        .param("nome", "Notebooks Dell Latitude")
                        .param("tipo", TipoAtivo.ENDPOINTS.name())
                        .param("descricao", "Windows 11 Pro")
                        .param("quantidade", "40")
                        .param("localizacao", "Escritório Central")
                        .param("responsavel", "Gabriel Andrew")
                        .param("ativo", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ativos"))
                .andExpect(flash().attributeExists("sucesso"));

        mockMvc.perform(get("/ativos"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalAtivos", 40))
                .andExpect(model().attribute("ativosTexto", "40/100"))
                .andExpect(content().string(containsString("Notebooks Dell Latitude")));
    }

    @Test
    @DisplayName("POST /ativos que ultrapassa 100 unidades deve rejeitar com flash de erro (RN01) e manter cota anterior")
    void deveRejeitarCadastroQueUltrapassaTrava100Ativos() throws Exception {
        // 1º cadastro: 80 ativos
        mockMvc.perform(post("/ativos")
                        .param("nome", "Estações de Trabalho")
                        .param("tipo", TipoAtivo.ENDPOINTS.name())
                        .param("quantidade", "80")
                        .param("ativo", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection());

        // 2º cadastro: +21 ativos (total = 101 -> bloqueado pela RN01)
        mockMvc.perform(post("/ativos")
                        .param("nome", "Servidores Extras")
                        .param("tipo", TipoAtivo.SERVIDORES_LOCAIS.name())
                        .param("quantidade", "21")
                        .param("ativo", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ativos"))
                .andExpect(flash().attributeExists("erro"));

        mockMvc.perform(get("/ativos"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalAtivos", 80));
    }

    @Test
    @DisplayName("POST /ativos com nome em branco deve retornar HTTP 200 na view com erros de Bean Validation")
    void deveRetornarErrosValidacaoQuandoNomeEmBranco() throws Exception {
        mockMvc.perform(post("/ativos")
                        .param("nome", "")
                        .param("tipo", TipoAtivo.ATIVOS_REDE.name())
                        .param("quantidade", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("ativo/ativos"))
                .andExpect(model().attributeHasFieldErrors("ativoForm", "nome"));
    }

    @Test
    @DisplayName("POST /ativos/{id}/inativar deve inativar ativo e liberar cota (HTTP 302)")
    void deveInativarAtivoELiberarCota() throws Exception {
        mockMvc.perform(post("/ativos")
                        .param("nome", "Switch Core Cisco")
                        .param("tipo", TipoAtivo.ATIVOS_REDE.name())
                        .param("quantidade", "15")
                        .param("ativo", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection());

        Long idCriado = jpaAtivoRepository.findAll().getFirst().getId();

        mockMvc.perform(post("/ativos/" + idCriado + "/inativar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ativos"))
                .andExpect(flash().attributeExists("sucesso"));

        mockMvc.perform(get("/ativos"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalAtivos", 0));
    }
}
