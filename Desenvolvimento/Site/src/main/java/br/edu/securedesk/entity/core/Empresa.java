package br.edu.securedesk.entity.core;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa a PME (Pequena/Média Empresa) atendida pela plataforma.
 * Delimitada pelo critério do TCC de gerenciar parques tecnológicos de até 100 dispositivos.
 */
@Entity
@Table(name = "tb_empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String razaoSocial;

    @Column(length = 150)
    private String nomeFantasia;

    @Column(unique = true, length = 20)
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private SetorEmpresa setor;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private PorteEmpresa porte;

    private Integer numFuncionarios;

    /**
     * Quantidade estimada ou gerenciada de dispositivos no parque tecnológico (foco <= 100).
     */
    private Integer qtdDispositivos;

    @Column(length = 255)
    private String endereco;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Usuario> usuarios = new ArrayList<>();
}
