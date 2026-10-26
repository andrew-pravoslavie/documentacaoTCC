package br.edu.securedesk.ativo.domain;

import java.util.Objects;

/**
 * Entidade de domínio pura (POJO sem anotações de framework/JPA) representando um Ativo de TI
 * do pequeno parque tecnológico da PME (limite de até 100 unidades gerenciadas - RN01).
 */
public class Ativo {

    public static final int LIMITE_MAXIMO_PARQUE = 100;

    private Long id;
    private String nome;
    private TipoAtivo tipo;
    private String descricao;
    private Integer quantidade;
    private String localizacao;
    private String responsavel;
    private boolean ativo;
    private Long empresaId;

    public Ativo() {
        this.quantidade = 1;
        this.ativo = true;
    }

    public Ativo(Long id, String nome, TipoAtivo tipo, String descricao, Integer quantidade,
                 String localizacao, String responsavel, boolean ativo, Long empresaId) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.quantidade = quantidade != null ? quantidade : 1;
        this.localizacao = localizacao;
        this.responsavel = responsavel;
        this.ativo = ativo;
        this.empresaId = empresaId;
        validarInvariantes();
    }

    /**
     * Valida as invariantes intrínsecas da entidade de domínio.
     */
    public final void validarInvariantes() {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do ativo é obrigatório.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("A categoria/tipo do ativo é obrigatória.");
        }
        if (empresaId == null || empresaId <= 0) {
            throw new IllegalArgumentException("O identificador da empresa (empresaId) é obrigatório.");
        }
        if (quantidade == null || quantidade < 1) {
            throw new IllegalArgumentException("A quantidade de ativos deve ser de pelo menos 1 unidade.");
        }
        if (quantidade > LIMITE_MAXIMO_PARQUE) {
            throw new LimiteAtivosExcedidoException(0, quantidade, LIMITE_MAXIMO_PARQUE);
        }
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoAtivo getTipo() {
        return tipo;
    }

    public void setTipo(TipoAtivo tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ativo that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
