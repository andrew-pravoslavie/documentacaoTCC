package br.edu.securedesk.governanca.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade de domínio pura (POJO sem JPA) representando uma Tarefa de Remediação do Kanban (CRUD 3).
 * Vinculada às funções do NIST CSF 2.0 (GV, ID, PR, DE, RS, RC), Gap opcional e lista de Evidências.
 */
public class TarefaRemediacao {

    private Long id;
    private Long empresaId;
    private Long gapId;
    private String funcaoNistSigla;
    private String titulo;
    private String descricao;
    private String responsavelNome;
    private StatusTarefa status;
    private Prioridade prioridade;
    private LocalDate prazoLimite;
    private LocalDateTime dataConclusao;
    private LocalDateTime dataCriacao;
    private List<Evidencia> evidencias;

    public TarefaRemediacao() {
        this.status = StatusTarefa.BACKLOG;
        this.prioridade = Prioridade.ALTA;
        this.funcaoNistSigla = "PR";
        this.dataCriacao = LocalDateTime.now();
        this.evidencias = new ArrayList<>();
    }

    public TarefaRemediacao(Long id, Long empresaId, Long gapId, String funcaoNistSigla,
                            String titulo, String descricao, String responsavelNome,
                            StatusTarefa status, Prioridade prioridade, LocalDate prazoLimite,
                            LocalDateTime dataConclusao, LocalDateTime dataCriacao,
                            List<Evidencia> evidencias) {
        this.id = id;
        this.empresaId = empresaId;
        this.gapId = gapId;
        this.funcaoNistSigla = (funcaoNistSigla != null && !funcaoNistSigla.isBlank()) ? funcaoNistSigla.toUpperCase() : "PR";
        this.titulo = titulo;
        this.descricao = descricao;
        this.responsavelNome = responsavelNome;
        this.status = status != null ? status : StatusTarefa.BACKLOG;
        this.prioridade = prioridade != null ? prioridade : Prioridade.ALTA;
        this.prazoLimite = prazoLimite;
        this.dataConclusao = dataConclusao;
        this.dataCriacao = dataCriacao != null ? dataCriacao : LocalDateTime.now();
        this.evidencias = evidencias != null ? new ArrayList<>(evidencias) : new ArrayList<>();
        validarInvariantes();
    }

    public final void validarInvariantes() {
        if (empresaId == null || empresaId <= 0) {
            throw new IllegalArgumentException("O identificador da empresa (empresaId) é obrigatório.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título da tarefa de remediação é obrigatório.");
        }
        if (funcaoNistSigla == null || funcaoNistSigla.isBlank()) {
            throw new IllegalArgumentException("A função NIST (GV, ID, PR, DE, RS, RC) é obrigatória.");
        }
        if (status == null) {
            throw new IllegalArgumentException("O status da tarefa é obrigatório.");
        }
        if (prioridade == null) {
            throw new IllegalArgumentException("A prioridade da tarefa é obrigatória.");
        }
    }

    /**
     * Realiza a transição de status no Kanban com validação de regras de ciclo de vida.
     */
    public void transicionarPara(StatusTarefa novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("O novo status não pode ser nulo.");
        }
        if (this.status == StatusTarefa.CANCELADA && novoStatus != StatusTarefa.BACKLOG && novoStatus != StatusTarefa.PENDENTE) {
            throw new IllegalStateException("Uma tarefa cancelada só pode ser reaberta para BACKLOG/PENDENTE.");
        }
        this.status = novoStatus;
        if (novoStatus.isConcluido()) {
            this.dataConclusao = LocalDateTime.now();
        } else {
            this.dataConclusao = null;
        }
    }

    public void iniciarAndamento() {
        transicionarPara(StatusTarefa.EM_ANDAMENTO);
    }

    public void concluir() {
        transicionarPara(StatusTarefa.CONCLUIDO);
    }

    public void cancelar() {
        transicionarPara(StatusTarefa.CANCELADA);
    }

    public void adicionarEvidencia(Evidencia evidencia) {
        if (evidencia == null) {
            throw new IllegalArgumentException("A evidência não pode ser nula.");
        }
        this.evidencias.add(evidencia);
    }

    public boolean isAtrasada() {
        return !status.isConcluido()
                && status != StatusTarefa.CANCELADA
                && prazoLimite != null
                && prazoLimite.isBefore(LocalDate.now());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public Long getGapId() { return gapId; }
    public void setGapId(Long gapId) { this.gapId = gapId; }
    public String getFuncaoNistSigla() { return funcaoNistSigla; }
    public void setFuncaoNistSigla(String funcaoNistSigla) { this.funcaoNistSigla = funcaoNistSigla; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getResponsavelNome() { return responsavelNome; }
    public void setResponsavelNome(String responsavelNome) { this.responsavelNome = responsavelNome; }
    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }
    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }
    public LocalDate getPrazoLimite() { return prazoLimite; }
    public void setPrazoLimite(LocalDate prazoLimite) { this.prazoLimite = prazoLimite; }
    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public List<Evidencia> getEvidencias() { return evidencias; }
    public void setEvidencias(List<Evidencia> evidencias) { this.evidencias = evidencias; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TarefaRemediacao that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
