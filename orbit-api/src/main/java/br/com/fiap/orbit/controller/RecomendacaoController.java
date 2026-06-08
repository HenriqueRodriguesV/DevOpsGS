package br.com.fiap.orbit.controller;

import br.com.fiap.orbit.dto.response.RecomendacaoResponse;
import br.com.fiap.orbit.service.RecomendacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/recomendacoes")
@RequiredArgsConstructor
@Tag(name = "Recomendações", description = "Motor de recomendação de pontos de apoio para missões em risco")
@SecurityRequirement(name = "Bearer Authentication")
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;

    @GetMapping("/missao/{missaoId}")
    @Operation(
            summary = "Recomendar pontos de apoio para uma missão",
            description = "Analisa a missão e retorna uma lista rankeada de pontos de apoio usando score ponderado com Haversine para distância"
    )
    public ResponseEntity<EntityModel<RecomendacaoResponse>> recomendar(@PathVariable Long missaoId) {
        RecomendacaoResponse response = recomendacaoService.recomendar(missaoId);
        EntityModel<RecomendacaoResponse> model = EntityModel.of(response,
                linkTo(methodOn(RecomendacaoController.class).recomendar(missaoId)).withSelfRel(),
                linkTo(methodOn(MissaoController.class).buscarPorId(missaoId)).withRel("missao"));
        return ResponseEntity.ok(model);
    }
}
