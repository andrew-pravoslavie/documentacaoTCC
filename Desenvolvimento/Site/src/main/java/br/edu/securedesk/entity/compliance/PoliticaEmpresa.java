package br.edu.securedesk.entity.compliance;

import br.edu.securedesk.entity.core.Empresa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Política de segurança adotada e customizada por uma empresa cliente,
 * com ciclo de vida, vigência e agendamento de revisões periódicas.
 */
@Entity
@Table(name = "tb_politica_empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoliticaEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_origem_id")
    private TemplatePolitica templateOrigem;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "LONGTEXT")
    private String conteudoCustomizado;

    @Builder.Default
    @Column(nullable = false, length = 10)
    private String versaoAtual = "1.0";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusPolitica status = StatusPolitica.EM_DIA;

    private LocalDate dataAprovacao;

    private LocalDate dataUltimaRevisao;

    @Builder.Default
    @Column(nullable = false)
    private Integer periodicidadeMesesRevisao = 6;

    private LocalDate proximaRevisao;

    @OneToMany(mappedBy = "politicaEmpresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HistoricoRevisaoPolitica> historicoRevisoes = new ArrayList<>();
}
