package br.edu.securedesk.entity.governanca;

/**
 * Nível de impacto de uma vulnerabilidade/gap sobre o negócio (eixo vertical da matriz de risco).
 */
public enum NivelImpacto {
    BAIXO("Baixo"),
    MEDIO("Médio"),
    ALTO("Alto");

    private final String descricao;

    NivelImpacto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
