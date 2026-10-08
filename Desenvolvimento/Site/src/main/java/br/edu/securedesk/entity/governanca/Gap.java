package br.edu.securedesk.entity.governanca;

import br.edu.securedesk.entity.diagnostico.Diagnostico;
import br.edu.securedesk.entity.diagnostico.FuncaoNist;
import br.edu.securedesk.entity.diagnostico.NivelMil;
import br.edu.securedesk.entity.diagnostico.QuestaoDiagnostico;
import jakarta.persistence.*;
import lombok.*;

/**
 * Representa um gap de segurança identificado no diagnóstico quando o resultado é inferior
 * ao patamar recomendado (MIL 2). Posiciona o item na Matriz de Risco (Impacto x Custo).
 */
@Entity
@Table(name = "tb_gap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gap {

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
    @JoinColumn(name = "funcao_nist_id", nullable = false)
    private FuncaoNist funcaoNist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NivelMil milAtual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private NivelMil milAlvo = NivelMil.MIL_2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private NivelImpacto impactoNegocio = NivelImpacto.ALTO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CustoMitigacao custoMitigacao = CustoMitigacao.MEDIO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Prioridade prioridadeMatriz = Prioridade.ALTA;

    @Column(columnDefinition = "TEXT")
    private String recomendacao;

    @Builder.Default
    @Column(nullable = false)
    private Boolean resolvido = false;
}
