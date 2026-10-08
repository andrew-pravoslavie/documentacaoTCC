package br.edu.securedesk.entity.compliance;

/**
 * Tipos de notificações disparadas pelo motor de compliance e monitoramento contínuo.
 */
public enum TipoNotificacao {
    REVISAO_POLITICA("Revisão de Política Próxima"),
    GAP_CRITICO("Gap Crítico Identificado"),
    TAREFA_ATRASADA("Tarefa de Remediação Atrasada"),
    SISTEMA("Aviso do Sistema");

    private final String descricao;

    TipoNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
