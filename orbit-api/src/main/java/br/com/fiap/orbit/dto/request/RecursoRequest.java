package br.com.fiap.orbit.dto.request;

import br.com.fiap.orbit.model.enums.TipoRecurso;
import jakarta.validation.constraints.*;

public record RecursoRequest(
        @NotBlank String nome,
        String descricao,
        @NotNull TipoRecurso tipo,
        @NotNull @Positive Double quantidade,
        @NotBlank String unidade,
        @NotNull Long pontoDeApoioId
) {}
