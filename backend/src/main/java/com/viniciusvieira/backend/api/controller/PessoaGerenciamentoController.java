package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.service.PessoaGerenciamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gerenciamento")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PessoaGerenciamentoController {
    private final PessoaGerenciamentoService pessoaGerenciamentoService;

    @PutMapping("/solicitar-codigo")
    public ResponseEntity<Void> recuperarCodigoViaEmail(@RequestParam("email") String email){
        pessoaGerenciamentoService.solicitarCodigo(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody PessoaGerenciamentoRequest pessoaGerenciamentoRequest){
        pessoaGerenciamentoService.alterarSenha(pessoaGerenciamentoRequest);
        return ResponseEntity.noContent().build();
    }
}
