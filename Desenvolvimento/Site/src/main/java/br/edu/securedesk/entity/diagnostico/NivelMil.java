package br.edu.securedesk.entity.diagnostico;

/**
 * Níveis de Maturidade baseados no modelo C2M2 (Maturity Indicator Level - MIL).
 * No modelo híbrido do TCC, o patamar mínimo recomendado para PMEs é o MIL 2.
 */
public enum NivelMil {
    MIL_0("Inexistente / Práticas Não Realizadas", 0),
    MIL_1("Inicial / Práticas Ad-hoc e Informais", 1),
    MIL_2("Padronizado / Práticas Documentadas e Atribuídas (Patamar Alvo)", 2),
    MIL_3("Otimizado / Práticas Institucionalizadas com Métricas", 3);

    private final String descricao;
    private final int valorNumerico;

    NivelMil(String descricao, int valorNumerico) {
        this.descricao = descricao;
        this.valorNumerico = valorNumerico;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }
}
