package br.edu.securedesk.compliance.infrastructure.persistence;

import br.edu.securedesk.compliance.domain.StatusPolitica;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade de persistência (JPA / Data Mapper) mapeando a tabela tb_politica_empresa_clean.
 * Guarda as políticas de segurança adotadas ou customizadas pela PME (CRUD 4). O histórico de
 * revisões fica em tabela própria (HistoricoRevisaoDataMapper), associado pelo id da política.
 */
@Entity
@Table(name = "tb_politica_empresa_clean")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoliticaDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "codigo_template_origem", length = 30)
    private String codigoTemplateOrigem;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(name = "norma_referencia", length = 200)
    private String normaReferencia;

    @Column(name = "funcao_nist_sigla", nullable = false, length = 2)
    private String funcaoNistSigla;

    @Column(name = "conteudo_customizado", nullable = false, columnDefinition = "TEXT")
    private String conteudoCustomizado;

    @Column(name = "versao_atual", nullable = false, length = 20)
    private String versaoAtual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPolitica status;

    @Column(name = "data_aprovacao")
    private LocalDate dataAprovacao;

    @Column(name = "data_ultima_revisao")
    private LocalDate dataUltimaRevisao;

    @Column(name = "periodicidade_meses_revisao", nullable = false)
    private Integer periodicidadeMesesRevisao;

    @Column(name = "proxima_revisao")
    private LocalDate proximaRevisao;
}
