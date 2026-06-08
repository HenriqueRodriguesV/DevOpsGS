package br.com.fiap.orbit.service;

import br.com.fiap.orbit.dto.request.PontoDeApoioRequest;
import br.com.fiap.orbit.dto.response.PontoDeApoioResponse;
import br.com.fiap.orbit.dto.response.RecursoResponse;
import br.com.fiap.orbit.exception.ResourceNotFoundException;
import br.com.fiap.orbit.model.Coordenadas;
import br.com.fiap.orbit.model.PontoDeApoio;
import br.com.fiap.orbit.repository.PontoDeApoioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PontoDeApoioService {

    private final PontoDeApoioRepository pontoDeApoioRepository;

    public Page<PontoDeApoioResponse> listarTodos(Pageable pageable) {
        return pontoDeApoioRepository.findAll(pageable).map(this::toResponse);
    }

    public PontoDeApoioResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public List<PontoDeApoioResponse> listarAtivos() {
        return pontoDeApoioRepository.findByAtivoTrue().stream().map(this::toResponse).toList();
    }

    @Transactional
    public PontoDeApoioResponse criar(PontoDeApoioRequest request) {
        PontoDeApoio ponto = PontoDeApoio.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .tipo(request.tipo())
                .coordenadas(new Coordenadas(request.latitude(), request.longitude()))
                .capacidadeAtendimento(request.capacidadeAtendimento())
                .ativo(true)
                .build();
        return toResponse(pontoDeApoioRepository.save(ponto));
    }

    @Transactional
    public PontoDeApoioResponse atualizar(Long id, PontoDeApoioRequest request) {
        PontoDeApoio ponto = buscarEntidade(id);
        ponto.setNome(request.nome());
        ponto.setDescricao(request.descricao());
        ponto.setTipo(request.tipo());
        ponto.setCoordenadas(new Coordenadas(request.latitude(), request.longitude()));
        ponto.setCapacidadeAtendimento(request.capacidadeAtendimento());
        return toResponse(pontoDeApoioRepository.save(ponto));
    }

    @Transactional
    public void desativar(Long id) {
        PontoDeApoio ponto = buscarEntidade(id);
        ponto.setAtivo(false);
        pontoDeApoioRepository.save(ponto);
    }

    public PontoDeApoio buscarEntidade(Long id) {
        return pontoDeApoioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de apoio não encontrado: " + id));
    }

    public PontoDeApoioResponse toResponse(PontoDeApoio p) {
        List<RecursoResponse> recursos = p.getRecursos().stream()
                .map(r -> new RecursoResponse(r.getId(), r.getNome(), r.getDescricao(), r.getTipo(),
                        r.getQuantidade(), r.getUnidade(), p.getId()))
                .toList();
        return new PontoDeApoioResponse(p.getId(), p.getNome(), p.getDescricao(), p.getTipo(),
                p.getCoordenadas().getLatitude(), p.getCoordenadas().getLongitude(),
                p.getCapacidadeAtendimento(), p.getAtivo(), recursos);
    }
}
