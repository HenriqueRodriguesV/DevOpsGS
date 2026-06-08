package br.com.fiap.orbit.dto.response;

import java.util.List;

public record RecomendacaoResponse(
        Long missaoId,
        String tituloMissao,
        List<PontoRankeado> pontosRecomendados
) {
    public record PontoRankeado(
            int posicao,
            Long pontoDeApoioId,
            String nomePonto,
            String tipoPonto,
            Double distanciaKm,
            Double scoreTotal,
            String justificativa
    ) {}
}
