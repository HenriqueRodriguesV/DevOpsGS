package br.com.fiap.orbit.model;

import br.com.fiap.orbit.model.enums.TipoVeiculo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TB_ORBIT_VEICULO")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING, length = 20)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_veiculo")
    @SequenceGenerator(name = "seq_veiculo", sequenceName = "SEQ_ORBIT_VEICULO", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String modelo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVeiculo tipo;

    @Column(nullable = false)
    private Double autonomiaKm;

    // Porcentagem do combustível/bateria: 0.0 a 1.0
    @Column(nullable = false)
    private Double nivelCombustivel;

    private Double capacidadeCarga;

    @Column(nullable = false)
    private Boolean ativo;
}
