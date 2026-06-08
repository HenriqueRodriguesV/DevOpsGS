package br.com.fiap.orbit.service;

import br.com.fiap.orbit.dto.request.MissaoRequest;
import br.com.fiap.orbit.dto.response.MissaoResponse;
import br.com.fiap.orbit.dto.response.PessoaResponse;
import br.com.fiap.orbit.dto.response.VeiculoResponse;
import br.com.fiap.orbit.exception.ResourceNotFoundException;
import br.com.fiap.orbit.model.Coordenadas;
import br.com.fiap.orbit.model.Missao;
import br.com.fiap.orbit.model.Veiculo;
import br.com.fiap.orbit.model.enums.StatusMissao;
import br.com.fiap.orbit.repository.MissaoRepository;
import br.com.fiap.orbit.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissaoService {

    private final MissaoRepository missaoRepository;
    private final VeiculoRepository veiculoRepository;

    public Page<MissaoResponse> listarTodos(Pageable pageable) {
        return missaoRepository.findAll(pageable).map(this::toResponse);
    }

    public MissaoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public List<MissaoResponse> listarPorStatus(StatusMissao status) {
        return missaoRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public List<MissaoResponse> listarCriticas(Double nivelMinimo) {
        return missaoRepository.findByNivelRiscoGreaterThanEqual(nivelMinimo).stream().map(this::toResponse).toList();
    }

    @Transactional
    public MissaoResponse criar(MissaoRequest request) {
        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: " + request.veiculoId()));
        Missao missao = Missao.builder()
                .titulo(request.titulo())
                .descricao(request.descricao())
                .origem(new Coordenadas(request.origemLatitude(), request.origemLongitude()))
                .status(request.status())
                .dataInicio(LocalDateTime.now())
                .nivelRisco(request.nivelRisco())
                .veiculo(veiculo)
                .build();
        return toResponse(missaoRepository.save(missao));
    }

    @Transactional
    public MissaoResponse atualizar(Long id, MissaoRequest request) {
        Missao missao = buscarEntidade(id);
        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: " + request.veiculoId()));
        missao.setTitulo(request.titulo());
        missao.setDescricao(request.descricao());
        missao.setOrigem(new Coordenadas(request.origemLatitude(), request.origemLongitude()));
        missao.setStatus(request.status());
        missao.setNivelRisco(request.nivelRisco());
        missao.setVeiculo(veiculo);
        if (request.status() == StatusMissao.CONCLUIDA || request.status() == StatusMissao.ABORTADA) {
            missao.setDataFim(LocalDateTime.now());
        }
        return toResponse(missaoRepository.save(missao));
    }

    @Transactional
    public void deletar(Long id) {
        missaoRepository.delete(buscarEntidade(id));
    }

    public Missao buscarEntidade(Long id) {
        return missaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Missão não encontrada: " + id));
    }

    private MissaoResponse toResponse(Missao m) {
        VeiculoResponse veiculoResp = m.getVeiculo() != null
                ? new VeiculoResponse(m.getVeiculo().getId(), m.getVeiculo().getNome(), m.getVeiculo().getModelo(),
                        m.getVeiculo().getTipo(), m.getVeiculo().getAutonomiaKm(), m.getVeiculo().getNivelCombustivel(),
                        m.getVeiculo().getCapacidadeCarga(), m.getVeiculo().getAtivo())
                : null;
        List<PessoaResponse> pessoas = m.getPessoas().stream()
                .map(p -> new PessoaResponse(p.getId(), p.getNome(), p.getMatricula(), p.getFuncao(),
                        p.getNivelCondicaoFisica(), p.getNivelHabilidade(), m.getId()))
                .toList();
        return new MissaoResponse(m.getId(), m.getTitulo(), m.getDescricao(),
                m.getOrigem().getLatitude(), m.getOrigem().getLongitude(),
                m.getStatus(), m.getDataInicio(), m.getDataFim(), m.getNivelRisco(),
                veiculoResp, pessoas);
    }
}
