package br.edu.securedesk.entity.core;

/**
 * Porte da empresa cliente (foco em PMEs e pequenos parques tecnológicos).
 */
public enum PorteEmpresa {
    MICROEMPRESA("Microempresa"),
    PEQUENA_EMPRESA("Pequena Empresa"),
    MEDIA_EMPRESA("Média Empresa");

    private final String descricao;

    PorteEmpresa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
