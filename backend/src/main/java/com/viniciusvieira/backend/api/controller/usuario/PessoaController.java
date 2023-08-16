package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pessoas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PessoaController {
    private final CrudPessoaService crudPessoaService;

    @GetMapping
    public ResponseEntity<List<Pessoa>> buscarTodos(){
        return ResponseEntity.ok(crudPessoaService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<PessoaResponse> inserir(@RequestBody @Valid PessoaRequest pessoaRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudPessoaService.inserir(pessoaRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponse> alterar(@PathVariable Long id, @RequestBody @Valid PessoaRequest pessoaRequest){
        return ResponseEntity.ok(crudPessoaService.alterar(id, pessoaRequest));
    }

    @DeleteMapping("/{idPessoa}/permissoes/{idPermissao}")
    public ResponseEntity<Void> excluirPermissao(@PathVariable Long idPessoa, @PathVariable Long idPermissao){
        crudPessoaService.excluirPermissao(idPessoa,  idPermissao);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudPessoaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
