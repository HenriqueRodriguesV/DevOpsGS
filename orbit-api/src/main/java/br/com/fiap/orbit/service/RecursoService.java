package br.com.fiap.orbit.service;

import br.com.fiap.orbit.dto.request.RecursoRequest;
import br.com.fiap.orbit.dto.response.RecursoResponse;
import br.com.fiap.orbit.exception.ResourceNotFoundException;
import br.com.fiap.orbit.model.PontoDeApoio;
import br.com.fiap.orbit.model.Recurso;
import br.com.fiap.orbit.repository.PontoDeApoioRepository;
import br.com.fiap.orbit.repository.RecursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final PontoDeApoioRepository pontoDeApoioRepository;

    public List<RecursoResponse> listarPorPonto(Long pontoId) {
        return recursoRepository.findByPontoDeApoioId(pontoId).stream().map(this::toResponse).toList();
    }

    public RecursoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public RecursoResponse criar(RecursoRequest request) {
        PontoDeApoio ponto = pontoDeApoioRepository.findById(request.pontoDeApoioId())
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de apoio não encontrado: " + request.pontoDeApoioId()));
        Recurso recurso = Recurso.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .tipo(request.tipo())
                .quantidade(request.quantidade())
                .unidade(request.unidade())
                .pontoDeApoio(ponto)
                .build();
        return toResponse(recursoRepository.save(recurso));
    }

    @Transactional
    public RecursoResponse atualizar(Long id, RecursoRequest request) {
        Recurso recurso = buscarEntidade(id);
        recurso.setNome(request.nome());
        recurso.setDescricao(request.descricao());
        recurso.setTipo(request.tipo());
        recurso.setQuantidade(request.quantidade());
        recurso.setUnidade(request.unidade());
        return toResponse(recursoRepository.save(recurso));
    }

    @Transactional
    public void deletar(Long id) {
        recursoRepository.delete(buscarEntidade(id));
    }

    public Recurso buscarEntidade(Long id) {
        return recursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado: " + id));
    }

    private RecursoResponse toResponse(Recurso r) {
        return new RecursoResponse(r.getId(), r.getNome(), r.getDescricao(), r.getTipo(),
                r.getQuantidade(), r.getUnidade(), r.getPontoDeApoio().getId());
    }
}
