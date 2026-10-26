package br.edu.securedesk.core.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para a entidade UsuarioDataMapper.
 */
@Repository
public interface JpaUsuarioRepository extends JpaRepository<UsuarioDataMapper, Long> {

    List<UsuarioDataMapper> findByEmpresaIdOrderByIdDesc(Long empresaId);

    Optional<UsuarioDataMapper> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
