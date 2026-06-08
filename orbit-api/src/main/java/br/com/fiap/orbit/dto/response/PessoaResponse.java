package br.com.fiap.orbit.dto.response;

public record PessoaResponse(
        Long id,
        String nome,
        String matricula,
        String funcao,
        Integer nivelCondicaoFisica,
        Integer nivelHabilidade,
        Long missaoId
) {}
