package br.edu.securedesk.core.usecase.model;

import br.edu.securedesk.core.domain.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Record de entrada para cadastro e edição de Usuário da PME (CRUD 2).
 */
public record UsuarioRequestModel(
        @NotBlank(message = "O nome do colaborador é obrigatório.")
        @Size(max = 80, message = "O nome deve ter no máximo 80 caracteres.")
        String nome,

        @Size(max = 80, message = "O sobrenome deve ter no máximo 80 caracteres.")
        String sobrenome,

        @NotBlank(message = "O e-mail corporativo é obrigatório.")
        @Email(message = "Informe um endereço de e-mail válido.")
        @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres.")
        String email,

        @Size(min = 6, max = 100, message = "A senha deve conter no mínimo 6 caracteres.")
        String senha,

        @NotNull(message = "O perfil de acesso é obrigatório.")
        PerfilUsuario perfil,

        Boolean ativo,

        Boolean consentimentoLgpd,

        Long empresaId
) {
    public UsuarioRequestModel {
        if (perfil == null) {
            perfil = PerfilUsuario.EDITOR;
        }
        if (ativo == null) {
            ativo = true;
        }
        if (consentimentoLgpd == null) {
            consentimentoLgpd = true;
        }
        if (empresaId == null) {
            empresaId = 1L;
        }
    }

    public static UsuarioRequestModel padrao(Long empresaId) {
        return new UsuarioRequestModel("", "", "", "Senha@123", PerfilUsuario.EDITOR, true, true, empresaId);
    }

    public static UsuarioRequestModel fromResponse(UsuarioResponseModel resp) {
        return new UsuarioRequestModel(
                resp.nome(),
                resp.sobrenome(),
                resp.email(),
                "Senha@123",
                resp.perfil(),
                resp.ativo(),
                resp.consentimentoLgpdAceito(),
                resp.empresaId()
        );
    }

    public String getNome() { return nome; }
    public String getSobrenome() { return sobrenome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public PerfilUsuario getPerfil() { return perfil; }
    public Boolean getAtivo() { return ativo; }
    public Boolean getConsentimentoLgpd() { return consentimentoLgpd; }
    public Long getEmpresaId() { return empresaId; }
}
