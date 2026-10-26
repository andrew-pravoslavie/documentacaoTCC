package br.edu.securedesk.core.usecase.boundary.output;

import br.edu.securedesk.core.domain.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Output Boundary (Gateway) de persistência para Usuários da PME.
 */
public interface UsuarioGateway {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> listarPorEmpresa(Long empresaId);

    boolean existePorEmail(String email);

    void removerPorId(Long id);
}
