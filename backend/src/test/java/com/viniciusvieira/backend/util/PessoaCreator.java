package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.CidadeIdRequest;
import com.viniciusvieira.backend.api.representation.model.request.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.PessoaResponse;
import com.viniciusvieira.backend.domain.model.Pessoa;

import java.time.OffsetDateTime;

public abstract class PessoaCreator {
    public static Pessoa mockPessoa(){
        return Pessoa.builder()
                .id(1L)
                .cep("01001-000")
                .cpf("302.218.730-07")
                .cidade(CidadeCreator.mockCidade())
                .nome("Teste 01")
                .senha("teste")
                .email("teste@gmail.com")
                .endereco("rua teste")
                .build();
    }

    public static Pessoa mockPessoaToUpdate(OffsetDateTime dataCriacao) {
        return Pessoa.builder()
                .id(1L)
                .cep("01001-002")
                .cpf("791.419.531-69")
                .cidade(CidadeCreator.mockCidade())
                .nome("Teste Update")
                .senha("update")
                .email("update@gmail.com")
                .endereco("rua teste update")
                .dataCriacao(dataCriacao)
                .build();
    }

    public static PessoaResponse mockPessoaResponse() {
        return PessoaResponse.builder()
                .cep("01001-000")
                .cpf("302.218.730-07")
                .cidade(CidadeCreator.mockCidadeResponse())
                .nome("Teste 01")
                .senha("teste")
                .email("teste@gmail.com")
                .endereco("rua teste")
                .build();
    }

    public static PessoaResponse mockPessoaResponseUpdate() {
        return PessoaResponse.builder()
                .cep("01001-002")
                .cpf("791.419.531-69")
                .cidade(CidadeCreator.mockCidadeResponse())
                .nome("Teste Update")
                .senha("update")
                .email("update@gmail.com")
                .endereco("rua teste update")
                .build();
    }

    public static PessoaRequest mockPessoaRequestToSave() {
        return PessoaRequest.builder()
                .cep("01001-000")
                .cpf("302.218.730-07")
                .nome("Teste 01")
                .senha("teste")
                .email("teste@gmail.com")
                .endereco("rua teste")
                .cidade(new CidadeIdRequest(1L))
                .build();
    }

    public static PessoaRequest mockPessoaRequestToUpdate() {
        return PessoaRequest.builder()
                .cep("01001-002")
                .cpf("791.419.531-69")
                .nome("Teste Update")
                .senha("update")
                .email("update@gmail.com")
                .endereco("rua teste update")
                .cidade(new CidadeIdRequest(1L))
                .build();
    }

    public static PessoaRequest mockInvalidPessoaRequest() {
        return PessoaRequest.builder()
                .cep(null)
                .cpf(null)
                .nome(null)
                .senha(null)
                .email(null)
                .endereco(null)
                .cidade(null)
                .build();
    }
}
