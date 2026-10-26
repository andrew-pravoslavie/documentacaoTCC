package br.edu.securedesk.ativo.domain;

/**
 * Classificação dos tipos/categorias de ativos que compõem o pequeno parque tecnológico (<= 100 ativos).
 * Inclui as 4 categorias principais do framework e os tipos granulares do inventário.
 */
public enum TipoAtivo {
    ENDPOINTS("Estações de Trabalho, Notebooks e Periféricos"),
    SERVIDORES_LOCAIS("Servidores de Arquivos, Controladores de Domínio e NAS"),
    ATIVOS_REDE("Roteadores, Switches, Firewalls e Access Points Wi-Fi"),
    MOBILE_E_NUVEM("Dispositivos Móveis Corporativos e Serviços em Nuvem/SaaS"),
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
