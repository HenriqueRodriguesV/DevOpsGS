package br.com.fiap.orbit.dto.request;

import br.com.fiap.orbit.model.enums.TipoPontoDeApoio;
import jakarta.validation.constraints.*;

public record PontoDeApoioRequest(
        @NotBlank String nome,
        String descricao,
        @NotNull TipoPontoDeApoio tipo,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @NotNull @Positive Integer capacidadeAtendimento
) {}
