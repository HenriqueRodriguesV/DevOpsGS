package br.com.fiap.orbit.controller;

import br.com.fiap.orbit.dto.request.PessoaRequest;
import br.com.fiap.orbit.dto.response.PessoaResponse;
import br.com.fiap.orbit.service.PessoaService;
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
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
@Tag(name = "Pessoas", description = "Gerenciamento de operadores e tripulantes")
@SecurityRequirement(name = "Bearer Authentication")
public class PessoaController {

    private final PessoaService pessoaService;

    @GetMapping
    @Operation(summary = "Listar todas as pessoas (paginado)")
    public ResponseEntity<Page<EntityModel<PessoaResponse>>> listar(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<EntityModel<PessoaResponse>> page = pessoaService.listarTodos(pageable)
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PessoaController.class).buscarPorId(p.id())).withSelfRel()));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID")
    public ResponseEntity<EntityModel<PessoaResponse>> buscarPorId(@PathVariable Long id) {
        PessoaResponse response = pessoaService.buscarPorId(id);
        EntityModel<PessoaResponse> model = EntityModel.of(response,
                linkTo(methodOn(PessoaController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PessoaController.class).listar(Pageable.unpaged())).withRel("pessoas"));
        return ResponseEntity.ok(model);
    }

    @GetMapping("/missao/{missaoId}")
    @Operation(summary = "Listar pessoas de uma missão")
    public ResponseEntity<List<PessoaResponse>> listarPorMissao(@PathVariable Long missaoId) {
        return ResponseEntity.ok(pessoaService.listarPorMissao(missaoId));
    }

    @PostMapping
    @Operation(summary = "Cadastrar pessoa")
    public ResponseEntity<EntityModel<PessoaResponse>> criar(@Valid @RequestBody PessoaRequest request) {
        PessoaResponse response = pessoaService.criar(request);
        EntityModel<PessoaResponse> model = EntityModel.of(response,
                linkTo(methodOn(PessoaController.class).buscarPorId(response.id())).withSelfRel());
        return ResponseEntity.status(201).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pessoa")
    public ResponseEntity<EntityModel<PessoaResponse>> atualizar(
            @PathVariable Long id, @Valid @RequestBody PessoaRequest request) {
        PessoaResponse response = pessoaService.atualizar(id, request);
        EntityModel<PessoaResponse> model = EntityModel.of(response,
                linkTo(methodOn(PessoaController.class).buscarPorId(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pessoa")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pessoaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
