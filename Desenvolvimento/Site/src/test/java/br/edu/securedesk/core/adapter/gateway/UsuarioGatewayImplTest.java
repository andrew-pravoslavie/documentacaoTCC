package br.edu.securedesk.core.adapter.gateway;

import br.edu.securedesk.core.domain.PerfilUsuario;
import br.edu.securedesk.core.domain.Usuario;
import br.edu.securedesk.core.infrastructure.persistence.JpaUsuarioRepository;
import br.edu.securedesk.core.infrastructure.persistence.UsuarioDataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioGatewayImplTest {

    @Mock
    private JpaUsuarioRepository jpaUsuarioRepository;

    @InjectMocks
    private UsuarioGatewayImpl usuarioGateway;

    private UsuarioDataMapper sampleMapper;
    private Usuario sampleDomain;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        sampleMapper = UsuarioDataMapper.builder()
                .id(1L)
                .empresaId(10L)
                .nome("Carlos")
                .sobrenome("Silva")
                .email("carlos@pme.com.br")
                .senhaHash("$2a$10$hashedPasswordSample")
                .perfil(PerfilUsuario.ADMINISTRADOR)
                .ativo(true)
                .consentimentoLgpdAceito(true)
                .dataCadastro(now)
                .build();

        sampleDomain = new Usuario(
                1L,
                10L,
                "Carlos",
                "Silva",
                "carlos@pme.com.br",
                "$2a$10$hashedPasswordSample",
                PerfilUsuario.ADMINISTRADOR,
                true,
                true,
                now
        );
    }

    @Test
    @DisplayName("Deve mapear corretamente de UsuarioDataMapper para Usuario (toDomain)")
    void toDomain_sucesso() {
        Usuario domain = UsuarioGatewayImpl.toDomain(sampleMapper);

        assertNotNull(domain);
        assertEquals(sampleMapper.getId(), domain.getId());
        assertEquals(sampleMapper.getEmpresaId(), domain.getEmpresaId());
        assertEquals(sampleMapper.getNome(), domain.getNome());
        assertEquals(sampleMapper.getSobrenome(), domain.getSobrenome());
        assertEquals(sampleMapper.getEmail(), domain.getEmail());
        assertEquals(sampleMapper.getSenhaHash(), domain.getSenhaHash());
        assertEquals(sampleMapper.getPerfil(), domain.getPerfil());
        assertEquals(sampleMapper.isAtivo(), domain.isAtivo());
        assertEquals(sampleMapper.isConsentimentoLgpdAceito(), domain.isConsentimentoLgpdAceito());
        assertEquals(sampleMapper.getDataCadastro(), domain.getDataCadastro());
    }

    @Test
    @DisplayName("toDomain com null deve retornar null")
    void toDomain_null() {
        assertNull(UsuarioGatewayImpl.toDomain(null));
    }

    @Test
    @DisplayName("Deve mapear corretamente de Usuario para UsuarioDataMapper (toDataMapper)")
    void toDataMapper_sucesso() {
        UsuarioDataMapper mapper = UsuarioGatewayImpl.toDataMapper(sampleDomain);

        assertNotNull(mapper);
        assertEquals(sampleDomain.getId(), mapper.getId());
        assertEquals(sampleDomain.getEmpresaId(), mapper.getEmpresaId());
        assertEquals(sampleDomain.getNome(), mapper.getNome());
        assertEquals(sampleDomain.getSobrenome(), mapper.getSobrenome());
        assertEquals(sampleDomain.getEmail(), mapper.getEmail());
        assertEquals(sampleDomain.getSenhaHash(), mapper.getSenhaHash());
        assertEquals(sampleDomain.getPerfil(), mapper.getPerfil());
        assertEquals(sampleDomain.isAtivo(), mapper.isAtivo());
        assertEquals(sampleDomain.isConsentimentoLgpdAceito(), mapper.isConsentimentoLgpdAceito());
        assertEquals(sampleDomain.getDataCadastro(), mapper.getDataCadastro());
    }

    @Test
    @DisplayName("toDataMapper com null deve retornar null")
    void toDataMapper_null() {
        assertNull(UsuarioGatewayImpl.toDataMapper(null));
    }

    @Test
    @DisplayName("salvar deve persistir via repositório e retornar domínio")
    void salvar_novo() {
        when(jpaUsuarioRepository.save(any(UsuarioDataMapper.class))).thenReturn(sampleMapper);

        Usuario salvo = usuarioGateway.salvar(sampleDomain);

        assertNotNull(salvo);
        assertEquals(1L, salvo.getId());
        verify(jpaUsuarioRepository, times(1)).save(any(UsuarioDataMapper.class));
    }

    @Test
    @DisplayName("buscarPorId deve retornar Optional com domínio")
    void buscarPorId_existente() {
        when(jpaUsuarioRepository.findById(1L)).thenReturn(Optional.of(sampleMapper));

        Optional<Usuario> opt = usuarioGateway.buscarPorId(1L);

        assertTrue(opt.isPresent());
        assertEquals("Carlos", opt.get().getNome());
    }

    @Test
    @DisplayName("buscarPorEmail deve retornar Optional com domínio se encontrado")
    void buscarPorEmail_existente() {
        when(jpaUsuarioRepository.findByEmailIgnoreCase("carlos@pme.com.br")).thenReturn(Optional.of(sampleMapper));

        Optional<Usuario> opt = usuarioGateway.buscarPorEmail("carlos@pme.com.br");

        assertTrue(opt.isPresent());
        assertEquals("carlos@pme.com.br", opt.get().getEmail());
    }

    @Test
    @DisplayName("buscarPorEmail com null retorna Optional vazio")
    void buscarPorEmail_null() {
        Optional<Usuario> opt = usuarioGateway.buscarPorEmail(null);
        assertTrue(opt.isEmpty());
        verify(jpaUsuarioRepository, never()).findByEmailIgnoreCase(any());
    }

    @Test
    @DisplayName("listarPorEmpresa deve retornar lista ordenada por ID decrescente")
    void listarPorEmpresa() {
        when(jpaUsuarioRepository.findByEmpresaIdOrderByIdDesc(10L)).thenReturn(List.of(sampleMapper));

        List<Usuario> lista = usuarioGateway.listarPorEmpresa(10L);

        assertEquals(1, lista.size());
        assertEquals("carlos@pme.com.br", lista.get(0).getEmail());
    }

    @Test
    @DisplayName("existePorEmail deve consultar repositório")
    void existePorEmail() {
        when(jpaUsuarioRepository.existsByEmailIgnoreCase("carlos@pme.com.br")).thenReturn(true);

        boolean existe = usuarioGateway.existePorEmail("carlos@pme.com.br");

        assertTrue(existe);
    }

    @Test
    @DisplayName("removerPorId deve delegar para deleteById do repositório")
    void removerPorId() {
        doNothing().when(jpaUsuarioRepository).deleteById(1L);

        usuarioGateway.removerPorId(1L);

        verify(jpaUsuarioRepository, times(1)).deleteById(1L);
    }
}
