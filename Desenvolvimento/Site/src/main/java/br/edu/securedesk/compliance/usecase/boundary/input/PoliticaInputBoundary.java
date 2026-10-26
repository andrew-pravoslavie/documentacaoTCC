package br.edu.securedesk.compliance.usecase.boundary.input;

import br.edu.securedesk.compliance.usecase.model.PoliticaRequestModel;
import br.edu.securedesk.compliance.usecase.model.PoliticaResponseModel;

import java.util.List;

/**
 * Input Boundary para Compliance e Gestão de Políticas de Segurança (CRUD 4).
 */
public interface PoliticaInputBoundary {

    PoliticaResponseModel adotarOuCriar(PoliticaRequestModel request);

    PoliticaResponseModel adotarTemplatePadrao(String codigoTemplate, Long empresaId);

    List<PoliticaResponseModel> listarPorEmpresa(Long empresaId);

    PoliticaResponseModel buscarPorId(Long id);

    PoliticaResponseModel revisarEAtualizar(Long id, PoliticaRequestModel request);

    void revogarOuExcluir(Long id);
}
