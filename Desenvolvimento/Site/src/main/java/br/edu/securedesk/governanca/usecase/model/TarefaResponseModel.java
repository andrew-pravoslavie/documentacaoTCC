package br.edu.securedesk.governanca.usecase.model;

import br.edu.securedesk.governanca.domain.Evidencia;
import br.edu.securedesk.governanca.domain.Prioridade;
import br.edu.securedesk.governanca.domain.StatusTarefa;
import br.edu.securedesk.governanca.domain.TarefaRemediacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Record de saída para TarefaRemediacao e suas Evidências.
 */
public record TarefaResponseModel(
        Long id,
        Long empresaId,
        Long gapId,
        String funcaoNistSigla,
        String titulo,
        String descricao,
        String responsavelNome,
        StatusTarefa status,
        Prioridade prioridade,
        LocalDate prazoLimite,
        LocalDateTime dataConclusao,
        LocalDateTime dataCriacao,
        boolean atrasada,
        List<EvidenciaItemModel> evidencias
) {
    public record EvidenciaItemModel(
            Long id,
            String nomeArquivo,
            String tipoArquivo,
            Long tamanhoBytes,
            String descricao,
            String autorUpload,
            LocalDateTime dataUpload
    ) {
        public static EvidenciaItemModel fromDomain(Evidencia e) {
            return new EvidenciaItemModel(
                    e.getId(),
                    e.getNomeArquivo(),
                    e.getTipoArquivo(),
                    e.getTamanhoBytes(),
                    e.getDescricao(),
                    e.getAutorUpload(),
                    e.getDataUpload()
            );
        }
    }

    public static TarefaResponseModel fromDomain(TarefaRemediacao t) {
        List<EvidenciaItemModel> evs = t.getEvidencias() != null
                ? t.getEvidencias().stream().map(EvidenciaItemModel::fromDomain).toList()
                : List.of();
        return new TarefaResponseModel(
                t.getId(),
                t.getEmpresaId(),
                t.getGapId(),
                t.getFuncaoNistSigla(),
                t.getTitulo(),
                t.getDescricao(),
                t.getResponsavelNome(),
                t.getStatus(),
                t.getPrioridade(),
                t.getPrazoLimite(),
                t.getDataConclusao(),
                t.getDataCriacao(),
                t.isAtrasada(),
                evs
        );
    }
}
