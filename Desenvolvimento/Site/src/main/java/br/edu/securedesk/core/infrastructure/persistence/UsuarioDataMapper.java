package br.edu.securedesk.core.infrastructure.persistence;

import br.edu.securedesk.core.domain.PerfilUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade de persistência JPA (Data Mapper) para Usuários no padrão Clean Architecture.
 * Mapeia a tabela tb_usuario_clean garantindo conformidade com isolamento multi-empresa e LGPD.
 */
@Entity
@Table(name = "tb_usuario_clean")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(length = 80)
    private String sobrenome;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false, length = 30)
    private PerfilUsuario perfil;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @Builder.Default
    @Column(name = "consentimento_lgpd_aceito", nullable = false)
    private Boolean consentimentoLgpdAceito = true;

    @Builder.Default
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDateTime.now();
        }
        if (this.ativo == null) {
            this.ativo = true;
        }
        if (this.consentimentoLgpdAceito == null) {
            this.consentimentoLgpdAceito = true;
        }
    }

    public boolean isAtivo() {
        return Boolean.TRUE.equals(this.ativo);
    }

    public boolean isConsentimentoLgpdAceito() {
        return Boolean.TRUE.equals(this.consentimentoLgpdAceito);
    }
}
