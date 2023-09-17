package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.service.usuario.SalvarClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final SalvarClienteService salvarClienteService;

    @PostMapping
    public ResponseEntity<PessoaResponse> inserir(@RequestBody @Valid ClienteRequest clienteRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salvarClienteService.inserirCliente(clienteRequest));
    }

}
