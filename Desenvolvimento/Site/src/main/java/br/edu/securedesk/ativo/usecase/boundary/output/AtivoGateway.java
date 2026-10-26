package br.edu.securedesk.ativo.usecase.boundary.output;

import br.edu.securedesk.ativo.domain.Ativo;

import java.util.List;
import java.util.Optional;

/**
 * Output Boundary (Gateway de Persistência) da camada de casos de uso para o domínio de Ativos.
 * Isola o Interactor de qualquer tecnologia de banco de dados (JPA/SQL).
 */
public interface AtivoGateway {

    Ativo salvar(Ativo ativo);

    Optional<Ativo> buscarPorId(Long id);

    List<Ativo> listarPorEmpresa(Long empresaId);

    int somarQuantidadeAtivosPorEmpresa(Long empresaId);

    void removerPorId(Long id);
}
