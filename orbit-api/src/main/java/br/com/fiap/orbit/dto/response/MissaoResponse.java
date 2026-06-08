package br.com.fiap.orbit.dto.response;

import br.com.fiap.orbit.model.enums.StatusMissao;

import java.time.LocalDateTime;
import java.util.List;

public record MissaoResponse(
        Long id,
        String titulo,
        String descricao,
        Double origemLatitude,
        Double origemLongitude,
        StatusMissao status,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Double nivelRisco,
        VeiculoResponse veiculo,
        List<PessoaResponse> pessoas
) {}
