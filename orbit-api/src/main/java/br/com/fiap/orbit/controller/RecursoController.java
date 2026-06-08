package br.com.fiap.orbit.controller;

import br.com.fiap.orbit.dto.request.RecursoRequest;
import br.com.fiap.orbit.dto.response.RecursoResponse;
import br.com.fiap.orbit.service.RecursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/recursos")
@RequiredArgsConstructor
@Tag(name = "Recursos", description = "Gerenciamento de recursos disponíveis nos pontos de apoio")
@SecurityRequirement(name = "Bearer Authentication")
public class RecursoController {

    private final RecursoService recursoService;

    @GetMapping("/ponto/{pontoId}")
    @Operation(summary = "Listar recursos de um ponto de apoio")
    public ResponseEntity<List<RecursoResponse>> listarPorPonto(@PathVariable Long pontoId) {
        return ResponseEntity.ok(recursoService.listarPorPonto(pontoId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar recurso por ID")
    public ResponseEntity<EntityModel<RecursoResponse>> buscarPorId(@PathVariable Long id) {
        RecursoResponse response = recursoService.buscarPorId(id);
        EntityModel<RecursoResponse> model = EntityModel.of(response,
                linkTo(methodOn(RecursoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(RecursoController.class).listarPorPonto(response.pontoDeApoioId())).withRel("recursos-do-ponto"));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Operation(summary = "Cadastrar recurso")
    public ResponseEntity<EntityModel<RecursoResponse>> criar(@Valid @RequestBody RecursoRequest request) {
        RecursoResponse response = recursoService.criar(request);
        EntityModel<RecursoResponse> model = EntityModel.of(response,
                linkTo(methodOn(RecursoController.class).buscarPorId(response.id())).withSelfRel());
        return ResponseEntity.status(201).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar recurso")
    public ResponseEntity<EntityModel<RecursoResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody RecursoRequest request) {
        RecursoResponse response = recursoService.atualizar(id, request);
        EntityModel<RecursoResponse> model = EntityModel.of(response,
                linkTo(methodOn(RecursoController.class).buscarPorId(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar recurso")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
