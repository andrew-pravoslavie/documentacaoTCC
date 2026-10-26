package br.edu.securedesk.compliance.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade de domínio pura (POJO sem JPA) representando uma Política de Segurança adotada/customizada
 * pela PME (CRUD 4 — formalização documental exigida pelo nível MIL 2 do C2M2).
 */
public class PoliticaEmpresa {

    private Long id;
    private Long empresaId;
    private String codigoTemplateOrigem;
    private String titulo;
    private String normaReferencia;
    private String funcaoNistSigla;
    private String conteudoCustomizado;
    private String versaoAtual;
    private StatusPolitica status;
    private LocalDate dataAprovacao;
    private LocalDate dataUltimaRevisao;
    private Integer periodicidadeMesesRevisao;
    private LocalDate proximaRevisao;
    private List<HistoricoRevisao> historicoRevisoes;

    public PoliticaEmpresa() {
        this.versaoAtual = "1.0";
        this.status = StatusPolitica.EM_ELABORACAO;
        this.periodicidadeMesesRevisao = 6;
        this.historicoRevisoes = new ArrayList<>();
    }

    public PoliticaEmpresa(Long id, Long empresaId, String codigoTemplateOrigem, String titulo,
                           String normaReferencia, String funcaoNistSigla, String conteudoCustomizado,
                           String versaoAtual, StatusPolitica status, LocalDate dataAprovacao,
                           LocalDate dataUltimaRevisao, Integer periodicidadeMesesRevisao,
                           LocalDate proximaRevisao, List<HistoricoRevisao> historicoRevisoes) {
        this.id = id;
        this.empresaId = empresaId;
        this.codigoTemplateOrigem = codigoTemplateOrigem;
        this.titulo = titulo;
        this.normaReferencia = (normaReferencia != null && !normaReferencia.isBlank()) ? normaReferencia : "ISO/IEC 27001 & LGPD";
        this.funcaoNistSigla = (funcaoNistSigla != null && !funcaoNistSigla.isBlank()) ? funcaoNistSigla.toUpperCase() : "GV";
        this.conteudoCustomizado = conteudoCustomizado;
        this.versaoAtual = (versaoAtual != null && !versaoAtual.isBlank()) ? versaoAtual : "1.0";
        this.status = status != null ? status : StatusPolitica.EM_ELABORACAO;
        this.dataAprovacao = dataAprovacao;
        this.dataUltimaRevisao = dataUltimaRevisao != null ? dataUltimaRevisao : LocalDate.now();
        this.periodicidadeMesesRevisao = (periodicidadeMesesRevisao != null && periodicidadeMesesRevisao > 0) ? periodicidadeMesesRevisao : 6;
        this.proximaRevisao = proximaRevisao != null ? proximaRevisao : this.dataUltimaRevisao.plusMonths(this.periodicidadeMesesRevisao);
        this.historicoRevisoes = historicoRevisoes != null ? new ArrayList<>(historicoRevisoes) : new ArrayList<>();
        validarInvariantes();
    }

    public final void validarInvariantes() {
        if (empresaId == null || empresaId <= 0) {
            throw new IllegalArgumentException("O identificador da empresa (empresaId) é obrigatório.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título da política de segurança é obrigatório.");
        }
        if (conteudoCustomizado == null || conteudoCustomizado.isBlank()) {
            throw new IllegalArgumentException("O conteúdo da política não pode estar em branco.");
        }
        if (periodicidadeMesesRevisao == null || periodicidadeMesesRevisao < 1 || periodicidadeMesesRevisao > 60) {
            throw new IllegalArgumentException("A periodicidade de revisão deve estar entre 1 e 60 meses.");
        }
    }

