package br.edu.securedesk.entity.governanca;

/**
 * Nível estimado do custo e complexidade de mitigação do gap (eixo horizontal da matriz de risco).
 */
public enum CustoMitigacao {
    BAIXO("Baixo Custo / Rápida Implementação"),
    MEDIO("Médio Custo / Média Complexidade"),
    ALTO("Alto Custo / Alta Complexidade");

    private final String descricao;

    CustoMitigacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
