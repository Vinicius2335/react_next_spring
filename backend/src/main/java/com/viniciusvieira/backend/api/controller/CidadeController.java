package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.CidadeRequest;
import com.viniciusvieira.backend.api.representation.model.response.CidadeResponse;
import com.viniciusvieira.backend.domain.model.Cidade;
import com.viniciusvieira.backend.domain.service.CascadeDeleteService;
import com.viniciusvieira.backend.domain.service.CrudCidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cidades")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CidadeController {
    private final CrudCidadeService crudCidadeService;
    private final CascadeDeleteService cascadeDeleteService;

    @GetMapping
    public ResponseEntity<List<Cidade>> buscarTodos(){
        return ResponseEntity.ok(crudCidadeService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<CidadeResponse> inserir(@RequestBody @Valid CidadeRequest cidadeRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudCidadeService.inserir(cidadeRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadeResponse> alterar(@PathVariable Long id, @RequestBody @Valid CidadeRequest cidadeRequest){
        return ResponseEntity.ok(crudCidadeService.alterar(id, cidadeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
//        crudCidadeService.excluir(id);
        cascadeDeleteService.cascadeDeleteCidade(id);
        return ResponseEntity.noContent().build();
    }

}
