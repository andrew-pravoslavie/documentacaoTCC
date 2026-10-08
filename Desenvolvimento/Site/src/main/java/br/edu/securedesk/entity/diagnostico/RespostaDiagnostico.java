package br.edu.securedesk.entity.diagnostico;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Resposta específica fornecida para uma questão dentro de um Diagnóstico.
 */
@Entity
@Table(name = "tb_resposta_diagnostico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespostaDiagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnostico_id", nullable = false)
    private Diagnostico diagnostico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id", nullable = false)
    private QuestaoDiagnostico questao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcao_resposta_id", nullable = false)
    private OpcaoResposta opcaoEscolhida;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dataResposta = LocalDateTime.now();
}
