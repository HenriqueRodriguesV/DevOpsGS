package br.com.fiap.orbit.repository;

import br.com.fiap.orbit.model.Missao;
import br.com.fiap.orbit.model.enums.StatusMissao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long> {

    List<Missao> findByStatus(StatusMissao status);

    List<Missao> findByVeiculoId(Long veiculoId);

    Page<Missao> findAll(Pageable pageable);

    @Query("SELECT m FROM Missao m WHERE m.nivelRisco >= :nivelMinimo")
    List<Missao> findByNivelRiscoGreaterThanEqual(Double nivelMinimo);
}
