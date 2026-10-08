package br.edu.securedesk.entity.core;

/**
 * Setor de atuação econômica da empresa.
 */
public enum SetorEmpresa {
    TECNOLOGIA("Tecnologia"),
    COMERCIO("Comércio"),
    SERVICOS("Serviços"),
    INDUSTRIA("Indústria"),
    SAUDE("Saúde"),
    EDUCACAO("Educação"),
    OUTRO("Outro");

    private final String descricao;

    SetorEmpresa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
