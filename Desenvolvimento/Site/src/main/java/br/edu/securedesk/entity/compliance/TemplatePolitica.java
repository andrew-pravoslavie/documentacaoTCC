package br.edu.securedesk.entity.compliance;

import br.edu.securedesk.entity.diagnostico.FuncaoNist;
import jakarta.persistence.*;
import lombok.*;

/**
 * Modelos base de políticas fornecidos pelo sistema (PSI, Controle de Acesso, Backup, LGPD, etc.)
 * alinhados à ISO 27001/27002 e LGPD (atende aos entregáveis OED 2 e OED 8).
 */
@Entity
@Table(name = "tb_template_politica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplatePolitica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcao_nist_id")
    private FuncaoNist funcaoNist;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    /**
     * Normas de referência (e.g. "ISO 27001 / LGPD", "ISO 27002").
     */
    @Column(length = 80)
    private String normaReferencia;

    @Column(columnDefinition = "LONGTEXT")
    private String conteudoPadrao;

    @Column(length = 255)
    private String arquivoModeloUrl;

    @Column(length = 30)
    private String icone;
}
