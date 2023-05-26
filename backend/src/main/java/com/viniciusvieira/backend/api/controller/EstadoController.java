package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.EstadoRequest;
import com.viniciusvieira.backend.api.representation.model.response.EstadoResponse;
import com.viniciusvieira.backend.domain.model.Estado;
import com.viniciusvieira.backend.domain.service.CrudEstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/estados")
public class EstadoController {
    private final CrudEstadoService crudEstadoService;

    @GetMapping
    public ResponseEntity<List<Estado>> buscarTodos(){
        return ResponseEntity.ok(crudEstadoService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<EstadoResponse> inserir(@RequestBody EstadoRequest estadoRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudEstadoService.inserir(estadoRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoResponse> alterar(@PathVariable Long id, @RequestBody EstadoRequest estadoRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(crudEstadoService.alterar(id, estadoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudEstadoService.excluir(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
