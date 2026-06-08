package br.com.fiap.orbit.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TB_ORBIT_VEICULO_TERRESTRE")
@DiscriminatorValue("TERRESTRE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VeiculoTerrestre extends Veiculo {

    private String tipoTracao;

    private Double alturaMaxima;
}
