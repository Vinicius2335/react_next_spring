package com.viniciusvieira.backend.api.controller;

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
    public ResponseEntity<Cidade> inserir(@RequestBody Cidade cidade){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudCidadeService.inserir(cidade));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cidade> alterar(@PathVariable Long id, @RequestBody Cidade cidade){
        return ResponseEntity.ok(crudCidadeService.alterar(id, cidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudCidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
