package br.edu.securedesk.core.adapter.controller;

import br.edu.securedesk.core.domain.PerfilUsuario;
import br.edu.securedesk.core.infrastructure.persistence.JpaUsuarioRepository;
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
 * Testes de integração WebMvc (MockMvc) para o UsuarioController (/configuracoes).
 * Valida HTTP 200/302, formulário Thymeleaf, atributos do modelo da PME e CRUD 2 de Usuários.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaUsuarioRepository jpaUsuarioRepository;

    @BeforeEach
    void setUp() {
        jpaUsuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /configuracoes deve retornar HTTP 200 com atributos da PME e formulário padrão")
    void deveExibirConfiguracoesComStatus200() throws Exception {
        mockMvc.perform(get("/configuracoes"))
                .andExpect(status().isOk())
                .andExpect(view().name("core/configuracoes"))
                .andExpect(model().attributeExists("empresaNome", "totalAtivos", "trava100Ativos", "usuarios", "usuarioForm", "perfisUsuario"))
                .andExpect(content().string(containsString("Gerenciamento de Usuários")));
    }

    @Test
    @DisplayName("POST /configuracoes/usuarios válido deve cadastrar colaborador e redirecionar com flash de sucesso")
    void deveCadastrarUsuarioValidoERedirecionar() throws Exception {
        mockMvc.perform(post("/configuracoes/usuarios")
                        .param("nome", "Lucas")
                        .param("sobrenome", "Oliveira")
                        .param("email", "lucas.oliveira@innovatech.com.br")
                        .param("senha", "Segredo@123")
                        .param("perfil", PerfilUsuario.EDITOR.name())
                        .param("ativo", "true")
                        .param("consentimentoLgpd", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracoes"))
                .andExpect(flash().attributeExists("sucesso"));

        mockMvc.perform(get("/configuracoes"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Lucas Oliveira")))
                .andExpect(content().string(containsString("lucas.oliveira@innovatech.com.br")));
    }

    @Test
    @DisplayName("POST /configuracoes/usuarios com campos inválidos deve reexibir view com erros de Bean Validation")
    void deveRetornarErrosValidacaoQuandoNomeEmailEmBranco() throws Exception {
        mockMvc.perform(post("/configuracoes/usuarios")
                        .param("nome", "")
                        .param("email", "invalido")
                        .param("senha", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("core/configuracoes"))
                .andExpect(model().attributeHasFieldErrors("usuarioForm", "nome", "email", "senha"));
    }

    @Test
    @DisplayName("GET /configuracoes/usuarios/{id}/editar deve carregar dados no formulário e definir usuarioEmEdicaoId")
    void deveCarregarFormularioEdicao() throws Exception {
        mockMvc.perform(post("/configuracoes/usuarios")
                        .param("nome", "Rafael")
                        .param("sobrenome", "Melo")
                        .param("email", "rafael.melo@innovatech.com.br")
                        .param("senha", "Senha@123")
                        .param("perfil", PerfilUsuario.VISUALIZADOR.name())
                        .param("ativo", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection());

        Long idCriado = jpaUsuarioRepository.findAll().getFirst().getId();

        mockMvc.perform(get("/configuracoes/usuarios/" + idCriado + "/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("core/configuracoes"))
                .andExpect(model().attribute("usuarioEmEdicaoId", idCriado))
                .andExpect(content().string(containsString("Editar Colaborador #" + idCriado)));
    }

    @Test
    @DisplayName("POST /configuracoes/usuarios/{id} deve atualizar colaborador com sucesso")
    void deveAtualizarUsuarioERedirecionar() throws Exception {
        mockMvc.perform(post("/configuracoes/usuarios")
                        .param("nome", "Bruno")
                        .param("sobrenome", "Calvacante")
                        .param("email", "bruno@innovatech.com.br")
                        .param("senha", "Senha@123")
                        .param("perfil", PerfilUsuario.EDITOR.name())
                        .param("ativo", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection());

        Long idCriado = jpaUsuarioRepository.findAll().getFirst().getId();

        mockMvc.perform(post("/configuracoes/usuarios/" + idCriado)
                        .param("nome", "Bruno Atualizado")
                        .param("sobrenome", "Calvacante")
                        .param("email", "bruno.novo@innovatech.com.br")
                        .param("senha", "NovaSenha@123")
                        .param("perfil", PerfilUsuario.ADMINISTRADOR.name())
                        .param("ativo", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracoes"))
                .andExpect(flash().attributeExists("sucesso"));

        mockMvc.perform(get("/configuracoes"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bruno Atualizado")));
    }

    @Test
    @DisplayName("POST /configuracoes/usuarios/{id}/status deve alternar ativo/inativo e redirecionar")
    void deveAlternarStatusUsuario() throws Exception {
        mockMvc.perform(post("/configuracoes/usuarios")
                        .param("nome", "Carla")
                        .param("sobrenome", "Silva")
                        .param("email", "carla@innovatech.com.br")
                        .param("senha", "Senha@123")
                        .param("perfil", PerfilUsuario.EDITOR.name())
                        .param("ativo", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection());

        Long idCriado = jpaUsuarioRepository.findAll().getFirst().getId();

        mockMvc.perform(post("/configuracoes/usuarios/" + idCriado + "/status"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracoes"))
                .andExpect(flash().attributeExists("sucesso"));

        UsuarioControllerTest.this.mockMvc.perform(get("/configuracoes"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Inativo")));
    }

    @Test
    @DisplayName("POST /configuracoes/usuarios/{id}/excluir deve remover o usuário do banco")
    void deveExcluirUsuario() throws Exception {
        mockMvc.perform(post("/configuracoes/usuarios")
                        .param("nome", "Temporario")
                        .param("sobrenome", "User")
                        .param("email", "temp@innovatech.com.br")
                        .param("senha", "Senha@123")
                        .param("perfil", PerfilUsuario.VISUALIZADOR.name())
                        .param("ativo", "true")
                        .param("empresaId", "1"))
                .andExpect(status().is3xxRedirection());

        Long idCriado = jpaUsuarioRepository.findAll().getFirst().getId();

        mockMvc.perform(post("/configuracoes/usuarios/" + idCriado + "/excluir"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configuracoes"))
                .andExpect(flash().attributeExists("sucesso"));

        org.junit.jupiter.api.Assertions.assertTrue(jpaUsuarioRepository.findById(idCriado).isEmpty());
    }
}
