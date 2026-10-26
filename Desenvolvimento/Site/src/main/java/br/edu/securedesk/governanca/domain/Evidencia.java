package br.edu.securedesk.governanca.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio pura representando uma Evidência comprobatória anexada a uma TarefaRemediacao (requisito MIL 2).
 */
public class Evidencia {

    private Long id;
    private Long tarefaId;
    private String nomeArquivo;
    private String tipoArquivo;
    private Long tamanhoBytes;
    private String caminhoArquivo;
    private String descricao;
    private String autorUpload;
    private LocalDateTime dataUpload;

    public Evidencia() {
        this.dataUpload = LocalDateTime.now();
    }

    public Evidencia(Long id, Long tarefaId, String nomeArquivo, String tipoArquivo,
                     Long tamanhoBytes, String caminhoArquivo, String descricao,
                     String autorUpload, LocalDateTime dataUpload) {
        this.id = id;
        this.tarefaId = tarefaId;
        this.nomeArquivo = nomeArquivo;
        this.tipoArquivo = tipoArquivo != null ? tipoArquivo : "application/octet-stream";
        this.tamanhoBytes = tamanhoBytes != null ? tamanhoBytes : 0L;
        this.caminhoArquivo = (caminhoArquivo != null && !caminhoArquivo.isBlank()) ? caminhoArquivo : "/uploads/" + nomeArquivo;
        this.descricao = descricao;
        this.autorUpload = (autorUpload != null && !autorUpload.isBlank()) ? autorUpload : "Administrador";
        this.dataUpload = dataUpload != null ? dataUpload : LocalDateTime.now();
        validarInvariantes();
    }

    public final void validarInvariantes() {
        if (tarefaId == null || tarefaId <= 0) {
            throw new IllegalArgumentException("O identificador da tarefa (tarefaId) é obrigatório para a evidência.");
        }
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            throw new IllegalArgumentException("O nome do arquivo de evidência é obrigatório.");
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTarefaId() { return tarefaId; }
    public void setTarefaId(Long tarefaId) { this.tarefaId = tarefaId; }
    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }
    public Long getTamanhoBytes() { return tamanhoBytes; }
    public void setTamanhoBytes(Long tamanhoBytes) { this.tamanhoBytes = tamanhoBytes; }
    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getAutorUpload() { return autorUpload; }
    public void setAutorUpload(String autorUpload) { this.autorUpload = autorUpload; }
    public LocalDateTime getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDateTime dataUpload) { this.dataUpload = dataUpload; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evidencia evidencia)) return false;
        return Objects.equals(id, evidencia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
