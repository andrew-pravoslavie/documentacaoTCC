package br.edu.securedesk.entity.ativo;

import br.edu.securedesk.entity.core.Empresa;
import br.edu.securedesk.entity.governanca.Prioridade;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Representa um dispositivo ou ativo tecnológico integrante do pequeno parque da PME.
 * Permite manter o inventário formalizado (requisito do NIST GV-02 e controle de escopo < 100 ativos).
 */
@Entity
@Table(name = "tb_ativo_ti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtivoTI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(nullable = false, length = 100)
    private String nomeHost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private TipoAtivo tipo;

    @Column(length = 80)
    private String sistemaOperacional;

    @Column(length = 50)
    private String ipOuMac;

    @Column(length = 100)
    private String responsavel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Prioridade criticidade = Prioridade.MEDIA;

    @Column(length = 255)
    private String localizacao;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
}
