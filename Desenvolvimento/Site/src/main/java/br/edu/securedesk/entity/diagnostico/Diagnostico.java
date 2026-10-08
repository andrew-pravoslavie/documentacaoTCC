package br.edu.securedesk.entity.diagnostico;

import br.edu.securedesk.entity.core.Empresa;
import br.edu.securedesk.entity.core.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Instância de avaliação de maturidade preenchida por uma empresa.
 * Permite registrar a linha de base inicial e acompanhar a evolução histórica.
 */
@Entity
@Table(name = "tb_diagnostico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_responsavel_id", nullable = false)
    private Usuario usuarioResponsavel;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dataRealizacao = LocalDateTime.now();

    /**
     * Score global de maturidade (percentual de 0.0 a 100.0%).
     */
    private Double scoreGlobal;

    /**
     * Nível médio MIL atingido (escala de 0.00 a 3.00).
     */
    private Double milMedio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusDiagnostico status = StatusDiagnostico.EM_ANDAMENTO;

    @OneToMany(mappedBy = "diagnostico", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RespostaDiagnostico> respostas = new ArrayList<>();

    @OneToMany(mappedBy = "diagnostico", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScoreFuncao> scoresPorFuncao = new ArrayList<>();
}
