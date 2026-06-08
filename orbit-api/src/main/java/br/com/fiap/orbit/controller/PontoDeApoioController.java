package br.com.fiap.orbit.controller;

import br.com.fiap.orbit.dto.request.PontoDeApoioRequest;
import br.com.fiap.orbit.dto.response.PontoDeApoioResponse;
import br.com.fiap.orbit.service.PontoDeApoioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/pontos-apoio")
@RequiredArgsConstructor
@Tag(name = "Pontos de Apoio", description = "Gerenciamento de pontos de apoio e suporte")
@SecurityRequirement(name = "Bearer Authentication")
public class PontoDeApoioController {

    private final PontoDeApoioService pontoDeApoioService;

    @GetMapping
    @Operation(summary = "Listar todos os pontos de apoio (paginado)")
    public ResponseEntity<Page<EntityModel<PontoDeApoioResponse>>> listar(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<EntityModel<PontoDeApoioResponse>> page = pontoDeApoioService.listarTodos(pageable)
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PontoDeApoioController.class).buscarPorId(p.id())).withSelfRel()));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ponto de apoio por ID")
    public ResponseEntity<EntityModel<PontoDeApoioResponse>> buscarPorId(@PathVariable Long id) {
        PontoDeApoioResponse response = pontoDeApoioService.buscarPorId(id);
        EntityModel<PontoDeApoioResponse> model = EntityModel.of(response,
                linkTo(methodOn(PontoDeApoioController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PontoDeApoioController.class).listar(Pageable.unpaged())).withRel("pontos"));
        return ResponseEntity.ok(model);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar pontos de apoio ativos")
    public ResponseEntity<List<PontoDeApoioResponse>> listarAtivos() {
        return ResponseEntity.ok(pontoDeApoioService.listarAtivos());
    }

    @PostMapping
    @Operation(summary = "Criar ponto de apoio")
    public ResponseEntity<EntityModel<PontoDeApoioResponse>> criar(
            @Valid @RequestBody PontoDeApoioRequest request) {
        PontoDeApoioResponse response = pontoDeApoioService.criar(request);
        EntityModel<PontoDeApoioResponse> model = EntityModel.of(response,
                linkTo(methodOn(PontoDeApoioController.class).buscarPorId(response.id())).withSelfRel());
        return ResponseEntity.status(201).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ponto de apoio")
    public ResponseEntity<EntityModel<PontoDeApoioResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody PontoDeApoioRequest request) {
        PontoDeApoioResponse response = pontoDeApoioService.atualizar(id, request);
        EntityModel<PontoDeApoioResponse> model = EntityModel.of(response,
                linkTo(methodOn(PontoDeApoioController.class).buscarPorId(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar ponto de apoio")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        pontoDeApoioService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
