package br.com.fiap.orbit.model;

import br.com.fiap.orbit.model.enums.TipoPontoDeApoio;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_ORBIT_PONTO_APOIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PontoDeApoio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ponto")
    @SequenceGenerator(name = "seq_ponto", sequenceName = "SEQ_ORBIT_PONTO", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 2000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPontoDeApoio tipo;

    @Embedded
    private Coordenadas coordenadas;

    private Integer capacidadeAtendimento;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "pontoDeApoio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Recurso> recursos = new ArrayList<>();
}
