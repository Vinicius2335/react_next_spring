package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.PermissaoResponse;
import com.viniciusvieira.backend.domain.model.Permissao;
import com.viniciusvieira.backend.domain.service.CrudPermissaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/permissoes")
public class PermissaoController {
    private final CrudPermissaoService crudPermissaoService;

    @GetMapping
    public ResponseEntity<List<Permissao>> buscarTodos(){
        return ResponseEntity.ok(crudPermissaoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permissao> buscarPeloId(@PathVariable Long id){
        return ResponseEntity.ok(crudPermissaoService.buscarPeloId(id));
    }

    @PostMapping
    public ResponseEntity<PermissaoResponse> inserir(@RequestBody @Valid PermissaoRequest permissaoRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudPermissaoService.inserir(permissaoRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissaoResponse> alterar(@PathVariable Long id, @RequestBody @Valid PermissaoRequest permissaoRequest){
        return ResponseEntity.ok(crudPermissaoService.alterar(id, permissaoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudPermissaoService.excluir(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
