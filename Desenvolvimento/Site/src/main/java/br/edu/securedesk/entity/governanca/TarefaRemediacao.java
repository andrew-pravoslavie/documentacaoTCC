package br.edu.securedesk.entity.governanca;

import br.edu.securedesk.entity.core.Empresa;
import br.edu.securedesk.entity.core.Usuario;
import br.edu.securedesk.entity.diagnostico.FuncaoNist;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarefa de remediação gerada a partir de um Gap ou cadastrada manualmente no Kanban de Governança.
 */
@Entity
@Table(name = "tb_tarefa_remediacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarefaRemediacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gap_id")
    private Gap gap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcao_nist_id", nullable = false)
    private FuncaoNist funcaoNist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    @Column(nullable = false, length = 180)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusTarefa status = StatusTarefa.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Prioridade prioridade = Prioridade.ALTA;

    private LocalDate prazoLimite;

    private LocalDateTime dataConclusao;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Evidencia> evidencias = new ArrayList<>();
}
