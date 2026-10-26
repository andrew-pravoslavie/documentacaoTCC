package br.edu.securedesk.ativo.usecase.interactor;

import br.edu.securedesk.ativo.domain.Ativo;
import br.edu.securedesk.ativo.domain.LimiteAtivosExcedidoException;
import br.edu.securedesk.ativo.domain.TipoAtivo;
import br.edu.securedesk.ativo.usecase.boundary.output.AtivoGateway;
import br.edu.securedesk.ativo.usecase.model.AtivoRequestModel;
import br.edu.securedesk.ativo.usecase.model.AtivoResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure unit tests for {@link AtivoInteractor} exercising the strict 100‑asset rule (RN01).
 * <p>
 * No Spring context or database is used – a simple in‑memory {@link AtivoGateway}
 * implementation provides the required behaviour.
 */
class AtivoInteractorTest {

    private AtivoInteractor interactor;
    private InMemoryAtivoGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new InMemoryAtivoGateway();
        interactor = new AtivoInteractor(gateway);
    }

    /** Helper to create a request model. */
    private AtivoRequestModel req(String nome, int quantidade, boolean ativo, Long empresaId) {
        return new AtivoRequestModel(
                nome,
                TipoAtivo.ENDPOINTS,
                "desc",
                quantidade,
                "loc",
                "resp",
                ativo,
                empresaId
        );
    }

    @Test
    @DisplayName("1) Cadastro dentro do limite – sucesso")
    void cadastrarDentroDoLimite() {
        // empresa já possui 50 ativos ativos
        gateway.seedAtivos(50, true, 1L);
        AtivoResponseModel response = interactor.cadastrar(req("Novo", 30, true, 1L));
        assertEquals(30, response.quantidade());
        assertEquals(80, gateway.somarQuantidadeAtivosPorEmpresa(1L));
    }

    @Test
    @DisplayName("2) Cadastro exatamente no limite – sucesso")
    void cadastrarNoLimiteExato() {
        gateway.seedAtivos(70, true, 1L);
        AtivoResponseModel response = interactor.cadastrar(req("Novo", 30, true, 1L));
        assertEquals(30, response.quantidade());
        assertEquals(100, gateway.somarQuantidadeAtivosPorEmpresa(1L));
    }

    @Test
    @DisplayName("3) Tentativa de estouro ao cadastrar – exceção")
    void cadastrarEstouro() {
        gateway.seedAtivos(80, true, 1L);
        LimiteAtivosExcedidoException ex = assertThrows(
                LimiteAtivosExcedidoException.class,
                () -> interactor.cadastrar(req("Novo", 21, true, 1L))
        );
        assertEquals(80, ex.quantidadeAtual());
        assertEquals(21, ex.quantidadeASomar());
    }

    @Test
    @DisplayName("4) Cadastro único > 100 – exceção")
    void cadastrarUnicoAcimaDoLimite() {
        LimiteAtivosExcedidoException ex = assertThrows(
                LimiteAtivosExcedidoException.class,
                () -> interactor.cadastrar(req("Muita", 101, true, 1L))
        );
        assertEquals(0, ex.quantidadeAtual());
        assertEquals(101, ex.quantidadeASomar());
    }

    @Test
    @DisplayName("5) Atualização dentro do limite – sucesso")
    void atualizarDentroDoLimite() {
        // total 90 ativos ativos; um ativo existente com quantidade 10
        gateway.seedAtivos(80, true, 1L);
        Ativo existing = gateway.salvar(new Ativo(null, "A", TipoAtivo.ENDPOINTS, "d", 10, "l", "r", true, 1L));
        // atualizar para quantidade 20 -> total 100
        AtivoResponseModel resp = interactor.atualizar(existing.getId(), req("A", 20, true, 1L));
        assertEquals(20, resp.quantidade());
        assertEquals(100, gateway.somarQuantidadeAtivosPorEmpresa(1L));
    }

    @Test
    @DisplayName("5b) Atualização que estoura – exceção")
    void atualizarEstoura() {
        gateway.seedAtivos(90, true, 1L);
        Ativo existing = gateway.salvar(new Ativo(null, "A", TipoAtivo.ENDPOINTS, "d", 10, "l", "r", true, 1L));
        // mudar para 21 -> 101 ativos ativos
        LimiteAtivosExcedidoException ex = assertThrows(
                LimiteAtivosExcedidoException.class,
                () -> interactor.atualizar(existing.getId(), req("A", 21, true, 1L))
        );
        assertEquals(100 - 10, ex.quantidadeAtual()); // base without this record: 90
        assertEquals(21, ex.quantidadeASomar());
    }

    @Test
    @DisplayName("6) Inativação libera cota")
    void inativarLiberaCota() {
        // 100 ativos ativos
        gateway.seedAtivos(90, true, 1L);
        Ativo ativo = gateway.salvar(new Ativo(null, "B", TipoAtivo.ENDPOINTS, "d", 10, "l", "r", true, 1L));
        // inativar
        interactor.inativar(ativo.getId());
        assertEquals(90, gateway.somarQuantidadeAtivosPorEmpresa(1L));
        // agora novo cadastro de 10 ativos deve ser permitido
        AtivoResponseModel resp = interactor.cadastrar(req("Novo", 10, true, 1L));
        assertEquals(10, resp.quantidade());
    }

    @Test
    @DisplayName("7) Cadastro de ativo já inativo não conta para a trava")
    void cadastrarInativoNaoConta() {
        // 100 ativos ativos já existentes
        gateway.seedAtivos(100, true, 1L);
        // cadastrar um ativo inativo de quantidade 20 – deve ser aceito
        AtivoResponseModel resp = interactor.cadastrar(req("Inativo", 20, false, 1L));
        assertFalse(resp.ativo());
        // total de ativos ativos continua 100
        assertEquals(100, gateway.somarQuantidadeAtivosPorEmpresa(1L));
    }

    // ---------------------------------------------------------------------
    // Simple in‑memory gateway used by the tests.
    // ---------------------------------------------------------------------
    private static class InMemoryAtivoGateway implements AtivoGateway {
        private final Map<Long, Ativo> store = new HashMap<>();
        private final AtomicLong idSeq = new AtomicLong(1);

        /** Seed a number of active/inactive assets for a given empresa. */
        void seedAtivos(int totalQuantidade, boolean ativo, Long empresaId) {
            // Distribute quantity in a single record for simplicity
            Ativo a = new Ativo(null, "seed", TipoAtivo.ENDPOINTS, "seed", totalQuantidade, "loc", "resp", ativo, empresaId);
            salvar(a);
        }

        @Override
        public Ativo salvar(Ativo ativo) {
            if (ativo.getId() == null) {
                ativo.setId(idSeq.getAndIncrement());
            }
            store.put(ativo.getId(), ativo);
            return ativo;
        }

        @Override
        public Optional<Ativo> buscarPorId(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<Ativo> listarPorEmpresa(Long empresaId) {
            List<Ativo> list = new ArrayList<>();
            for (Ativo a : store.values()) {
                if (Objects.equals(a.getEmpresaId(), empresaId)) {
                    list.add(a);
                }
            }
            return list;
        }

        @Override
        public int somarQuantidadeAtivosPorEmpresa(Long empresaId) {
            int sum = 0;
            for (Ativo a : store.values()) {
                if (Objects.equals(a.getEmpresaId(), empresaId) && a.isAtivo()) {
                    sum += a.getQuantidade();
                }
            }
            return sum;
        }

        @Override
        public void removerPorId(Long id) {
            store.remove(id);
        }
    }
}
