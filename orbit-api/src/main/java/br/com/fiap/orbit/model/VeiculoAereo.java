package br.com.fiap.orbit.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TB_ORBIT_VEICULO_AEREO")
@DiscriminatorValue("AEREO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VeiculoAereo extends Veiculo {

    private Double alcanceVertical;

    private Integer capacidadePassageiros;
}
