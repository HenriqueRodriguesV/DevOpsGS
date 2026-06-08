package br.com.fiap.orbit.controller;

import br.com.fiap.orbit.dto.request.MissaoRequest;
import br.com.fiap.orbit.dto.response.MissaoResponse;
import br.com.fiap.orbit.model.enums.StatusMissao;
import br.com.fiap.orbit.service.MissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/missoes")
@RequiredArgsConstructor
@Tag(name = "Missões", description = "Gerenciamento de missões operacionais")
@SecurityRequirement(name = "Bearer Authentication")
public class MissaoController {

    private final MissaoService missaoService;

    @GetMapping
    @Operation(summary = "Listar todas as missões (paginado)")
    public ResponseEntity<Page<EntityModel<MissaoResponse>>> listar(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<EntityModel<MissaoResponse>> page = missaoService.listarTodos(pageable)
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(MissaoController.class).buscarPorId(m.id())).withSelfRel(),
                        linkTo(methodOn(MissaoController.class).listar(pageable)).withRel("missoes")));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar missão por ID")
    public ResponseEntity<EntityModel<MissaoResponse>> buscarPorId(@PathVariable Long id) {
        MissaoResponse response = missaoService.buscarPorId(id);
        EntityModel<MissaoResponse> model = EntityModel.of(response,
                linkTo(methodOn(MissaoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(MissaoController.class).listar(Pageable.unpaged())).withRel("missoes"),
                linkTo(methodOn(RecomendacaoController.class).recomendar(id)).withRel("recomendacao"));
        return ResponseEntity.ok(model);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar missões por status")
    public ResponseEntity<List<MissaoResponse>> listarPorStatus(@PathVariable StatusMissao status) {
        return ResponseEntity.ok(missaoService.listarPorStatus(status));
    }

    @GetMapping("/criticas")
    @Operation(summary = "Listar missões com nível de risco elevado")
    public ResponseEntity<List<MissaoResponse>> listarCriticas(
            @RequestParam(defaultValue = "7.0") Double nivelMinimo) {
        return ResponseEntity.ok(missaoService.listarCriticas(nivelMinimo));
    }

    @PostMapping
    @Operation(summary = "Criar nova missão")
    public ResponseEntity<EntityModel<MissaoResponse>> criar(@Valid @RequestBody MissaoRequest request) {
        MissaoResponse response = missaoService.criar(request);
        EntityModel<MissaoResponse> model = EntityModel.of(response,
                linkTo(methodOn(MissaoController.class).buscarPorId(response.id())).withSelfRel());
        return ResponseEntity.status(201).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar missão")
    public ResponseEntity<EntityModel<MissaoResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody MissaoRequest request) {
        MissaoResponse response = missaoService.atualizar(id, request);
        EntityModel<MissaoResponse> model = EntityModel.of(response,
                linkTo(methodOn(MissaoController.class).buscarPorId(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar missão")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        missaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
