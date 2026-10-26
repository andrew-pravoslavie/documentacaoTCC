package br.edu.securedesk.core.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio pura (POJO sem anotações JPA) representando o Usuário da PME (CRUD 2).
 * Respeita RN04 (Isolamento Multi-Empresa), RN05 (Consentimento LGPD) e RNF01 (Hash de senha).
 */
public class Usuario {

    private Long id;
    private Long empresaId;
    private String nome;
    private String sobrenome;
    private String email;
    private String senhaHash;
    private PerfilUsuario perfil;
    private boolean ativo;
    private boolean consentimentoLgpdAceito;
    private LocalDateTime dataCadastro;

    public Usuario() {
        this.perfil = PerfilUsuario.EDITOR;
        this.ativo = true;
        this.dataCadastro = LocalDateTime.now();
    }

    public Usuario(Long id, Long empresaId, String nome, String sobrenome, String email,
                   String senhaHash, PerfilUsuario perfil, boolean ativo,
                   boolean consentimentoLgpdAceito, LocalDateTime dataCadastro) {
        this.id = id;
        this.empresaId = empresaId;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email != null ? email.trim().toLowerCase() : null;
        this.senhaHash = senhaHash;
        this.perfil = perfil != null ? perfil : PerfilUsuario.EDITOR;
        this.ativo = ativo;
        this.consentimentoLgpdAceito = consentimentoLgpdAceito;
        this.dataCadastro = dataCadastro != null ? dataCadastro : LocalDateTime.now();
        validarInvariantes();
    }

    public final void validarInvariantes() {
        if (empresaId == null || empresaId <= 0) {
            throw new IllegalArgumentException("O identificador da empresa (empresaId) é obrigatório para o isolamento multi-tenant (RN04).");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Um e-mail corporativo válido é obrigatório.");
        }
        if (senhaHash == null || senhaHash.isBlank()) {
            throw new IllegalArgumentException("O hash da senha do usuário é obrigatório (RNF01).");
        }
        if (perfil == null) {
            throw new IllegalArgumentException("O perfil de acesso do usuário é obrigatório.");
        }
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void registrarConsentimentoLgpd() {
        this.consentimentoLgpdAceito = true;
    }

    public String getNomeCompleto() {
        if (sobrenome != null && !sobrenome.isBlank()) {
            return nome + " " + sobrenome;
        }
        return nome;
    }

    public String getIniciais() {
        String n = (nome != null && !nome.isBlank()) ? nome.substring(0, 1).toUpperCase() : "U";
        String s = (sobrenome != null && !sobrenome.isBlank()) ? sobrenome.substring(0, 1).toUpperCase() : "";
        return n + s;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }
    public boolean isAtivo() { return ativo; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public boolean isConsentimentoLgpdAceito() { return consentimentoLgpdAceito; }
    public void setConsentimentoLgpdAceito(boolean consentimentoLgpdAceito) { this.consentimentoLgpdAceito = consentimentoLgpdAceito; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
