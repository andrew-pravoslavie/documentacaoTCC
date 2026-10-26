package br.edu.securedesk.core.usecase.boundary.input;

import br.edu.securedesk.core.usecase.model.UsuarioRequestModel;
import br.edu.securedesk.core.usecase.model.UsuarioResponseModel;

import java.util.List;

/**
 * Input Boundary para Gestão de Usuários da PME (CRUD 2).
 */
public interface UsuarioInputBoundary {

    UsuarioResponseModel cadastrar(UsuarioRequestModel request);

    List<UsuarioResponseModel> listarPorEmpresa(Long empresaId);

    UsuarioResponseModel buscarPorId(Long id);

    UsuarioResponseModel atualizar(Long id, UsuarioRequestModel request);

    UsuarioResponseModel alternarStatusAtivo(Long id);

    void excluir(Long id);
}
