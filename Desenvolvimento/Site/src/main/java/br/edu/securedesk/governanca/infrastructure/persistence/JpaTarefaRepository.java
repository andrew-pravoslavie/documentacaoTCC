package br.edu.securedesk.governanca.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade TarefaDataMapper.
 */
@Repository
public interface JpaTarefaRepository extends JpaRepository<TarefaDataMapper, Long> {

    List<TarefaDataMapper> findByEmpresaIdOrderByIdDesc(Long empresaId);
}
