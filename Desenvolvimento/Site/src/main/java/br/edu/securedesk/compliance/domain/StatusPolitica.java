package br.edu.securedesk.compliance.domain;

/**
 * Status do ciclo de vida das Políticas de Segurança da PME (CRUD 4).
 */
public enum StatusPolitica {
    EM_ELABORACAO("Em Elaboração"),
    PENDENTE_REVISAO("Pendente de Revisão"),
    REVISAO_PENDENTE("Revisão Pendente"),
    EM_DIA("Em dia / Vigente"),
    VENCIDA("Vencida");

    private final String descricao;

    StatusPolitica(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isPendenteRevisao() {
        return this == PENDENTE_REVISAO || this == REVISAO_PENDENTE;
    }
}
