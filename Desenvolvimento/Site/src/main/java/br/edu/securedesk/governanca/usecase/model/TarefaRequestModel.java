package br.edu.securedesk.governanca.usecase.model;

import br.edu.securedesk.governanca.domain.Prioridade;
import br.edu.securedesk.governanca.domain.StatusTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Record de entrada para criação e edição de TarefaRemediacao (CRUD 3).
 */
public record TarefaRequestModel(
        @NotBlank(message = "O título da tarefa é obrigatório.")
        @Size(max = 180, message = "O título deve ter no máximo 180 caracteres.")
        String titulo,

        String descricao,

        @NotBlank(message = "A função NIST é obrigatória.")
        String funcaoNistSigla,

        String responsavelNome,

        StatusTarefa status,

        @NotNull(message = "A prioridade é obrigatória.")
        Prioridade prioridade,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate prazoLimite,

        Long gapId,

        Long empresaId
) {
    public TarefaRequestModel {
        if (funcaoNistSigla == null || funcaoNistSigla.isBlank()) {
            funcaoNistSigla = "PR";
        }
        if (status == null) {
            status = StatusTarefa.BACKLOG;
        }
        if (prioridade == null) {
            prioridade = Prioridade.ALTA;
        }
        if (empresaId == null) {
            empresaId = 1L;
        }
    }

    public static TarefaRequestModel padrao(Long empresaId) {
        return new TarefaRequestModel("", "", "PR", "Gabriel Andrew",
                StatusTarefa.BACKLOG, Prioridade.ALTA, LocalDate.now().plusDays(15), null, empresaId);
    }

    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getFuncaoNistSigla() { return funcaoNistSigla; }
    public String getResponsavelNome() { return responsavelNome; }
    public StatusTarefa getStatus() { return status; }
    public Prioridade getPrioridade() { return prioridade; }
    public LocalDate getPrazoLimite() { return prazoLimite; }
    public Long getGapId() { return gapId; }
    public Long getEmpresaId() { return empresaId; }
}
