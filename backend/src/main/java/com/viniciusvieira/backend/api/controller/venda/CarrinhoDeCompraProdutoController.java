package com.viniciusvieira.backend.api.controller.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraProdutoResponse;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompraProduto;
import com.viniciusvieira.backend.domain.service.venda.CrudCarrinhoDeCompraProdutoService;
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
