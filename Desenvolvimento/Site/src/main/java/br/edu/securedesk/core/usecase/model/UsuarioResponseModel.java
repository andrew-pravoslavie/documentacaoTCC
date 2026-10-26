package br.edu.securedesk.core.usecase.model;

import br.edu.securedesk.core.domain.PerfilUsuario;
import br.edu.securedesk.core.domain.Usuario;

import java.time.LocalDateTime;

/**
 * Record de saída (Response Model) para Usuário da PME.
 */
public record UsuarioResponseModel(
        Long id,
        Long empresaId,
        String nome,
        String sobrenome,
        String nomeCompleto,
        String iniciais,
        String email,
        PerfilUsuario perfil,
        boolean ativo,
        boolean consentimentoLgpdAceito,
        LocalDateTime dataCadastro
) {
    public static UsuarioResponseModel fromDomain(Usuario u) {
        return new UsuarioResponseModel(
                u.getId(),
                u.getEmpresaId(),
                u.getNome(),
                u.getSobrenome(),
                u.getNomeCompleto(),
                u.getIniciais(),
                u.getEmail(),
                u.getPerfil(),
                u.isAtivo(),
                u.isConsentimentoLgpdAceito(),
                u.getDataCadastro()
        );
    }
}
