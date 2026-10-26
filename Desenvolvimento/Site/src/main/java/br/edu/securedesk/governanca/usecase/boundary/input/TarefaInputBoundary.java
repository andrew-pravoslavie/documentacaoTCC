package br.edu.securedesk.governanca.usecase.boundary.input;

import br.edu.securedesk.governanca.domain.StatusTarefa;
import br.edu.securedesk.governanca.usecase.model.EvidenciaRequestModel;
import br.edu.securedesk.governanca.usecase.model.TarefaRequestModel;
import br.edu.securedesk.governanca.usecase.model.TarefaResponseModel;

import java.util.List;

/**
 * Input Boundary para Governança Operacional (Kanban de Tarefas de Remediação e Evidências - CRUD 3).
 */
public interface TarefaInputBoundary {

    TarefaResponseModel criar(TarefaRequestModel request);

    List<TarefaResponseModel> listarPorEmpresa(Long empresaId);

    TarefaResponseModel buscarPorId(Long id);

    TarefaResponseModel atualizar(Long id, TarefaRequestModel request);

    TarefaResponseModel alterarStatus(Long id, StatusTarefa novoStatus);

    TarefaResponseModel anexarEvidencia(Long tarefaId, EvidenciaRequestModel evidenciaRequest);

    void excluir(Long id);
}
