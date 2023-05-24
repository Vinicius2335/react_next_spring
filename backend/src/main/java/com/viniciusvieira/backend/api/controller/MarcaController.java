package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.domain.model.Marca;
import com.viniciusvieira.backend.domain.service.CrudMarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/marcas")
public class MarcaController {
    private final CrudMarcaService crudMarcaService;

    @GetMapping
    public ResponseEntity<List<Marca>> buscarTodos(){
        return ResponseEntity.ok(crudMarcaService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> buscarPeloId(@PathVariable Long id){
        return ResponseEntity.ok(crudMarcaService.buscarPeloId(id));
    }

    @PostMapping
    public ResponseEntity<Marca> inserir(@RequestBody Marca marca){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudMarcaService.inserir(marca));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> alterar(@PathVariable Long id, @RequestBody Marca marca){
        return ResponseEntity.ok(crudMarcaService.alterar(id, marca));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudMarcaService.excluir(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
