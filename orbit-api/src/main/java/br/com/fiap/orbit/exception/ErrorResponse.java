package br.com.fiap.orbit.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String erro,
        String mensagem,
        LocalDateTime timestamp,
        List<String> detalhes
) {}
