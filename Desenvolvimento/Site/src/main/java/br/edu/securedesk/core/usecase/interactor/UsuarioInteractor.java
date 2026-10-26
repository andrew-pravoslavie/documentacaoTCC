package br.edu.securedesk.core.usecase.interactor;

import br.edu.securedesk.core.domain.Usuario;
import br.edu.securedesk.core.usecase.boundary.input.UsuarioInputBoundary;
import br.edu.securedesk.core.usecase.boundary.output.UsuarioGateway;
import br.edu.securedesk.core.usecase.model.UsuarioRequestModel;
import br.edu.securedesk.core.usecase.model.UsuarioResponseModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Interactor de Gestão de Usuários da PME (CRUD 2).
 * Garante isolamento por empresa (RN04), rastreabilidade LGPD (RN05) e criptografia BCrypt (RNF01).
 */
@Service
public class UsuarioInteractor implements UsuarioInputBoundary {

    private final UsuarioGateway usuarioGateway;
    private final PasswordEncoder passwordEncoder;

    public UsuarioInteractor(UsuarioGateway usuarioGateway, PasswordEncoder passwordEncoder) {
        this.usuarioGateway = usuarioGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioResponseModel cadastrar(UsuarioRequestModel request) {
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("O e-mail corporativo é obrigatório.");
        }
        String emailNormalizado = request.email().trim().toLowerCase();
        if (usuarioGateway.existePorEmail(emailNormalizado)) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com o e-mail: " + emailNormalizado);
        }
        if (request.senha() == null || request.senha().isBlank() || request.senha().length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        String senhaHash = passwordEncoder.encode(request.senha());
        boolean ativo = request.ativo() == null || request.ativo();
        boolean lgpdAceito = request.consentimentoLgpd() == null || request.consentimentoLgpd();

        Usuario novo = new Usuario(
                null,
                request.empresaId(),
                request.nome(),
                request.sobrenome(),
                emailNormalizado,
                senhaHash,
                request.perfil(),
                ativo,
                lgpdAceito,
                LocalDateTime.now()
        );

        Usuario salvo = usuarioGateway.salvar(novo);
        return UsuarioResponseModel.fromDomain(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseModel> listarPorEmpresa(Long empresaId) {
        return usuarioGateway.listarPorEmpresa(empresaId)
                .stream()
                .map(UsuarioResponseModel::fromDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseModel buscarPorId(Long id) {
        Usuario u = usuarioGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com id: " + id));
        return UsuarioResponseModel.fromDomain(u);
    }

    @Override
    @Transactional
    public UsuarioResponseModel atualizar(Long id, UsuarioRequestModel request) {
        Usuario existente = usuarioGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com id: " + id));

        String emailNormalizado = request.email() != null ? request.email().trim().toLowerCase() : existente.getEmail();
        Optional<Usuario> usuarioComMesmoEmail = usuarioGateway.buscarPorEmail(emailNormalizado);
        if (usuarioComMesmoEmail.isPresent() && !usuarioComMesmoEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Este e-mail já está em uso por outro colaborador.");
        }

        String senhaHash = (request.senha() != null && !request.senha().isBlank())
                ? passwordEncoder.encode(request.senha())
                : existente.getSenhaHash();

        boolean ativo = request.ativo() != null ? request.ativo() : existente.isAtivo();
        boolean lgpdAceito = request.consentimentoLgpd() != null ? request.consentimentoLgpd() : existente.isConsentimentoLgpdAceito();
        Long empresaId = request.empresaId() != null ? request.empresaId() : existente.getEmpresaId();

        Usuario atualizado = new Usuario(
                existente.getId(),
                empresaId,
                request.nome(),
                request.sobrenome(),
                emailNormalizado,
                senhaHash,
                request.perfil(),
                ativo,
                lgpdAceito,
                existente.getDataCadastro()
        );

        return UsuarioResponseModel.fromDomain(usuarioGateway.salvar(atualizado));
    }

    @Override
    @Transactional
    public UsuarioResponseModel alternarStatusAtivo(Long id) {
        Usuario existente = usuarioGateway.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com id: " + id));
        if (existente.isAtivo()) {
            existente.desativar();
        } else {
            existente.ativar();
        }
        return UsuarioResponseModel.fromDomain(usuarioGateway.salvar(existente));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (usuarioGateway.buscarPorId(id).isEmpty()) {
            throw new NoSuchElementException("Usuário não encontrado com id: " + id);
        }
        usuarioGateway.removerPorId(id);
    }
}
