package br.edu.securedesk.governanca.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade EvidenciaDataMapper.
 */
@Repository
public interface JpaEvidenciaRepository extends JpaRepository<EvidenciaDataMapper, Long> {

    List<EvidenciaDataMapper> findByTarefaIdOrderByDataUploadAscIdAsc(Long tarefaId);

    List<EvidenciaDataMapper> findByTarefaIdInOrderByDataUploadAscIdAsc(Collection<Long> tarefaIds);

    @Transactional
    void deleteByTarefaId(Long tarefaId);
}
