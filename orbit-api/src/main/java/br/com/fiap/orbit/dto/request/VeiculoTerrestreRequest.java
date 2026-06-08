package br.com.fiap.orbit.dto.request;

import jakarta.validation.constraints.*;

public record VeiculoTerrestreRequest(
        @NotBlank String nome,
        @NotBlank String modelo,
        @NotNull @Positive Double autonomiaKm,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") Double nivelCombustivel,
        @NotNull @Positive Double capacidadeCarga,
        @NotBlank String tipoTracao,
        @NotNull @Positive Double alturaMaxima
) {}
