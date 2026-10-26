package br.edu.securedesk.ativo.usecase.model;

import br.edu.securedesk.ativo.domain.Ativo;
import br.edu.securedesk.ativo.domain.TipoAtivo;

/**
 * Record de saída (Response Model) da fronteira de casos de uso de Ativos.
 */
public record AtivoResponseModel(
        Long id,
        String nome,
        TipoAtivo tipo,
        String descricao,
        Integer quantidade,
        String localizacao,
        String responsavel,
        boolean ativo,
        Long empresaId
) {
    public static AtivoResponseModel fromDomain(Ativo domain) {
        return new AtivoResponseModel(
                domain.getId(),
                domain.getNome(),
                domain.getTipo(),
                domain.getDescricao(),
                domain.getQuantidade(),
                domain.getLocalizacao(),
                domain.getResponsavel(),
                domain.isAtivo(),
                domain.getEmpresaId()
        );
    }
}
