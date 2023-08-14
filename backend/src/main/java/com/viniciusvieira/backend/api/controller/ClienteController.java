package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.PessoaResponse;
import com.viniciusvieira.backend.domain.service.SalvarClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClienteController {
    private final SalvarClienteService salvarClienteService;

    @PostMapping
    public ResponseEntity<PessoaResponse> inserir(@RequestBody @Valid ClienteRequest clienteRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salvarClienteService.inserirCliente(clienteRequest));
    }

}
