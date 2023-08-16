package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.EstadoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.EstadoResponse;
import com.viniciusvieira.backend.domain.model.usuario.Estado;
import com.viniciusvieira.backend.domain.service.CascadeDeleteService;
import com.viniciusvieira.backend.domain.service.usuario.CrudEstadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/estados")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EstadoController {
    private final CrudEstadoService crudEstadoService;
    private final CascadeDeleteService cascadeDeleteService;

    @GetMapping
    public ResponseEntity<List<Estado>> buscarTodos(){
        return ResponseEntity.ok(crudEstadoService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<EstadoResponse> inserir(@RequestBody @Valid EstadoRequest estadoRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudEstadoService.inserir(estadoRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoResponse> alterar(@PathVariable Long id, @RequestBody @Valid EstadoRequest estadoRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(crudEstadoService.alterar(id, estadoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        cascadeDeleteService.cascadeDeleteEstado(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
