package br.edu.securedesk.entity.core;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Registro de consentimento de tratamento de dados pessoais e termos de uso (LGPD).
 * Atende ao entregável OEM 6a da monografia.
 */
@Entity
@Table(name = "tb_termo_consentimento_lgpd")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermoConsentimentoLgpd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 20)
    private String versaoTermo;

    @Builder.Default
    @Column(nullable = false)
    private Boolean aceito = true;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAceite = LocalDateTime.now();

    @Column(length = 45)
    private String ipOrigem;
}
