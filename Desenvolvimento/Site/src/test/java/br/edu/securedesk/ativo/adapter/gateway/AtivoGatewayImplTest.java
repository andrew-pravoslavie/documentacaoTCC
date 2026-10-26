package br.edu.securedesk.ativo.adapter.gateway;

import br.edu.securedesk.ativo.domain.Ativo;
import br.edu.securedesk.ativo.domain.TipoAtivo;
import br.edu.securedesk.ativo.infrastructure.persistence.AtivoDataMapper;
import br.edu.securedesk.ativo.infrastructure.persistence.JpaAtivoRepository;
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
class AtivoGatewayImplTest {

    @Mock
    private JpaAtivoRepository jpaAtivoRepository;

    @InjectMocks
    private AtivoGatewayImpl ativoGateway;

    private AtivoDataMapper sampleMapper;
    private Ativo sampleDomain;

    @BeforeEach
    void setUp() {
        sampleMapper = AtivoDataMapper.builder()
                .id(1L)
                .empresaId(10L)
                .nome("Servidor AD")
                .tipo(TipoAtivo.SERVIDORES_LOCAIS)
                .descricao("Controlador de domínio principal")
                .quantidade(2)
                .localizacao("Rack TI 01")
                .responsavel("João Silva")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build();

        sampleDomain = new Ativo(
                1L,
                "Servidor AD",
                TipoAtivo.SERVIDORES_LOCAIS,
                "Controlador de domínio principal",
                2,
                "Rack TI 01",
                "João Silva",
                true,
                10L
        );
    }

    @Test
    @DisplayName("Deve mapear corretamente de AtivoDataMapper para Ativo (toDomain)")
    void toDomain_sucesso() {
        Ativo domain = AtivoGatewayImpl.toDomain(sampleMapper);

        assertNotNull(domain);
        assertEquals(sampleMapper.getId(), domain.getId());
        assertEquals(sampleMapper.getNome(), domain.getNome());
        assertEquals(sampleMapper.getTipo(), domain.getTipo());
        assertEquals(sampleMapper.getDescricao(), domain.getDescricao());
        assertEquals(sampleMapper.getQuantidade(), domain.getQuantidade());
        assertEquals(sampleMapper.getLocalizacao(), domain.getLocalizacao());
        assertEquals(sampleMapper.getResponsavel(), domain.getResponsavel());
        assertEquals(sampleMapper.isAtivo(), domain.isAtivo());
        assertEquals(sampleMapper.getEmpresaId(), domain.getEmpresaId());
    }

    @Test
    @DisplayName("toDomain com null deve retornar null")
    void toDomain_null() {
        assertNull(AtivoGatewayImpl.toDomain(null));
    }

    @Test
    @DisplayName("Deve mapear corretamente de Ativo para AtivoDataMapper (toDataMapper)")
    void toDataMapper_sucesso() {
        AtivoDataMapper mapper = AtivoGatewayImpl.toDataMapper(sampleDomain);

        assertNotNull(mapper);
        assertEquals(sampleDomain.getId(), mapper.getId());
        assertEquals(sampleDomain.getNome(), mapper.getNome());
        assertEquals(sampleDomain.getTipo(), mapper.getTipo());
        assertEquals(sampleDomain.getDescricao(), mapper.getDescricao());
        assertEquals(sampleDomain.getQuantidade(), mapper.getQuantidade());
        assertEquals(sampleDomain.getLocalizacao(), mapper.getLocalizacao());
        assertEquals(sampleDomain.getResponsavel(), mapper.getResponsavel());
        assertEquals(sampleDomain.isAtivo(), mapper.isAtivo());
        assertEquals(sampleDomain.getEmpresaId(), mapper.getEmpresaId());
    }

    @Test
    @DisplayName("toDataMapper com null deve retornar null")
    void toDataMapper_null() {
        assertNull(AtivoGatewayImpl.toDataMapper(null));
    }

    @Test
    @DisplayName("salvar deve persistir via repositório e retornar domínio")
    void salvar_novo() {
        when(jpaAtivoRepository.save(any(AtivoDataMapper.class))).thenReturn(sampleMapper);

        Ativo salvo = ativoGateway.salvar(sampleDomain);

        assertNotNull(salvo);
        assertEquals(1L, salvo.getId());
        verify(jpaAtivoRepository, times(1)).save(any(AtivoDataMapper.class));
    }

    @Test
    @DisplayName("buscarPorId deve retornar Optional de domínio se existir")
    void buscarPorId_existente() {
        when(jpaAtivoRepository.findById(1L)).thenReturn(Optional.of(sampleMapper));

        Optional<Ativo> opt = ativoGateway.buscarPorId(1L);

        assertTrue(opt.isPresent());
        assertEquals("Servidor AD", opt.get().getNome());
    }

    @Test
    @DisplayName("buscarPorId deve retornar Optional vazio se não existir")
    void buscarPorId_inexistente() {
        when(jpaAtivoRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Ativo> opt = ativoGateway.buscarPorId(999L);

        assertTrue(opt.isEmpty());
    }

    @Test
    @DisplayName("listarPorEmpresa deve retornar lista de domínios")
    void listarPorEmpresa() {
        when(jpaAtivoRepository.findByEmpresaIdOrderByIdDesc(10L)).thenReturn(List.of(sampleMapper));

        List<Ativo> lista = ativoGateway.listarPorEmpresa(10L);

        assertEquals(1, lista.size());
        assertEquals("Servidor AD", lista.get(0).getNome());
    }

    @Test
    @DisplayName("somarQuantidadeAtivosPorEmpresa deve retornar soma correta")
    void somarQuantidadeAtivosPorEmpresa() {
        when(jpaAtivoRepository.somarQuantidadeAtivosPorEmpresa(10L)).thenReturn(42);

        int total = ativoGateway.somarQuantidadeAtivosPorEmpresa(10L);

        assertEquals(42, total);
    }

    @Test
    @DisplayName("somarQuantidadeAtivosPorEmpresa com null deve retornar 0")
    void somarQuantidadeAtivosPorEmpresa_nullRetornaZero() {
        when(jpaAtivoRepository.somarQuantidadeAtivosPorEmpresa(10L)).thenReturn(null);

        int total = ativoGateway.somarQuantidadeAtivosPorEmpresa(10L);

        assertEquals(0, total);
    }

    @Test
    @DisplayName("removerPorId deve invocar deleteById no repositório")
    void removerPorId() {
        doNothing().when(jpaAtivoRepository).deleteById(1L);

        ativoGateway.removerPorId(1L);

        verify(jpaAtivoRepository, times(1)).deleteById(1L);
    }
}
