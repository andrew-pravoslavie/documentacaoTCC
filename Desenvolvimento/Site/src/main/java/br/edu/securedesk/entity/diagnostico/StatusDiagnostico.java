package br.edu.securedesk.entity.diagnostico;

/**
 * Status do processo de preenchimento do questionário de diagnóstico.
 */
public enum StatusDiagnostico {
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDO("Concluído");

    private final String descricao;

    StatusDiagnostico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