    /**
     * Registra uma nova revisão e incrementa automaticamente a versão secundária (ex: 1.0 -> 1.1),
     * atualizando o status para EM_DIA e recalculando a próxima revisão.
     */
    public HistoricoRevisao registrarNovaRevisao(String novoConteudo, String revisorNome, String observacoes) {
        if (novoConteudo != null && !novoConteudo.isBlank()) {
            this.conteudoCustomizado = novoConteudo;
        }
        this.versaoAtual = incrementarVersao(this.versaoAtual);
        this.dataUltimaRevisao = LocalDate.now();
        if (this.dataAprovacao == null) {
            this.dataAprovacao = LocalDate.now();
        }
        this.proximaRevisao = this.dataUltimaRevisao.plusMonths(this.periodicidadeMesesRevisao);
        this.status = StatusPolitica.EM_DIA;

        HistoricoRevisao revisao = new HistoricoRevisao(
                null,
                this.id,
                this.versaoAtual,
                revisorNome,
                observacoes != null && !observacoes.isBlank() ? observacoes : "Revisão e atualização da política",
                LocalDateTime.now()
        );
        this.historicoRevisoes.add(revisao);
        return revisao;
    }

    public void avaliarVencimento() {
        if (proximaRevisao != null && proximaRevisao.isBefore(LocalDate.now())) {
            this.status = StatusPolitica.VENCIDA;
        } else if (proximaRevisao != null && !proximaRevisao.isAfter(LocalDate.now().plusDays(15)) && this.status == StatusPolitica.EM_DIA) {
            this.status = StatusPolitica.PENDENTE_REVISAO;
        }
    }

    private String incrementarVersao(String versao) {
        try {
            String[] partes = versao.split("\\.");
            int major = Integer.parseInt(partes[0]);
            int minor = partes.length > 1 ? Integer.parseInt(partes[1]) + 1 : 1;
            return major + "." + minor;
        } catch (Exception e) {
            return "1.1";
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public String getCodigoTemplateOrigem() { return codigoTemplateOrigem; }
    public void setCodigoTemplateOrigem(String codigoTemplateOrigem) { this.codigoTemplateOrigem = codigoTemplateOrigem; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getNormaReferencia() { return normaReferencia; }
    public void setNormaReferencia(String normaReferencia) { this.normaReferencia = normaReferencia; }
    public String getFuncaoNistSigla() { return funcaoNistSigla; }
    public void setFuncaoNistSigla(String funcaoNistSigla) { this.funcaoNistSigla = funcaoNistSigla; }
    public String getConteudoCustomizado() { return conteudoCustomizado; }
    public void setConteudoCustomizado(String conteudoCustomizado) { this.conteudoCustomizado = conteudoCustomizado; }
    public String getVersaoAtual() { return versaoAtual; }
    public void setVersaoAtual(String versaoAtual) { this.versaoAtual = versaoAtual; }
    public StatusPolitica getStatus() { return status; }
    public void setStatus(StatusPolitica status) { this.status = status; }
    public LocalDate getDataAprovacao() { return dataAprovacao; }
    public void setDataAprovacao(LocalDate dataAprovacao) { this.dataAprovacao = dataAprovacao; }
    public LocalDate getDataUltimaRevisao() { return dataUltimaRevisao; }
    public void setDataUltimaRevisao(LocalDate dataUltimaRevisao) { this.dataUltimaRevisao = dataUltimaRevisao; }
    public Integer getPeriodicidadeMesesRevisao() { return periodicidadeMesesRevisao; }
    public void setPeriodicidadeMesesRevisao(Integer periodicidadeMesesRevisao) { this.periodicidadeMesesRevisao = periodicidadeMesesRevisao; }
    public LocalDate getProximaRevisao() { return proximaRevisao; }
    public void setProximaRevisao(LocalDate proximaRevisao) { this.proximaRevisao = proximaRevisao; }
    public List<HistoricoRevisao> getHistoricoRevisoes() { return historicoRevisoes; }
    public void setHistoricoRevisoes(List<HistoricoRevisao> historicoRevisoes) { this.historicoRevisoes = historicoRevisoes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PoliticaEmpresa that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
