package br.com.fiap.orbit.service;

import br.com.fiap.orbit.dto.response.RecomendacaoResponse;
import br.com.fiap.orbit.dto.response.RecomendacaoResponse.PontoRankeado;
import br.com.fiap.orbit.exception.BusinessException;
import br.com.fiap.orbit.exception.ResourceNotFoundException;
import br.com.fiap.orbit.model.Missao;
import br.com.fiap.orbit.model.PontoDeApoio;
import br.com.fiap.orbit.model.Veiculo;
import br.com.fiap.orbit.repository.MissaoRepository;
import br.com.fiap.orbit.repository.PontoDeApoioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecomendacaoService {

    private static final double PESO_DISTANCIA = 0.35;
    private static final double PESO_COMBUSTIVEL = 0.25;
    private static final double PESO_RECURSOS = 0.20;
    private static final double PESO_RISCO_ROTA = 0.10;
    private static final double PESO_CONDICAO_OPERADOR = 0.10;

    private final MissaoRepository missaoRepository;
    private final PontoDeApoioRepository pontoDeApoioRepository;

    public RecomendacaoResponse recomendar(Long missaoId) {
        Missao missao = missaoRepository.findById(missaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Missão não encontrada: " + missaoId));

        Veiculo veiculo = missao.getVeiculo();
        if (veiculo == null) {
            throw new BusinessException("Missão não possui veículo associado");
        }

        List<PontoDeApoio> pontos = pontoDeApoioRepository.findByAtivoTrue();
        if (pontos.isEmpty()) {
            throw new BusinessException("Nenhum ponto de apoio ativo disponível");
        }

        double latOrigem = missao.getOrigem().getLatitude();
        double lonOrigem = missao.getOrigem().getLongitude();

        double mediaCondicaoFisica = missao.getPessoas().isEmpty() ? 5.0
                : missao.getPessoas().stream()
                        .mapToInt(p -> p.getNivelCondicaoFisica())
                        .average().orElse(5.0);

        List<PontoComScore> scoredList = new ArrayList<>();
        for (PontoDeApoio ponto : pontos) {
            double distKm = haversine(latOrigem, lonOrigem,
                    ponto.getCoordenadas().getLatitude(), ponto.getCoordenadas().getLongitude());

            double scoreDistancia = calcularScoreDistancia(distKm, veiculo.getAutonomiaKm());
            double scoreCombustivel = veiculo.getNivelCombustivel() * 10.0;
            double scoreRecursos = calcularScoreRecursos(ponto);
            double scoreRisco = 10.0 - missao.getNivelRisco();
            double scoreOperador = mediaCondicaoFisica;

            double scoreTotal = (scoreDistancia * PESO_DISTANCIA)
                    + (scoreCombustivel * PESO_COMBUSTIVEL)
                    + (scoreRecursos * PESO_RECURSOS)
                    + (scoreRisco * PESO_RISCO_ROTA)
                    + (scoreOperador * PESO_CONDICAO_OPERADOR);

            String justificativa = buildJustificativa(ponto, distKm, scoreDistancia, scoreCombustivel,
                    scoreRecursos, scoreRisco, scoreOperador, scoreTotal);

            scoredList.add(new PontoComScore(ponto, distKm, scoreTotal, justificativa));
        }

        scoredList.sort(Comparator.comparingDouble(PontoComScore::score).reversed());

        List<PontoRankeado> ranking = new ArrayList<>();
        for (int i = 0; i < scoredList.size(); i++) {
            PontoComScore ps = scoredList.get(i);
            ranking.add(new PontoRankeado(
                    i + 1,
                    ps.ponto().getId(),
                    ps.ponto().getNome(),
                    ps.ponto().getTipo().name(),
                    Math.round(ps.distanciaKm() * 100.0) / 100.0,
                    Math.round(ps.score() * 100.0) / 100.0,
                    ps.justificativa()
            ));
        }

        return new RecomendacaoResponse(missaoId, missao.getTitulo(), ranking);
    }

    private double calcularScoreDistancia(double distKm, double autonomiaKm) {
        if (autonomiaKm <= 0) return 0.0;
        double ratio = distKm / autonomiaKm;
        if (ratio >= 1.0) return 0.0;
        return (1.0 - ratio) * 10.0;
    }

    private double calcularScoreRecursos(PontoDeApoio ponto) {
        int totalRecursos = ponto.getRecursos().size();
        if (totalRecursos == 0) return 0.0;
        double totalQtd = ponto.getRecursos().stream()
                .mapToDouble(r -> r.getQuantidade() != null ? r.getQuantidade() : 0.0)
                .sum();
        return Math.min(totalQtd / 100.0, 1.0) * 10.0;
    }

    private String buildJustificativa(PontoDeApoio ponto, double distKm, double sDist, double sComb,
                                      double sRec, double sRisco, double sOp, double total) {
        return String.format(
                "%s | Distância: %.1f km (score %.1f) | Combustível: %.1f | Recursos: %.1f | Risco: %.1f | Operador: %.1f | TOTAL: %.2f",
                ponto.getTipo().name(), distKm, sDist, sComb, sRec, sRisco, sOp, total
        );
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private record PontoComScore(PontoDeApoio ponto, double distanciaKm, double score, String justificativa) {}
}
