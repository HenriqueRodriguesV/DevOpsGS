package br.com.fiap.orbit.repository;

import br.com.fiap.orbit.model.PontoDeApoio;
import br.com.fiap.orbit.model.enums.TipoPontoDeApoio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoDeApoioRepository extends JpaRepository<PontoDeApoio, Long> {

    List<PontoDeApoio> findByAtivo(Boolean ativo);

    List<PontoDeApoio> findByTipo(TipoPontoDeApoio tipo);

    List<PontoDeApoio> findByAtivoTrue();

    Page<PontoDeApoio> findAll(Pageable pageable);
}
