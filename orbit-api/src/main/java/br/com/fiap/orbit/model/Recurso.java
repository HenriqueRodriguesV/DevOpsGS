package br.com.fiap.orbit.model;

import br.com.fiap.orbit.model.enums.TipoRecurso;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ORBIT_RECURSO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_recurso")
    @SequenceGenerator(name = "seq_recurso", sequenceName = "SEQ_ORBIT_RECURSO", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRecurso tipo;

    @Column(nullable = false)
    private Double quantidade;

    @Column(nullable = false)
    private String unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_apoio_id", nullable = false)
    private PontoDeApoio pontoDeApoio;
}
