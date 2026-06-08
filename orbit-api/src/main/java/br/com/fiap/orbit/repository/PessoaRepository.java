package br.com.fiap.orbit.repository;

import br.com.fiap.orbit.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    List<Pessoa> findByMissaoId(Long missaoId);

    Optional<Pessoa> findByMatricula(String matricula);

    boolean existsByMatricula(String matricula);

    Page<Pessoa> findAll(Pageable pageable);
}
