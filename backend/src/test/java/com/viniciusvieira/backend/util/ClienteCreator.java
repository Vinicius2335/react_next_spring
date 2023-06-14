package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.CidadeIdRequest;
import com.viniciusvieira.backend.api.representation.model.request.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.PessoaResponse;

public abstract class ClienteCreator {
    public static ClienteRequest mockClienteRequest(){
        return ClienteRequest.builder()
                .cep("01001-002")
                .cpf(PessoaCreator.mockPessoa().getCpf())
                .nome("Cliente Teste")
                .email("cliente@gmail.com")
                .cidade(new CidadeIdRequest(1L))
                .endereco("Endereço do cliente para teste")
                .build();
    }

    public static ClienteRequest mockInvalidClienteRequest(){
        return ClienteRequest.builder()
                .cep(null)
                .cpf("")
                .nome(null)
                .email("")
                .cidade(new CidadeIdRequest(1L))
                .endereco(null)
                .build();
    }

    public static PessoaResponse mockClientePessoaResponse(){
        return PessoaResponse.builder()
                .cep("01001-002")
                .cpf("791.419.531-69")
                .nome("Cliente Teste")
                .email("cliente@gmail.com")
                .endereco("Endereço do cliente para teste")
                .nomeCidade(CidadeCreator.mockCidade().getNome())
                .build();
    }
}
