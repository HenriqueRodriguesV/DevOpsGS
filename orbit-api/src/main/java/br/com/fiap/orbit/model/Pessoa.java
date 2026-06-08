package br.com.fiap.orbit.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ORBIT_PESSOA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pessoa")
    @SequenceGenerator(name = "seq_pessoa", sequenceName = "SEQ_ORBIT_PESSOA", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true)
    private String matricula;

    private String funcao;

    // Nível de condição física: 0 a 10
    @Column(nullable = false)
    private Integer nivelCondicaoFisica;

    // Nível de habilidade técnica: 0 a 10
    @Column(nullable = false)
    private Integer nivelHabilidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missao_id")
    private Missao missao;
}
