package br.edu.securedesk.entity.governanca;

import br.edu.securedesk.entity.core.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Arquivo ou comprovação anexada a uma tarefa para validar que o gap foi resolvido
 * e que a prática de segurança está documentada/institucionalizada (requisito MIL 2).
 */
@Entity
@Table(name = "tb_evidencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarefa_id", nullable = false)
    private TarefaRemediacao tarefa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_upload_id", nullable = false)
    private Usuario usuarioUpload;

    @Column(nullable = false, length = 180)
    private String nomeArquivo;

    @Column(length = 60)
    private String tipoArquivo;

    private Long tamanhoBytes;

    @Column(nullable = false, length = 255)
    private String caminhoArquivo;

    @Column(length = 255)
    private String descricao;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataUpload = LocalDateTime.now();
}
