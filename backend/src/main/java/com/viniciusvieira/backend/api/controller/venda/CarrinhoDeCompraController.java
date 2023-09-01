package com.viniciusvieira.backend.api.controller.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.service.venda.CrudCarrinhoDeCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carrinhos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CarrinhoDeCompraController {
    private final CrudCarrinhoDeCompraService crudCarrinhoDeCompraService;

    @GetMapping
    public ResponseEntity<List<CarrinhoDeCompra>> buscarTodos(){
        return ResponseEntity.ok(crudCarrinhoDeCompraService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarrinhoDeCompra> buscarPeloId(@PathVariable Long id){
        return ResponseEntity.ok(crudCarrinhoDeCompraService.buscarPeloId(id));
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
