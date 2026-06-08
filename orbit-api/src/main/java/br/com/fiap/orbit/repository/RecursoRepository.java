package br.com.fiap.orbit.repository;

import br.com.fiap.orbit.model.Recurso;
import br.com.fiap.orbit.model.enums.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    List<Recurso> findByPontoDeApoioId(Long pontoDeApoioId);

    List<Recurso> findByTipo(TipoRecurso tipo);
}
