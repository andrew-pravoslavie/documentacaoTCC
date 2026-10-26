package br.edu.securedesk.governanca.usecase.interactor;

import br.edu.securedesk.governanca.domain.Evidencia;
import br.edu.securedesk.governanca.domain.Prioridade;
import br.edu.securedesk.governanca.domain.StatusTarefa;
import br.edu.securedesk.governanca.domain.TarefaRemediacao;
import br.edu.securedesk.governanca.usecase.boundary.output.TarefaGateway;
import br.edu.securedesk.governanca.usecase.model.EvidenciaRequestModel;
import br.edu.securedesk.governanca.usecase.model.TarefaRequestModel;
import br.edu.securedesk.governanca.usecase.model.TarefaResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure unit tests for {@link GerenciarTarefaInteractor} covering Kanban flow,
 * status transitions, invalid transition from CANCELADA and evidence attachment.
 */
class GerenciarTarefaInteractorTest {

    private GerenciarTarefaInteractor interactor;
    private InMemoryTarefaGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new InMemoryTarefaGateway();
        interactor = new GerenciarTarefaInteractor(gateway);
    }

    private TarefaRequestModel req(StatusTarefa status, Long empresaId) {
        return new TarefaRequestModel(
                "Título",
                "Descrição",
                "GV",
                "Responsável",
                status,
                Prioridade.ALTA,
                LocalDate.now().plusDays(7),
                1L,
                empresaId
        );
    }

    @Test
    @DisplayName("1) Criação define status BACKLOG por padrão e sem dataConclusao")
    void criarTarefaBacklog() {
        TarefaResponseModel resp = interactor.criar(req(null, 1L));
        assertEquals(StatusTarefa.BACKLOG, resp.status());
        assertNull(resp.dataConclusao());
        assertEquals(1L, resp.empresaId());
    }

    @Test
    @DisplayName("2) Transição BACKLOG -> EM_ANDAMENTO -> CONCLUIDO preenche dataConclusao")
    void fluxoKanbanConcluido() {
        TarefaResponseModel created = interactor.criar(req(StatusTarefa.BACKLOG, 1L));
        Long id = created.id();
        interactor.alterarStatus(id, StatusTarefa.EM_ANDAMENTO);
        TarefaResponseModel concluida = interactor.alterarStatus(id, StatusTarefa.CONCLUIDO);
        assertTrue(concluida.status().isConcluido());
        assertNotNull(concluida.dataConclusao());
    }

    @Test
    @DisplayName("3) Bloqueio de transição inválida a partir de CANCELADA")
    void transicaoInvalidaAPartirDeCancelada() {
        TarefaResponseModel created = interactor.criar(req(StatusTarefa.CANCELADA, 1L));
        Long id = created.id();
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> interactor.alterarStatus(id, StatusTarefa.EM_ANDAMENTO));
        assertTrue(ex.getMessage().contains("reaberta"));
    }

    @Test
    @DisplayName("4) Anexar evidência a tarefa existente")
    void anexarEvidencia() {
        TarefaResponseModel created = interactor.criar(req(StatusTarefa.BACKLOG, 1L));
        Long id = created.id();
        EvidenciaRequestModel evReq = new EvidenciaRequestModel(
                "relatorio.pdf",
                "application/pdf",
                1024L,
                "Relatório da auditoria",
                "Auditor"
        );
        TarefaResponseModel after = interactor.anexarEvidencia(id, evReq);
        assertEquals(1, after.evidencias().size());
        TarefaResponseModel.EvidenciaItemModel ev = after.evidencias().getFirst();
        assertEquals("relatorio.pdf", ev.nomeArquivo());
    }

    private static class InMemoryTarefaGateway implements TarefaGateway {
        private final Map<Long, TarefaRemediacao> store = new HashMap<>();
        private final AtomicLong idSeq = new AtomicLong(1);
        private final Map<Long, Evidencia> evidencias = new HashMap<>();
        private final AtomicLong evIdSeq = new AtomicLong(1);

        @Override
        public TarefaRemediacao salvar(TarefaRemediacao tarefa) {
            if (tarefa.getId() == null) {
                tarefa.setId(idSeq.getAndIncrement());
            }
            store.put(tarefa.getId(), tarefa);
            return tarefa;
        }

        @Override
        public Optional<TarefaRemediacao> buscarPorId(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<TarefaRemediacao> listarPorEmpresa(Long empresaId) {
            List<TarefaRemediacao> list = new ArrayList<>();
            for (TarefaRemediacao t : store.values()) {
                if (Objects.equals(t.getEmpresaId(), empresaId)) {
                    list.add(t);
                }
            }
            return list;
        }

        @Override
        public Evidencia salvarEvidencia(Evidencia evidencia) {
            if (evidencia.getId() == null) {
                evidencia.setId(evIdSeq.getAndIncrement());
            }
            evidencias.put(evidencia.getId(), evidencia);
            return evidencia;
        }

        @Override
        public List<Evidencia> listarEvidenciasPorTarefa(Long tarefaId) {
            List<Evidencia> list = new ArrayList<>();
            for (Evidencia e : evidencias.values()) {
                if (Objects.equals(e.getTarefaId(), tarefaId)) {
                    list.add(e);
                }
            }
            return list;
        }

        @Override
        public void removerPorId(Long id) {
            store.remove(id);
        }
    }
}
