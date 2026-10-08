package br.edu.securedesk.entity.diagnostico;

import jakarta.persistence.*;
import lombok.*;

/**
 * Alternativa de resposta vinculada a uma QuestaoDiagnostico,
 * associando cada escolha a um nível MIL e a um peso para cálculo do score.
 */
@Entity
@Table(name = "tb_opcao_resposta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpcaoResposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id", nullable = false)
    private QuestaoDiagnostico questao;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NivelMil nivelMil;

    /**
     * Peso numérico associado à opção (e.g. 0.0 para MIL 0, até 3.0 para MIL 3).
     */
    @Column(nullable = false)
    private Double pesoPontuacao;
}
