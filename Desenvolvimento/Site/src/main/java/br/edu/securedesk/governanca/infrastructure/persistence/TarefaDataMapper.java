package br.edu.securedesk.governanca.infrastructure.persistence;

import br.edu.securedesk.governanca.domain.Prioridade;
import br.edu.securedesk.governanca.domain.StatusTarefa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade de persistência (JPA / Data Mapper) mapeando a tabela tb_tarefa_remediacao_clean.
 * Sustenta o Kanban de Governança Operacional (CRUD 3). As evidências ficam em tabela própria
 * (EvidenciaDataMapper) e são associadas apenas pelo identificador da tarefa.
 */
@Entity
@Table(name = "tb_tarefa_remediacao_clean")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarefaDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "gap_id")
    private Long gapId;

    @Column(name = "funcao_nist_sigla", nullable = false, length = 2)
    private String funcaoNistSigla;

    @Column(nullable = false, length = 180)
    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @Column(name = "responsavel_nome", length = 100)
    private String responsavelNome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTarefa status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Prioridade prioridade;

    @Column(name = "prazo_limite")
    private LocalDate prazoLimite;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
    }
}
