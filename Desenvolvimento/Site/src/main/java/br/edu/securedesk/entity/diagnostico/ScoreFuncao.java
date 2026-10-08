package br.edu.securedesk.entity.diagnostico;

import jakarta.persistence.*;
import lombok.*;

/**
 * Armazena a pontuação agregada por função do NIST (GV, ID, PR, DE, RS, RC)
 * para um dado Diagnóstico, alimentando diretamente o Gráfico Radar.
 */
@Entity
@Table(name = "tb_score_funcao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreFuncao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnostico_id", nullable = false)
    private Diagnostico diagnostico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcao_nist_id", nullable = false)
    private FuncaoNist funcaoNist;

    /**
     * Score percentual obtido na função (0.0 a 100.0%).
     */
    @Column(nullable = false)
    private Double scorePercentual;

    /**
     * Nível MIL equivalente calculado para a função (0.00 a 3.00).
     */
    @Column(nullable = false)
    private Double milCalculado;
}
