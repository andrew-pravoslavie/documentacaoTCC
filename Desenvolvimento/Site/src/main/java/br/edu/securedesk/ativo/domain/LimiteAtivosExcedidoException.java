package br.edu.securedesk.ativo.domain;

/**
 * Exceção de domínio/negócio lançada quando a inclusão ou atualização de um ativo
 * viola a Regra RN01 (Trava Estrita de até 100 dispositivos gerenciados por PME).
 */
public class LimiteAtivosExcedidoException extends RuntimeException {

    private final int quantidadeAtual;
    private final int quantidadeSolicitada;
    private final int limiteMaximo;

    public LimiteAtivosExcedidoException(int quantidadeAtual, int quantidadeSolicitada, int limiteMaximo) {
        super(String.format(
                "Operação bloqueada pela Trava RN01 (Pequeno Parque <= %d ativos): a empresa possui %d ativo(s) cadastrado(s) e a solicitação de %d unidade(s) resultaria em %d unidades.",
                limiteMaximo, quantidadeAtual, quantidadeSolicitada, (quantidadeAtual + quantidadeSolicitada)
        ));
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeSolicitada = quantidadeSolicitada;
        this.limiteMaximo = limiteMaximo;
    }

    public LimiteAtivosExcedidoException(String message) {
        super(message);
        this.quantidadeAtual = 0;
        this.quantidadeSolicitada = 0;
        this.limiteMaximo = 100;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public int quantidadeAtual() {
        return quantidadeAtual;
    }

    public int getQuantidadeSolicitada() {
        return quantidadeSolicitada;
    }

    public int quantidadeASomar() {
        return quantidadeSolicitada;
    }

    public int getLimiteMaximo() {
        return limiteMaximo;
    }
}
