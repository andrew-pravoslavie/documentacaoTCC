package br.edu.securedesk.entity.ativo;

/**
 * Classificação dos tipos de ativos que compõem o pequeno parque tecnológico (<100 ativos).
 * Alinhado ao entregável OEM 2 e base de conhecimento da monografia.
 */
public enum TipoAtivo {
    ENDPOINT("Estações de Trabalho / Laptops"),
    SERVIDOR("Servidores Locais e Armazenamento"),
    REDE_PERIMETRO("Roteadores, Firewalls e Switches"),
    MOVEL("Smartphones e Tablets Corporativos"),
    NUVEM("Serviços e Armazenamento em Nuvem"),
    OUTRO("Outro Ativo");

    private final String descricao;

    TipoAtivo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
