package br.edu.securedesk.compliance.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade de persistência (JPA / Data Mapper) mapeando a tabela tb_historico_revisao_clean.
 * Cada linha representa uma versão registrada no ciclo de revisão de uma política.
 */
@Entity
@Table(name = "tb_historico_revisao_clean")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoRevisaoDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "politica_empresa_id", nullable = false)
    private Long politicaEmpresaId;

    @Column(nullable = false, length = 20)
    private String versao;

    @Column(name = "revisor_nome", length = 100)
    private String revisorNome;

    @Column(length = 1000)
    private String observacoes;

    @Column(name = "data_revisao", nullable = false, updatable = false)
    private LocalDateTime dataRevisao;

    @PrePersist
    public void prePersist() {
        if (this.dataRevisao == null) {
            this.dataRevisao = LocalDateTime.now();
        }
    }
}
