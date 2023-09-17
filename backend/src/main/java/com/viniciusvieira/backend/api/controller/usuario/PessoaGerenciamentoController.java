package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.service.usuario.PessoaGerenciamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gerenciamento")
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
