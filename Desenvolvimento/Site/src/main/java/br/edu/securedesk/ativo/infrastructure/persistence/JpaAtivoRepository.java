package br.edu.securedesk.ativo.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade AtivoDataMapper.
 */
@Repository
public interface JpaAtivoRepository extends JpaRepository<AtivoDataMapper, Long> {

    List<AtivoDataMapper> findByEmpresaIdOrderByIdDesc(Long empresaId);

    @Query("SELECT COALESCE(SUM(a.quantidade), 0) FROM AtivoDataMapper a WHERE a.empresaId = :empresaId AND a.ativo = true")
    Integer somarQuantidadeAtivosPorEmpresa(@Param("empresaId") Long empresaId);
}
