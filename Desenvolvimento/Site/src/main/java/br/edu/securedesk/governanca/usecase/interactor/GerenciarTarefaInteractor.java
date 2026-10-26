package br.edu.securedesk.governanca.usecase.interactor;

import br.edu.securedesk.governanca.domain.Evidencia;
import br.edu.securedesk.governanca.domain.StatusTarefa;
import br.edu.securedesk.governanca.domain.TarefaRemediacao;
import br.edu.securedesk.governanca.usecase.boundary.input.TarefaInputBoundary;
import br.edu.securedesk.governanca.usecase.boundary.output.TarefaGateway;
import br.edu.securedesk.governanca.usecase.model.EvidenciaRequestModel;
import br.edu.securedesk.governanca.usecase.model.TarefaRequestModel;
import br.edu.securedesk.governanca.usecase.model.TarefaResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Use Case Interactor para o Quadro Kanban de Tarefas de Remediação e Evidências (CRUD 3).
 */
@Service
public class GerenciarTarefaInteractor implements TarefaInputBoundary {

    private final TarefaGateway tarefaGateway;

    public GerenciarTarefaInteractor(TarefaGateway tarefaGateway) {
        this.tarefaGateway = tarefaGateway;
    }

    @Override
    @Transactional
    public TarefaResponseModel criar(TarefaRequestModel request) {
        StatusTarefa statusInicial = request.status() != null ? request.status() : StatusTarefa.BACKLOG;
        LocalDateTime dataConclusao = statusInicial.isConcluido() ? LocalDateTime.now() : null;

        TarefaRemediacao nova = new TarefaRemediacao(
                null,
                request.empresaId(),
                request.gapId(),
                request.funcaoNistSigla(),
                request.titulo(),
                request.descricao(),
                request.responsavelNome(),
                statusInicial,
                request.prioridade(),
                request.prazoLimite(),
                dataConclusao,
                LocalDateTime.now(),
                List.of()
        );

        TarefaRemediacao salva = tarefaGateway.salvar(nova);
        return TarefaResponseModel.fromDomain(salva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarefaResponseModel> listarPorEmpresa(Long empresaId) {
        return tarefaGateway.listarPorEmpresa(empresaId)
                .stream()
                .map(TarefaResponseModel::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TarefaResponseModel buscarPorId(Long id) {
        TarefaRemediacao tarefa = tarefaGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Tarefa de remediação não encontrada com id: " + id));
        return TarefaResponseModel.fromDomain(tarefa);
    }

    @Override
    @Transactional
    public TarefaResponseModel atualizar(Long id, TarefaRequestModel request) {
        TarefaRemediacao existente = tarefaGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Tarefa de remediação não encontrada com id: " + id));

        StatusTarefa novoStatus = request.status() != null ? request.status() : existente.getStatus();
        existente.setTitulo(request.titulo());
        existente.setDescricao(request.descricao());
        existente.setFuncaoNistSigla(request.funcaoNistSigla());
        existente.setResponsavelNome(request.responsavelNome());
        existente.setPrioridade(request.prioridade());
        existente.setPrazoLimite(request.prazoLimite());
        if (novoStatus != existente.getStatus()) {
            existente.transicionarPara(novoStatus);
        }
        existente.validarInvariantes();

        TarefaRemediacao salva = tarefaGateway.salvar(existente);
        return TarefaResponseModel.fromDomain(salva);
    }

    @Override
    @Transactional
    public TarefaResponseModel alterarStatus(Long id, StatusTarefa novoStatus) {
        TarefaRemediacao existente = tarefaGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Tarefa de remediação não encontrada com id: " + id));
        existente.transicionarPara(novoStatus);
        TarefaRemediacao salva = tarefaGateway.salvar(existente);
        return TarefaResponseModel.fromDomain(salva);
    }

    @Override
    @Transactional
    public TarefaResponseModel anexarEvidencia(Long tarefaId, EvidenciaRequestModel evidenciaRequest) {
        TarefaRemediacao existente = tarefaGateway.buscarPorId(tarefaId)
                .orElseThrow(() -> new NoSuchElementException("Tarefa de remediação não encontrada com id: " + tarefaId));

        Evidencia novaEvidencia = new Evidencia(
                null,
                tarefaId,
                evidenciaRequest.nomeArquivo(),
                evidenciaRequest.tipoArquivo(),
                evidenciaRequest.tamanhoBytes(),
                "/uploads/" + evidenciaRequest.nomeArquivo(),
                evidenciaRequest.descricao(),
                evidenciaRequest.autorUpload(),
                LocalDateTime.now()
        );

        Evidencia salva = tarefaGateway.salvarEvidencia(novaEvidencia);
        existente.adicionarEvidencia(salva);
        return TarefaResponseModel.fromDomain(existente);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (tarefaGateway.buscarPorId(id).isEmpty()) {
            throw new NoSuchElementException("Tarefa de remediação não encontrada com id: " + id);
        }
        tarefaGateway.removerPorId(id);
    }
}
