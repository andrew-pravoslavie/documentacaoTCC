package br.edu.securedesk.entity.compliance;

import br.edu.securedesk.entity.core.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Trilha de auditoria das revisões e alterações realizadas em uma política de segurança da empresa.
 */
@Entity
@Table(name = "tb_historico_revisao_politica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoRevisaoPolitica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "politica_empresa_id", nullable = false)
    private PoliticaEmpresa politicaEmpresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revisor_id", nullable = false)
    private Usuario revisor;

    @Column(nullable = false, length = 10)
    private String versao;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataRevisao = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
