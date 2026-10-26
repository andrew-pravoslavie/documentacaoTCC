package br.edu.securedesk.compliance.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio pura representando um registro no Histórico de Revisão de uma Política (CRUD 4).
 */
public class HistoricoRevisao {

    private Long id;
    private Long politicaEmpresaId;
    private String versao;
    private String revisorNome;
    private String observacoes;
    private LocalDateTime dataRevisao;

    public HistoricoRevisao() {
        this.dataRevisao = LocalDateTime.now();
    }

    public HistoricoRevisao(Long id, Long politicaEmpresaId, String versao,
                            String revisorNome, String observacoes, LocalDateTime dataRevisao) {
        this.id = id;
        this.politicaEmpresaId = politicaEmpresaId;
        this.versao = (versao != null && !versao.isBlank()) ? versao : "1.0";
        this.revisorNome = (revisorNome != null && !revisorNome.isBlank()) ? revisorNome : "Administrador";
        this.observacoes = observacoes;
        this.dataRevisao = dataRevisao != null ? dataRevisao : LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPoliticaEmpresaId() { return politicaEmpresaId; }
    public void setPoliticaEmpresaId(Long politicaEmpresaId) { this.politicaEmpresaId = politicaEmpresaId; }
    public String getVersao() { return versao; }
    public void setVersao(String versao) { this.versao = versao; }
    public String getRevisorNome() { return revisorNome; }
    public void setRevisorNome(String revisorNome) { this.revisorNome = revisorNome; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getDataRevisao() { return dataRevisao; }
    public void setDataRevisao(LocalDateTime dataRevisao) { this.dataRevisao = dataRevisao; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricoRevisao that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
