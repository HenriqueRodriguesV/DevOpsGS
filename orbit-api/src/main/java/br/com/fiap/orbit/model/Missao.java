package br.com.fiap.orbit.model;

import br.com.fiap.orbit.model.enums.StatusMissao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_ORBIT_MISSAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_missao")
    @SequenceGenerator(name = "seq_missao", sequenceName = "SEQ_ORBIT_MISSAO", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "origem_latitude", nullable = false)),
        @AttributeOverride(name = "longitude", column = @Column(name = "origem_longitude", nullable = false))
    })
    private Coordenadas origem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMissao status;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    // Nível de risco: 0.0 a 10.0
    @Column(nullable = false)
    private Double nivelRisco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @OneToMany(mappedBy = "missao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Pessoa> pessoas = new ArrayList<>();
}
