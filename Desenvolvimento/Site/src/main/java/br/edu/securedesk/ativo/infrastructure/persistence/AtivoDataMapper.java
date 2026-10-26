package br.edu.securedesk.ativo.infrastructure.persistence;

import br.edu.securedesk.ativo.domain.TipoAtivo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade de persistência (JPA / Data Mapper) mapeando a tabela tb_ativo_ti.
 * Atende ao inventário de ativos de TI para pequenas e médias empresas (escopo <= 100 ativos).
 */
@Entity
@Table(name = "tb_ativo_ti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtivoDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "nome_host", nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoAtivo tipo;

    @Column(length = 255)
    private String descricao;

    @Builder.Default
    @Column(nullable = false)
    private Integer quantidade = 1;

    @Column(length = 255)
    private String localizacao;

    @Column(length = 100)
    private String responsavel;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @Builder.Default
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDateTime.now();
        }
        if (this.quantidade == null) {
            this.quantidade = 1;
        }
        if (this.ativo == null) {
            this.ativo = true;
        }
    }

    public boolean isAtivo() {
        return Boolean.TRUE.equals(this.ativo);
    }
}
