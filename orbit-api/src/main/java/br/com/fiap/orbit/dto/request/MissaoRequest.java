package br.com.fiap.orbit.dto.request;

import br.com.fiap.orbit.model.enums.StatusMissao;
import jakarta.validation.constraints.*;

public record MissaoRequest(
        @NotBlank String titulo,
        String descricao,
        @NotNull Double origemLatitude,
        @NotNull Double origemLongitude,
        @NotNull StatusMissao status,
        @NotNull @DecimalMin("0.0") @DecimalMax("10.0") Double nivelRisco,
        @NotNull Long veiculoId
) {}
