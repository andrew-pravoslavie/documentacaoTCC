package br.edu.securedesk.governanca.adapter.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração (MockMvc + H2) do GovernancaController: Kanban de tarefas e evidências (CRUD 3).
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "securedesk.uploads.dir=target/test-uploads")
class GovernancaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Long criarTarefa(String titulo, String status) throws Exception {
        mockMvc.perform(post("/governanca")
                        .param("titulo", titulo)
                        .param("funcaoNistSigla", "DE")
                        .param("prioridade", "CRITICA")
                        .param("status", status)
                        .param("responsavelNome", "Gabriel Andrew")
                        .param("prazoLimite", "2030-01-15"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/governanca"))
                .andExpect(flash().attributeExists("sucesso"));
        String html = mockMvc.perform(get("/governanca")).andReturn().getResponse().getContentAsString();
        int posTitulo = html.indexOf(titulo);
        int posAcao = html.indexOf("/governanca/", posTitulo);
        String trecho = html.substring(posAcao + "/governanca/".length());
        return Long.valueOf(trecho.substring(0, trecho.indexOf('/')));
    }

    @Test
    @DisplayName("GET /governanca renderiza o Kanban vazio com contadores e colunas")
    void deveRenderizarKanban() throws Exception {
        mockMvc.perform(get("/governanca"))
                .andExpect(status().isOk())
                .andExpect(view().name("governanca/governanca"))
                .andExpect(model().attributeExists("tarefaForm", "pendentes", "emAndamento", "concluidas",
                        "tarefasPendentes", "tarefasConcluidas"))
                .andExpect(content().string(containsString("Governança Operacional")));
    }

    @Test
    @DisplayName("POST /governanca sem título reapresenta o formulário com erro de validação")
    void deveValidarTituloObrigatorio() throws Exception {
        mockMvc.perform(post("/governanca").param("titulo", "").param("funcaoNistSigla", "PR").param("prioridade", "ALTA"))
                .andExpect(status().isOk())
                .andExpect(view().name("governanca/governanca"))
                .andExpect(model().attributeHasFieldErrors("tarefaForm", "titulo"))
                .andExpect(content().string(containsString("O título da tarefa é obrigatório.")));
    }

    @Test
    @DisplayName("Ciclo completo: criar, mover no Kanban, editar e excluir uma tarefa")
    void deveExecutarCicloDaTarefa() throws Exception {
        Long id = criarTarefa("Tarefa ciclo Kanban", "BACKLOG");

        mockMvc.perform(get("/governanca"))
                .andExpect(content().string(containsString("Tarefa ciclo Kanban")));

        mockMvc.perform(post("/governanca/{id}/status", id).param("status", "EM_ANDAMENTO"))
                .andExpect(redirectedUrl("/governanca"))
                .andExpect(flash().attribute("sucesso", containsString("Em Andamento")));

        mockMvc.perform(get("/governanca/{id}/editar", id))
                .andExpect(status().isOk())
                .andExpect(model().attribute("tarefaEmEdicaoId", id))
                .andExpect(content().string(containsString("Editar Tarefa #" + id)));

        mockMvc.perform(post("/governanca/{id}", id)
                        .param("titulo", "Tarefa ciclo Kanban editada")
                        .param("funcaoNistSigla", "RS")
                        .param("prioridade", "ALTA")
                        .param("status", "CONCLUIDO"))
                .andExpect(redirectedUrl("/governanca"))
                .andExpect(flash().attributeExists("sucesso"));

        mockMvc.perform(get("/governanca"))
                .andExpect(content().string(containsString("Tarefa ciclo Kanban editada")));

        mockMvc.perform(post("/governanca/{id}/excluir", id))
                .andExpect(redirectedUrl("/governanca"))
                .andExpect(flash().attributeExists("sucesso"));

        mockMvc.perform(get("/governanca/{id}/editar", id))
                .andExpect(redirectedUrl("/governanca"))
                .andExpect(flash().attributeExists("erro"));
    }

    @Test
    @DisplayName("Upload de evidência grava o arquivo, registra metadados e remove o arquivo ao excluir a tarefa")
    void deveAnexarEvidenciaPorTarefa() throws Exception {
        Long id = criarTarefa("Tarefa com evidencia", "EM_ANDAMENTO");
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", "../laudo mfa.pdf", "application/pdf", "conteudo".getBytes());

        mockMvc.perform(multipart("/governanca/{id}/evidencias", id).file(arquivo).param("descricao", "Print do MFA ativo"))
                .andExpect(redirectedUrl("/governanca"))
                .andExpect(flash().attribute("sucesso", containsString("tarefa" + id + "_laudo mfa.pdf")));

        Path gravado = Paths.get("target/test-uploads", "tarefa" + id + "_laudo mfa.pdf");
        assertTrue(Files.exists(gravado), "arquivo de evidência deve existir em disco");

        mockMvc.perform(get("/governanca"))
                .andExpect(content().string(containsString("tarefa" + id + "_laudo mfa.pdf")))
                .andExpect(content().string(containsString("Print do MFA ativo")));

        mockMvc.perform(multipart("/governanca/{id}/evidencias", id).file(arquivo))
                .andExpect(flash().attribute("erro", containsString("Já existe uma evidência")));

        mockMvc.perform(post("/governanca/{id}/excluir", id)).andExpect(flash().attributeExists("sucesso"));
        assertFalse(Files.exists(gravado), "arquivo deve ser removido junto com a tarefa");
    }

    @Test
    @DisplayName("Upload sem arquivo ou para tarefa inexistente retorna mensagem de erro via Flash Attribute")
    void deveRejeitarUploadInvalido() throws Exception {
        Long id = criarTarefa("Tarefa upload vazio", "BACKLOG");

        mockMvc.perform(multipart("/governanca/{id}/evidencias", id)
                        .file(new MockMultipartFile("arquivo", "vazio.txt", "text/plain", new byte[0])))
                .andExpect(flash().attribute("erro", containsString("Selecione um arquivo")));

        mockMvc.perform(multipart("/governanca/{id}/evidencias", 999999L)
                        .file(new MockMultipartFile("arquivo", "x.txt", "text/plain", "x".getBytes())))
                .andExpect(flash().attribute("erro", containsString("não encontrada")));
    }

    @Test
    @DisplayName("Filtro por função NIST e por prioridade crítica é aplicado no servidor")
    void deveFiltrarKanban() throws Exception {
        criarTarefa("Tarefa filtro RC", "BACKLOG");
        mockMvc.perform(get("/governanca").param("funcao", "GV"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("filtroFuncao", "GV"))
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("Tarefa filtro RC"))));
        mockMvc.perform(get("/governanca").param("prioridade", "critica"))
                .andExpect(content().string(containsString("Tarefa filtro RC")));
    }
}
