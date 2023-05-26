package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.CategoriaResponse;
import com.viniciusvieira.backend.domain.model.Categoria;
import com.viniciusvieira.backend.domain.service.CrudCategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CrudCategoriaService crudCategoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> buscarTodos(){
        return ResponseEntity.ok(crudCategoriaService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPeloId(@PathVariable Long id){
        return ResponseEntity.ok(crudCategoriaService.buscarPeloId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> inserir(@RequestBody CategoriaRequest categoriaRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudCategoriaService.inserir(categoriaRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> alterar(@PathVariable Long id, @RequestBody CategoriaRequest categoriaRequest){
        return ResponseEntity.ok(crudCategoriaService.alterar(id, categoriaRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudCategoriaService.excluir(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
