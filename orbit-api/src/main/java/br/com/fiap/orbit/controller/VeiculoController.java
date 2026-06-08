package br.com.fiap.orbit.controller;

import br.com.fiap.orbit.dto.request.VeiculoAereoRequest;
import br.com.fiap.orbit.dto.request.VeiculoTerrestreRequest;
import br.com.fiap.orbit.dto.response.VeiculoResponse;
import br.com.fiap.orbit.service.VeiculoService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/veiculos")
@RequiredArgsConstructor
@Tag(name = "Veículos", description = "Gerenciamento de veículos operacionais")
@SecurityRequirement(name = "Bearer Authentication")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @GetMapping
    @Operation(summary = "Listar todos os veículos (paginado)")
    public ResponseEntity<Page<EntityModel<VeiculoResponse>>> listar(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<EntityModel<VeiculoResponse>> page = veiculoService.listarTodos(pageable)
                .map(v -> EntityModel.of(v,
                        linkTo(methodOn(VeiculoController.class).buscarPorId(v.id())).withSelfRel()));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veículo por ID")
    public ResponseEntity<EntityModel<VeiculoResponse>> buscarPorId(@PathVariable Long id) {
        VeiculoResponse response = veiculoService.buscarPorId(id);
        EntityModel<VeiculoResponse> model = EntityModel.of(response,
                linkTo(methodOn(VeiculoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listar(Pageable.unpaged())).withRel("veiculos"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/terrestre")
    @Operation(summary = "Cadastrar veículo terrestre")
    public ResponseEntity<EntityModel<VeiculoResponse>> criarTerrestre(
            @Valid @RequestBody VeiculoTerrestreRequest request) {
        VeiculoResponse response = veiculoService.criarTerrestre(request);
        EntityModel<VeiculoResponse> model = EntityModel.of(response,
                linkTo(methodOn(VeiculoController.class).buscarPorId(response.id())).withSelfRel());
        return ResponseEntity.status(201).body(model);
    }

    @PostMapping("/aereo")
    @Operation(summary = "Cadastrar veículo aéreo")
    public ResponseEntity<EntityModel<VeiculoResponse>> criarAereo(
            @Valid @RequestBody VeiculoAereoRequest request) {
        VeiculoResponse response = veiculoService.criarAereo(request);
        EntityModel<VeiculoResponse> model = EntityModel.of(response,
                linkTo(methodOn(VeiculoController.class).buscarPorId(response.id())).withSelfRel());
        return ResponseEntity.status(201).body(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar veículo")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        veiculoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
