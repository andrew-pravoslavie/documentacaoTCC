package br.edu.securedesk.compliance.usecase.model;

import br.edu.securedesk.compliance.domain.StatusPolitica;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record de entrada para adoção, customização e revisão de Políticas da PME (CRUD 4).
 */
public record PoliticaRequestModel(
        @NotBlank(message = "O título da política é obrigatório.")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
        String titulo,

        String codigoTemplateOrigem,

        String normaReferencia,

        String funcaoNistSigla,

        @NotBlank(message = "O conteúdo da política não pode ficar em branco.")
        String conteudoCustomizado,

        StatusPolitica status,

        @Min(value = 1, message = "A periodicidade mínima é de 1 mês.")
        @Max(value = 60, message = "A periodicidade máxima é de 60 meses.")
        Integer periodicidadeMesesRevisao,

        String revisorNome,

        String observacoesRevisao,

        Long empresaId
) {
    public PoliticaRequestModel {
        if (normaReferencia == null || normaReferencia.isBlank()) {
            normaReferencia = "ISO/IEC 27001 & LGPD";
        }
        if (funcaoNistSigla == null || funcaoNistSigla.isBlank()) {
            funcaoNistSigla = "GV";
        }
        if (status == null) {
            status = StatusPolitica.EM_DIA;
        }
        if (periodicidadeMesesRevisao == null) {
            periodicidadeMesesRevisao = 6;
        }
        if (revisorNome == null || revisorNome.isBlank()) {
            revisorNome = "Gabriel Andrew";
        }
        if (empresaId == null) {
            empresaId = 1L;
        }
    }

    public static PoliticaRequestModel padrao(Long empresaId) {
        return new PoliticaRequestModel(
                "",
                "PSI",
                "ISO/IEC 27001, ISO/IEC 27002 & LGPD",
                "GV",
                "1. OBJETIVO\nEstabelecer diretrizes de segurança da informação para o pequeno parque tecnológico da PME...",
                StatusPolitica.EM_ELABORACAO,
                6,
                "Gabriel Andrew",
                "Adoção inicial de política",
                empresaId
        );
    }

    public static PoliticaRequestModel fromResponse(PoliticaResponseModel resp) {
        return new PoliticaRequestModel(
                resp.titulo(),
                resp.codigoTemplateOrigem(),
                resp.normaReferencia(),
                resp.funcaoNistSigla(),
                resp.conteudoCustomizado(),
                resp.status(),
                resp.periodicidadeMesesRevisao(),
                "Gabriel Andrew",
                "",
                resp.empresaId()
        );
    }

    public String getTitulo() { return titulo; }
    public String getCodigoTemplateOrigem() { return codigoTemplateOrigem; }
    public String getNormaReferencia() { return normaReferencia; }
    public String getFuncaoNistSigla() { return funcaoNistSigla; }
    public String getConteudoCustomizado() { return conteudoCustomizado; }
    public StatusPolitica getStatus() { return status; }
    public Integer getPeriodicidadeMesesRevisao() { return periodicidadeMesesRevisao; }
    public String getRevisorNome() { return revisorNome; }
    public String getObservacoesRevisao() { return observacoesRevisao; }
    public Long getEmpresaId() { return empresaId; }
}
