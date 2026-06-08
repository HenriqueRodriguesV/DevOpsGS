package br.com.fiap.orbit.service;

import br.com.fiap.orbit.dto.request.PessoaRequest;
import br.com.fiap.orbit.dto.response.PessoaResponse;
import br.com.fiap.orbit.exception.BusinessException;
import br.com.fiap.orbit.exception.ResourceNotFoundException;
import br.com.fiap.orbit.model.Missao;
import br.com.fiap.orbit.model.Pessoa;
import br.com.fiap.orbit.repository.MissaoRepository;
import br.com.fiap.orbit.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final MissaoRepository missaoRepository;

    public Page<PessoaResponse> listarTodos(Pageable pageable) {
        return pessoaRepository.findAll(pageable).map(this::toResponse);
    }

    public PessoaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public List<PessoaResponse> listarPorMissao(Long missaoId) {
        return pessoaRepository.findByMissaoId(missaoId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public PessoaResponse criar(PessoaRequest request) {
        if (pessoaRepository.existsByMatricula(request.matricula())) {
            throw new BusinessException("Matrícula já cadastrada: " + request.matricula());
        }
        Missao missao = request.missaoId() != null
                ? missaoRepository.findById(request.missaoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Missão não encontrada: " + request.missaoId()))
                : null;
        Pessoa pessoa = Pessoa.builder()
                .nome(request.nome())
                .matricula(request.matricula())
                .funcao(request.funcao())
                .nivelCondicaoFisica(request.nivelCondicaoFisica())
                .nivelHabilidade(request.nivelHabilidade())
                .missao(missao)
                .build();
        return toResponse(pessoaRepository.save(pessoa));
    }

    @Transactional
    public PessoaResponse atualizar(Long id, PessoaRequest request) {
        Pessoa pessoa = buscarEntidade(id);
        Missao missao = request.missaoId() != null
                ? missaoRepository.findById(request.missaoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Missão não encontrada: " + request.missaoId()))
                : null;
        pessoa.setNome(request.nome());
        pessoa.setFuncao(request.funcao());
        pessoa.setNivelCondicaoFisica(request.nivelCondicaoFisica());
        pessoa.setNivelHabilidade(request.nivelHabilidade());
        pessoa.setMissao(missao);
        return toResponse(pessoaRepository.save(pessoa));
    }

    @Transactional
    public void deletar(Long id) {
        pessoaRepository.delete(buscarEntidade(id));
    }

    public Pessoa buscarEntidade(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada: " + id));
    }

    private PessoaResponse toResponse(Pessoa p) {
        return new PessoaResponse(p.getId(), p.getNome(), p.getMatricula(), p.getFuncao(),
                p.getNivelCondicaoFisica(), p.getNivelHabilidade(),
                p.getMissao() != null ? p.getMissao().getId() : null);
    }
}
