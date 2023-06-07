package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.model.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.service.CrudCarrinhoDeCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carrinhos")
public class CarrinhoDeCompraController {
    private final CrudCarrinhoDeCompraService crudCarrinhoDeCompraService;

    @GetMapping
    public ResponseEntity<List<CarrinhoDeCompra>> buscarTodos(){
        return ResponseEntity.ok(crudCarrinhoDeCompraService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<CarrinhoDeCompraResponse> inserir(@RequestBody @Valid CarrinhoDeCompraRequest estadoRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudCarrinhoDeCompraService.inserir(estadoRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarrinhoDeCompraResponse> alterar(@PathVariable Long id, @RequestBody @Valid CarrinhoDeCompraRequest estadoRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(crudCarrinhoDeCompraService.alterar(id, estadoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudCarrinhoDeCompraService.excluir(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
