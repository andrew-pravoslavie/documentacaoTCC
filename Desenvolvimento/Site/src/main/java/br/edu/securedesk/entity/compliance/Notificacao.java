package br.edu.securedesk.entity.compliance;

import br.edu.securedesk.entity.core.Empresa;
import br.edu.securedesk.entity.core.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Notificações geradas pelo motor de compliance e monitoramento
 * (e.g. políticas próximas ao vencimento, tarefas atrasadas, gaps críticos).
 */
@Entity
@Table(name = "tb_notificacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoNotificacao tipo;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Column(length = 255)
    private String linkDestino;

    @Builder.Default
    @Column(nullable = false)
    private Boolean lida = false;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataEnvio = LocalDateTime.now();
}
