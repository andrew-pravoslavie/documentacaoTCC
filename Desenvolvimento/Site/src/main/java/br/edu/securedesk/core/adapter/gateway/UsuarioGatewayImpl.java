package br.edu.securedesk.core.adapter.gateway;

import br.edu.securedesk.core.domain.Usuario;
import br.edu.securedesk.core.infrastructure.persistence.JpaUsuarioRepository;
import br.edu.securedesk.core.infrastructure.persistence.UsuarioDataMapper;
import br.edu.securedesk.core.usecase.boundary.output.UsuarioGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do Gateway de persistência para Usuários (Clean Architecture).
 * Realiza a conversão entre o modelo de domínio puro e o modelo relacional JPA.
 */
@Component
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final JpaUsuarioRepository jpaUsuarioRepository;

    public UsuarioGatewayImpl(JpaUsuarioRepository jpaUsuarioRepository) {
        this.jpaUsuarioRepository = jpaUsuarioRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioDataMapper mapper = toDataMapper(usuario);
        if (mapper.getId() != null && mapper.getDataCadastro() == null) {
            jpaUsuarioRepository.findById(mapper.getId())
                    .ifPresent(existing -> mapper.setDataCadastro(existing.getDataCadastro()));
        }
        UsuarioDataMapper salvo = jpaUsuarioRepository.save(mapper);
        return toDomain(salvo);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaUsuarioRepository.findById(id).map(UsuarioGatewayImpl::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return jpaUsuarioRepository.findByEmailIgnoreCase(email.trim()).map(UsuarioGatewayImpl::toDomain);
    }

    @Override
    public List<Usuario> listarPorEmpresa(Long empresaId) {
        return jpaUsuarioRepository.findByEmpresaIdOrderByIdDesc(empresaId)
                .stream()
                .map(UsuarioGatewayImpl::toDomain)
                .toList();
    }

    @Override
    public boolean existePorEmail(String email) {
        if (email == null) {
            return false;
        }
        return jpaUsuarioRepository.existsByEmailIgnoreCase(email.trim());
    }

    @Override
    public void removerPorId(Long id) {
        jpaUsuarioRepository.deleteById(id);
    }

    public static Usuario toDomain(UsuarioDataMapper mapper) {
        if (mapper == null) {
            return null;
        }
        return new Usuario(
                mapper.getId(),
                mapper.getEmpresaId(),
                mapper.getNome(),
                mapper.getSobrenome(),
                mapper.getEmail(),
                mapper.getSenhaHash(),
                mapper.getPerfil(),
                mapper.isAtivo(),
                mapper.isConsentimentoLgpdAceito(),
                mapper.getDataCadastro()
        );
    }

    public static UsuarioDataMapper toDataMapper(Usuario domain) {
        if (domain == null) {
            return null;
        }
        return UsuarioDataMapper.builder()
                .id(domain.getId())
                .empresaId(domain.getEmpresaId())
                .nome(domain.getNome())
                .sobrenome(domain.getSobrenome())
                .email(domain.getEmail())
                .senhaHash(domain.getSenhaHash())
                .perfil(domain.getPerfil())
                .ativo(domain.isAtivo())
                .consentimentoLgpdAceito(domain.isConsentimentoLgpdAceito())
                .dataCadastro(domain.getDataCadastro())
                .build();
    }
}
