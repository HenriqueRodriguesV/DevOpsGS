package br.com.fiap.orbit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissaoRecursoId implements Serializable {

    @Column(name = "missao_id")
    private Long missaoId;

    @Column(name = "recurso_id")
    private Long recursoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MissaoRecursoId that)) return false;
        return Objects.equals(missaoId, that.missaoId) && Objects.equals(recursoId, that.recursoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(missaoId, recursoId);
    }
}
