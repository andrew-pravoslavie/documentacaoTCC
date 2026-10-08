package br.edu.securedesk.entity.diagnostico;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Pergunta do questionário de diagnóstico do Framework Híbrido.
 * Corresponde a uma função do NIST e estabelece o critério mínimo de nivelamento MIL.
 */
@Entity
@Table(name = "tb_questao_diagnostico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestaoDiagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcao_nist_id", nullable = false)
    private FuncaoNist funcaoNist;

    /**
     * Identificador textual da questão, e.g. "GV-01", "ID-02", "PR-01".
     */
    @Column(nullable = false, length = 15)
    private String codigo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(columnDefinition = "TEXT")
    private String explicacaoAjuda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private NivelMil milAlvoMinimo = NivelMil.MIL_2;

    private Integer ordem;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OpcaoResposta> opcoes = new ArrayList<>();
}
