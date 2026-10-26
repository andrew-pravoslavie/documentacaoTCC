package br.edu.securedesk.compliance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade HistoricoRevisaoDataMapper.
 */
@Repository
public interface JpaHistoricoRevisaoRepository extends JpaRepository<HistoricoRevisaoDataMapper, Long> {

    List<HistoricoRevisaoDataMapper> findByPoliticaEmpresaIdOrderByDataRevisaoAscIdAsc(Long politicaEmpresaId);

    List<HistoricoRevisaoDataMapper> findByPoliticaEmpresaIdInOrderByDataRevisaoAscIdAsc(Collection<Long> politicaEmpresaIds);

    @Transactional
    void deleteByPoliticaEmpresaId(Long politicaEmpresaId);
}
