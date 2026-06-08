package br.com.fiap.orbit.dto.response;

import br.com.fiap.orbit.model.enums.TipoPontoDeApoio;

import java.util.List;

public record PontoDeApoioResponse(
        Long id,
        String nome,
        String descricao,
        TipoPontoDeApoio tipo,
        Double latitude,
        Double longitude,
        Integer capacidadeAtendimento,
        Boolean ativo,
        List<RecursoResponse> recursos
) {}
