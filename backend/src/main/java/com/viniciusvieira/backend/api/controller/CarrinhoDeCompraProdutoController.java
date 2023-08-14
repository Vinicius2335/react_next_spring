package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.CarrinhoDeCompraProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.CarrinhoDeCompraProdutoResponse;
import com.viniciusvieira.backend.domain.model.CarrinhoDeCompraProduto;
import com.viniciusvieira.backend.domain.service.CrudCarrinhoDeCompraProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carrinho-produto")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CarrinhoDeCompraProdutoController {
    private final CrudCarrinhoDeCompraProdutoService crudCarrinhoDeCompraProdutoService;

    @GetMapping
    public ResponseEntity<List<CarrinhoDeCompraProduto>> buscarTodos(){
        return ResponseEntity.ok(crudCarrinhoDeCompraProdutoService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<CarrinhoDeCompraProdutoResponse> inserir(@RequestBody @Valid CarrinhoDeCompraProdutoRequest carrinhoDeCompraProdutoRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudCarrinhoDeCompraProdutoService.inserir(carrinhoDeCompraProdutoRequest));
    }
}
