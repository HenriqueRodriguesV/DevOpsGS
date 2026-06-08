package br.com.fiap.orbit.dto.response;

import br.com.fiap.orbit.model.enums.TipoVeiculo;

public record VeiculoResponse(
        Long id,
        String nome,
        String modelo,
        TipoVeiculo tipo,
        Double autonomiaKm,
        Double nivelCombustivel,
        Double capacidadeCarga,
        Boolean ativo
) {}
