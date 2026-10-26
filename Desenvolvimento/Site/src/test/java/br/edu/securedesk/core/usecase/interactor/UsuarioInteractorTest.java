package br.edu.securedesk.core.usecase.interactor;

import br.edu.securedesk.core.domain.Usuario;
import br.edu.securedesk.core.domain.PerfilUsuario;
import br.edu.securedesk.core.usecase.boundary.output.UsuarioGateway;
import br.edu.securedesk.core.usecase.model.UsuarioRequestModel;
import br.edu.securedesk.core.usecase.model.UsuarioResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure unit tests for {@link UsuarioInteractor} covering registration, email uniqueness,
 * company isolation, updates, activation toggle and deletion.
 */
class UsuarioInteractorTest {

    private UsuarioInteractor interactor;
    private InMemoryUsuarioGateway gateway;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        gateway = new InMemoryUsuarioGateway();
        passwordEncoder = new BCryptPasswordEncoder();
        interactor = new UsuarioInteractor(gateway, passwordEncoder);
    }

    private UsuarioRequestModel req(Long empresaId, String email, String senha, boolean ativo) {
        return new UsuarioRequestModel(
                "Nome",
                "Sobrenome",
                email,
                senha,
                PerfilUsuario.ADMINISTRADOR,
                ativo,
                true, // consentimento LGPD
                empresaId
        );
    }

    @Test
    @DisplayName("1) Cadastro cria hash BCrypt e persiste usuário")
    void cadastrarCriaHash() {
        UsuarioResponseModel resp = interactor.cadastrar(req(1L, "user@example.com", "secret123", true));
        assertNotNull(resp.id());
        Usuario stored = gateway.buscarPorId(resp.id()).orElseThrow();
        assertTrue(passwordEncoder.matches("secret123", stored.getSenhaHash()));
    }

    @Test
    @DisplayName("2) Cadastro bloqueia e‑mail duplicado")
    void cadastrarEmailDuplicado() {
        interactor.cadastrar(req(1L, "dup@example.com", "pass123", true));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> interactor.cadastrar(req(2L, "dup@example.com", "pass456", true)));
        assertTrue(ex.getMessage().contains("Já existe um usuário cadastrado"));
    }

    @Test
    @DisplayName("3) Listar por empresa isolado por empresaId")
    void listarIsoladoPorEmpresa() {
        interactor.cadastrar(req(1L, "a@c1.com", "secret1", true));
        interactor.cadastrar(req(2L, "b@c2.com", "secret2", true));
        List<UsuarioResponseModel> c1 = interactor.listarPorEmpresa(1L);
        assertEquals(1, c1.size());
        assertEquals("a@c1.com", c1.get(0).email());
    }

    @Test
    @DisplayName("4) Atualizar preserva hash quando senha não informada")
    void atualizarMantemHash() {
        UsuarioResponseModel created = interactor.cadastrar(req(1L, "upd@example.com", "oldpass", true));
        UsuarioResponseModel updated = interactor.atualizar(created.id(), new UsuarioRequestModel(
                "NomeNovo",
                "SobrenomeNovo",
                "upd@example.com",
                null, // sem senha nova
                PerfilUsuario.EDITOR,
                true,
                true,
                1L
        ));
        Usuario stored = gateway.buscarPorId(updated.id()).orElseThrow();
        assertTrue(passwordEncoder.matches("oldpass", stored.getSenhaHash()));
        assertEquals("NomeNovo", stored.getNome());
        assertEquals(PerfilUsuario.EDITOR, stored.getPerfil());
    }

    @Test
    @DisplayName("5) Alternar status ativo/inativo")
    void alternarStatusAtivo() {
        UsuarioResponseModel created = interactor.cadastrar(req(1L, "toggle@example.com", "pass123", true));
        UsuarioResponseModel after = interactor.alternarStatusAtivo(created.id());
        assertFalse(after.ativo());
        after = interactor.alternarStatusAtivo(created.id());
        assertTrue(after.ativo());
    }

    @Test
    @DisplayName("6) Exclusão remove usuário")
    void excluirUsuario() {
        UsuarioResponseModel created = interactor.cadastrar(req(1L, "del@example.com", "pass123", true));
        interactor.excluir(created.id());
        assertTrue(gateway.buscarPorId(created.id()).isEmpty());
    }

    // ---------------------------------------------------------------------
    // In‑memory gateway implementation used by the tests
    // ---------------------------------------------------------------------
    private static class InMemoryUsuarioGateway implements UsuarioGateway {
        private final Map<Long, Usuario> store = new HashMap<>();
        private final AtomicLong idSeq = new AtomicLong(1);

        @Override
        public Usuario salvar(Usuario usuario) {
            if (usuario.getId() == null) {
                usuario.setId(idSeq.getAndIncrement());
            }
            store.put(usuario.getId(), usuario);
            return usuario;
        }

        @Override
        public Optional<Usuario> buscarPorId(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public Optional<Usuario> buscarPorEmail(String email) {
            return store.values().stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(email))
                    .findFirst();
        }

        @Override
        public List<Usuario> listarPorEmpresa(Long empresaId) {
            List<Usuario> list = new ArrayList<>();
            for (Usuario u : store.values()) {
                if (Objects.equals(u.getEmpresaId(), empresaId)) {
                    list.add(u);
                }
            }
            return list;
        }

        @Override
        public boolean existePorEmail(String email) {
            return store.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        }

        @Override
        public void removerPorId(Long id) {
            store.remove(id);
        }
    }
}
