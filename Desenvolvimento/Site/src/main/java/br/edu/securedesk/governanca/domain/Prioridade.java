package br.edu.securedesk.governanca.domain;

/**
 * Níveis de prioridade da Matriz de Risco e do Kanban de Remediação.
 */
public enum Prioridade {
    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta"),
    CRITICA("Crítica");

    private final String descricao;

    Prioridade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
