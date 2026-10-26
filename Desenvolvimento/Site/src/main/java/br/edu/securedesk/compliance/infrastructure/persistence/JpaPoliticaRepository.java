package br.edu.securedesk.compliance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade PoliticaDataMapper.
 */
@Repository
public interface JpaPoliticaRepository extends JpaRepository<PoliticaDataMapper, Long> {

    List<PoliticaDataMapper> findByEmpresaIdOrderByTituloAsc(Long empresaId);
}
