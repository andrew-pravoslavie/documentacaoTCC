package br.edu.securedesk.core.domain;

/**
 * Perfis de acesso dos usuários da PME (RF02).
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
