package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.service.usuario.SalvarClienteService;
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
