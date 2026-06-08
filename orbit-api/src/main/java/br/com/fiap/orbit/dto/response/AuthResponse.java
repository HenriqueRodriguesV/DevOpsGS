package br.com.fiap.orbit.dto.response;

public record AuthResponse(
        String token,
        String tipo,
        String email,
        String nome
) {}
