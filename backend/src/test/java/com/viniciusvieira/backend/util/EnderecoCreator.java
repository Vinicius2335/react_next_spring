package com.viniciusvieira.backend.util;

import com.github.javafaker.Faker;
import com.viniciusvieira.backend.api.representation.model.request.usuario.EnderecoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.EnderecoResponse;
import com.viniciusvieira.backend.domain.model.usuario.Endereco;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;

import java.util.Locale;

public abstract class EnderecoCreator {
    private static final Faker FAKER = new Faker(new Locale("pt_BR"));

    public static Endereco createEndereco(){
        return Endereco.builder()
                .logradouro(FAKER.address().streetAddress())
                .cidade(FAKER.address().city())
                .estado(FAKER.address().state())
                .cep("01001-000")
                .build();
    }

    public static Endereco createInvalidEndereco(){
        return Endereco.builder()
                .build();
    }

    public static EnderecoRequest createEnderecoRequest(){
        return EnderecoRequest.builder()
                .logradouro(FAKER.address().streetAddress())
                .cidade(FAKER.address().city())
                .estado(FAKER.address().state())
                .cep("01001-000")
                .build();
    }

    public static EnderecoResponse createEnderecoResponse(Pessoa pessoa){
        return EnderecoResponse.builder()
                .cep(pessoa.getEndereco().getCep())
                .logradouro(pessoa.getEndereco().getLogradouro())
                .cidade(pessoa.getEndereco().getCidade())
                .estado(pessoa.getEndereco().getEstado())
                .build();
    }
}
