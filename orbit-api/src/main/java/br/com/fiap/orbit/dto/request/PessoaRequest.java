package br.com.fiap.orbit.dto.request;

import jakarta.validation.constraints.*;

public record PessoaRequest(
        @NotBlank String nome,
        @NotBlank String matricula,
        @NotBlank String funcao,
        @NotNull @Min(0) @Max(10) Integer nivelCondicaoFisica,
        @NotNull @Min(0) @Max(10) Integer nivelHabilidade,
        Long missaoId
) {}
