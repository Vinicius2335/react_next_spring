package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.domain.model.ProdutoImagem;
import com.viniciusvieira.backend.domain.service.CrudProdutoImagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/imagens")
public class UploadController {
    private final CrudProdutoImagemService crudProdutoImagemService;

    @GetMapping
    public ResponseEntity<List<ProdutoImagem>> buscarTodos(){
        return ResponseEntity
                .ok(crudProdutoImagemService.buscarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        crudProdutoImagemService.excluir(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
