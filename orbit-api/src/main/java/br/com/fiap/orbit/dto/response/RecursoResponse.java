package br.com.fiap.orbit.dto.response;

import br.com.fiap.orbit.model.enums.TipoRecurso;

public record RecursoResponse(
        Long id,
        String nome,
        String descricao,
        TipoRecurso tipo,
        Double quantidade,
        String unidade,
        Long pontoDeApoioId
) {}
