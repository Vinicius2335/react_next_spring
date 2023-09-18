package com.viniciusvieira.backend.api.controller.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.service.venda.CrudProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final CrudProdutoService crudProdutoService;

    @GetMapping
    public ResponseEntity<List<Produto>> buscarTodos() {
        return ResponseEntity.ok(crudProdutoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(crudProdutoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> inserir(@RequestBody @Valid ProdutoRequest produtoRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudProdutoService.inserir(produtoRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> alterar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest produtoRequest) {
        return ResponseEntity.ok(crudProdutoService.alterar(id, produtoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        crudProdutoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
