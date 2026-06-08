package br.com.fiap.orbit.repository;

import br.com.fiap.orbit.model.Veiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    List<Veiculo> findByAtivo(Boolean ativo);

    Page<Veiculo> findAll(Pageable pageable);
}
