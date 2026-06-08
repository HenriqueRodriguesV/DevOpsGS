package br.com.fiap.orbit.repository;

import br.com.fiap.orbit.model.MissaoRecurso;
import br.com.fiap.orbit.model.MissaoRecursoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissaoRecursoRepository extends JpaRepository<MissaoRecurso, MissaoRecursoId> {

    List<MissaoRecurso> findByMissaoId(Long missaoId);

    List<MissaoRecurso> findByRecursoId(Long recursoId);
}
