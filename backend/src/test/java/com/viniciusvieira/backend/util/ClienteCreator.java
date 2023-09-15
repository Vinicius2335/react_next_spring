package com.viniciusvieira.backend.util;

import com.github.javafaker.Faker;
import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;

import java.util.Locale;

public abstract class ClienteCreator {
    public static ClienteRequest createClienteRequest(){
        Faker faker = createFaker();
        return ClienteRequest.builder()
                .cpf("152.497.740-32")
                .email(faker.internet().emailAddress())
                .nome(faker.name().fullName())
                .endereco(EnderecoCreator.createEnderecoRequest())
                .build();
    }

    public static ClienteRequest createInvalidClienteRequest(){
        return ClienteRequest.builder()
                .cpf(null)
                .email(null)
                .nome(null)
                .endereco(null)
                .build();
    }

    private static Faker createFaker(){
        return new Faker(new Locale("pt_BR"));
    }
}
