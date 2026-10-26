package br.edu.securedesk.ativo.usecase.boundary.input;

import br.edu.securedesk.ativo.usecase.model.AtivoRequestModel;
import br.edu.securedesk.ativo.usecase.model.AtivoResponseModel;

import java.util.List;

/**
 * Input Boundary (Contrato de Entrada) dos casos de uso de Gestão de Ativos de TI.
 */
public interface AtivoInputBoundary {

    AtivoResponseModel cadastrar(AtivoRequestModel request);

    List<AtivoResponseModel> listarPorEmpresa(Long empresaId);

    AtivoResponseModel buscarPorId(Long id);

    AtivoResponseModel atualizar(Long id, AtivoRequestModel request);

    void inativar(Long id);

    void excluir(Long id);

    int consultarTotalAtivosPorEmpresa(Long empresaId);
}
