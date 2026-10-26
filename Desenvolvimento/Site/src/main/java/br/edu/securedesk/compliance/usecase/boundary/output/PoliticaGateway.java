package br.edu.securedesk.compliance.usecase.boundary.output;

import br.edu.securedesk.compliance.domain.HistoricoRevisao;
import br.edu.securedesk.compliance.domain.PoliticaEmpresa;

import java.util.List;
import java.util.Optional;

/**
 * Output Boundary (Gateway) para persistência de Políticas de Segurança e Histórico de Revisões (CRUD 4).
 */
public interface PoliticaGateway {

    PoliticaEmpresa salvar(PoliticaEmpresa politica);

    Optional<PoliticaEmpresa> buscarPorId(Long id);

    List<PoliticaEmpresa> listarPorEmpresa(Long empresaId);

    HistoricoRevisao salvarRevisao(HistoricoRevisao revisao);

    List<HistoricoRevisao> listarRevisoesPorPolitica(Long politicaEmpresaId);

    void removerPorId(Long id);
}
