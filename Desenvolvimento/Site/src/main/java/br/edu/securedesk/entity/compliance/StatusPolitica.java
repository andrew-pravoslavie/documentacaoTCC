package br.edu.securedesk.entity.compliance;

/**
 * Status do ciclo de vida e conformidade de uma política de segurança da empresa.
 */
public enum StatusPolitica {
    EM_DIA("Em dia / Vigente"),
    REVISAO_PENDENTE("Revisão Pendente"),
    VENCIDA("Vencida");

    private final String descricao;

    StatusPolitica(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
