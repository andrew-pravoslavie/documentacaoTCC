package br.edu.securedesk.governanca.usecase.boundary.output;

import br.edu.securedesk.governanca.domain.Evidencia;
import br.edu.securedesk.governanca.domain.TarefaRemediacao;

import java.util.List;
import java.util.Optional;

/**
 * Output Boundary (Gateway) para persistência de TarefaRemediacao e Evidencia.
 */
public interface TarefaGateway {

    TarefaRemediacao salvar(TarefaRemediacao tarefa);

    Optional<TarefaRemediacao> buscarPorId(Long id);

    List<TarefaRemediacao> listarPorEmpresa(Long empresaId);

    Evidencia salvarEvidencia(Evidencia evidencia);

    List<Evidencia> listarEvidenciasPorTarefa(Long tarefaId);

    void removerPorId(Long id);
}
