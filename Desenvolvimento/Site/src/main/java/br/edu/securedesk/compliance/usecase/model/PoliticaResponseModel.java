package br.edu.securedesk.compliance.usecase.model;

import br.edu.securedesk.compliance.domain.HistoricoRevisao;
import br.edu.securedesk.compliance.domain.PoliticaEmpresa;
import br.edu.securedesk.compliance.domain.StatusPolitica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Record de saída para Política de Segurança da PME e seu histórico de revisões.
 */
public record PoliticaResponseModel(
        Long id,
        Long empresaId,
        String codigoTemplateOrigem,
        String titulo,
        String normaReferencia,
        String funcaoNistSigla,
        String conteudoCustomizado,
        String versaoAtual,
        StatusPolitica status,
        LocalDate dataAprovacao,
        LocalDate dataUltimaRevisao,
        Integer periodicidadeMesesRevisao,
        LocalDate proximaRevisao,
        List<RevisaoItemModel> historicoRevisoes
) {
    public record RevisaoItemModel(
            Long id,
            String versao,
            String revisorNome,
            String observacoes,
            LocalDateTime dataRevisao
    ) {
        public static RevisaoItemModel fromDomain(HistoricoRevisao h) {
            return new RevisaoItemModel(
                    h.getId(),
                    h.getVersao(),
                    h.getRevisorNome(),
                    h.getObservacoes(),
                    h.getDataRevisao()
            );
        }
    }

    public static PoliticaResponseModel fromDomain(PoliticaEmpresa p) {
        List<RevisaoItemModel> revs = p.getHistoricoRevisoes() != null
                ? p.getHistoricoRevisoes().stream().map(RevisaoItemModel::fromDomain).toList()
                : List.of();
        return new PoliticaResponseModel(
                p.getId(),
                p.getEmpresaId(),
                p.getCodigoTemplateOrigem(),
                p.getTitulo(),
                p.getNormaReferencia(),
                p.getFuncaoNistSigla(),
                p.getConteudoCustomizado(),
                p.getVersaoAtual(),
                p.getStatus(),
                p.getDataAprovacao(),
                p.getDataUltimaRevisao(),
                p.getPeriodicidadeMesesRevisao(),
                p.getProximaRevisao(),
                revs
        );
    }
}
