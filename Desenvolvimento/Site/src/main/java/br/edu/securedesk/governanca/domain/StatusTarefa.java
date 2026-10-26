package br.edu.securedesk.governanca.domain;

/**
 * Status do ciclo de vida das tarefas de remediação no quadro Kanban (CRUD 3).
 */
public enum StatusTarefa {
    BACKLOG("Backlog / Pendente"),
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDO("Concluído"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusTarefa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isPendenteOuBacklog() {
        return this == BACKLOG || this == PENDENTE;
    }

    public boolean isConcluido() {
        return this == CONCLUIDO || this == CONCLUIDA;
    }
}
