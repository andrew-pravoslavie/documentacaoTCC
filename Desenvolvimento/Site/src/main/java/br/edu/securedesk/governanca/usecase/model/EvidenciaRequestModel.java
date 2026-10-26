package br.edu.securedesk.governanca.usecase.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record de entrada para anexar Evidência comprobatória a uma tarefa de remediação.
 */
public record EvidenciaRequestModel(
        @NotBlank(message = "O nome do arquivo de evidência é obrigatório.")
        @Size(max = 180, message = "O nome do arquivo deve ter no máximo 180 caracteres.")
        String nomeArquivo,

        String tipoArquivo,

        Long tamanhoBytes,

        String descricao,

        String autorUpload
) {
    public EvidenciaRequestModel {
        if (tipoArquivo == null || tipoArquivo.isBlank()) {
            tipoArquivo = "application/pdf";
        }
        if (tamanhoBytes == null || tamanhoBytes <= 0) {
            tamanhoBytes = 102400L;
        }
        if (autorUpload == null || autorUpload.isBlank()) {
            autorUpload = "Gabriel Andrew";
        }
    }

    public String getNomeArquivo() { return nomeArquivo; }
    public String getTipoArquivo() { return tipoArquivo; }
    public Long getTamanhoBytes() { return tamanhoBytes; }
    public String getDescricao() { return descricao; }
    public String getAutorUpload() { return autorUpload; }
}
