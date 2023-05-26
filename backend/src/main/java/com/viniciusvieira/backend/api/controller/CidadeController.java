package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.CidadeRequest;
import com.viniciusvieira.backend.api.representation.model.response.CidadeResponse;
import com.viniciusvieira.backend.domain.model.Cidade;
import com.viniciusvieira.backend.domain.service.CrudCidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cidades")
public class CidadeController {
    private final CrudCidadeService crudCidadeService;

    @GetMapping
    public ResponseEntity<List<Cidade>> buscarTodos(){
        return ResponseEntity.ok(crudCidadeService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<CidadeResponse> inserir(@RequestBody CidadeRequest cidadeRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudCidadeService.inserir(cidadeRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CidadeResponse> alterar(@PathVariable Long id, @RequestBody CidadeRequest cidadeRequest){
        return ResponseEntity.ok(crudCidadeService.alterar(id, cidadeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudCidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
