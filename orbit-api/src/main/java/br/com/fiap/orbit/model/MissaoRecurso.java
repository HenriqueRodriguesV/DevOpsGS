package br.com.fiap.orbit.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ORBIT_MISSAO_RECURSO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissaoRecurso {

    @EmbeddedId
    private MissaoRecursoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("missaoId")
    @JoinColumn(name = "missao_id")
    private Missao missao;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recursoId")
    @JoinColumn(name = "recurso_id")
    private Recurso recurso;

    private Double quantidadeConsumida;

    private LocalDateTime dataConsumo;
}
