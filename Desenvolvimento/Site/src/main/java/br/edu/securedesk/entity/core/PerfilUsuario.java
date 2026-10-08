package br.edu.securedesk.entity.core;

/**
 * Perfis de acesso dos usuários ao sistema SecureDesk.
 */
public enum PerfilUsuario {
    ADMINISTRADOR("Administrador"),
    EDITOR("Editor"),
    VISUALIZADOR("Visualizador");

    private final String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
