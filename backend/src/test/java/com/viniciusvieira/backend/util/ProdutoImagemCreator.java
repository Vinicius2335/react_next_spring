package com.viniciusvieira.backend.util;

import com.github.javafaker.Faker;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;

import java.time.OffsetDateTime;
import java.util.Locale;

public abstract class ProdutoImagemCreator {
    public static ProdutoImagem createProdutoImagem(){
        Faker faker = createFaker();
        return ProdutoImagem.builder()
                .id(1L)
                .nome(faker.commerce().productName())
                .produto(ProdutoCreator.createProduto())
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }

    public static ProdutoImagemResponse createProdutoImagemResponse(){
        Faker faker = createFaker();
        return ProdutoImagemResponse.builder()
                .nome(faker.commerce().productName())
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    private static Faker createFaker(){
        return new Faker(new Locale("pt_BR"));
    }
}
