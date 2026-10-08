package br.edu.securedesk.entity.diagnostico;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa formalmente uma das 6 funções estruturantes do NIST CSF 2.0.
 */
@Entity
@Table(name = "tb_funcao_nist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncaoNist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 10)
    private SiglaFuncaoNist sigla;

    @Column(nullable = false, length = 60)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 50)
    private String icone;

    private Integer ordem;
}
