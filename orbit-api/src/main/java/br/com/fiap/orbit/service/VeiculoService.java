package br.com.fiap.orbit.service;

import br.com.fiap.orbit.dto.request.VeiculoAereoRequest;
import br.com.fiap.orbit.dto.request.VeiculoTerrestreRequest;
import br.com.fiap.orbit.dto.response.VeiculoResponse;
import br.com.fiap.orbit.exception.ResourceNotFoundException;
import br.com.fiap.orbit.model.Veiculo;
import br.com.fiap.orbit.model.VeiculoAereo;
import br.com.fiap.orbit.model.VeiculoTerrestre;
import br.com.fiap.orbit.model.enums.TipoVeiculo;
import br.com.fiap.orbit.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public Page<VeiculoResponse> listarTodos(Pageable pageable) {
        return veiculoRepository.findAll(pageable).map(this::toResponse);
    }

    public VeiculoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public VeiculoResponse criarTerrestre(VeiculoTerrestreRequest request) {
        VeiculoTerrestre v = VeiculoTerrestre.builder()
                .nome(request.nome())
                .modelo(request.modelo())
                .tipo(TipoVeiculo.TERRESTRE)
                .autonomiaKm(request.autonomiaKm())
                .nivelCombustivel(request.nivelCombustivel())
                .capacidadeCarga(request.capacidadeCarga())
                .ativo(true)
                .tipoTracao(request.tipoTracao())
                .alturaMaxima(request.alturaMaxima())
                .build();
        return toResponse(veiculoRepository.save(v));
    }

    @Transactional
    public VeiculoResponse criarAereo(VeiculoAereoRequest request) {
        VeiculoAereo v = VeiculoAereo.builder()
                .nome(request.nome())
                .modelo(request.modelo())
                .tipo(TipoVeiculo.AEREO)
                .autonomiaKm(request.autonomiaKm())
                .nivelCombustivel(request.nivelCombustivel())
                .capacidadeCarga(request.capacidadeCarga())
                .ativo(true)
                .alcanceVertical(request.alcanceVertical())
                .capacidadePassageiros(request.capacidadePassageiros())
                .build();
        return toResponse(veiculoRepository.save(v));
    }

    @Transactional
    public void desativar(Long id) {
        Veiculo v = buscarEntidade(id);
        v.setAtivo(false);
        veiculoRepository.save(v);
    }

    public Veiculo buscarEntidade(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: " + id));
    }

    private VeiculoResponse toResponse(Veiculo v) {
        return new VeiculoResponse(v.getId(), v.getNome(), v.getModelo(), v.getTipo(),
                v.getAutonomiaKm(), v.getNivelCombustivel(), v.getCapacidadeCarga(), v.getAtivo());
    }
}
