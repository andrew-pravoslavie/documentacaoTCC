package br.edu.securedesk.governanca.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade de persistência (JPA / Data Mapper) mapeando a tabela tb_evidencia_clean.
 * Armazena os metadados das evidências comprobatórias anexadas às tarefas de remediação (MIL 2).
 */
@Entity
@Table(name = "tb_evidencia_clean")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvidenciaDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tarefa_id", nullable = false)
    private Long tarefaId;

    @Column(name = "nome_arquivo", nullable = false, length = 180)
    private String nomeArquivo;

    @Column(name = "tipo_arquivo", length = 120)
    private String tipoArquivo;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "caminho_arquivo", length = 300)
    private String caminhoArquivo;

    @Column(length = 500)
    private String descricao;

    @Column(name = "autor_upload", length = 100)
    private String autorUpload;

    @Column(name = "data_upload", nullable = false, updatable = false)
    private LocalDateTime dataUpload;

    @PrePersist
    public void prePersist() {
        if (this.dataUpload == null) {
            this.dataUpload = LocalDateTime.now();
        }
    }
}
