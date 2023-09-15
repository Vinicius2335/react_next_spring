package com.viniciusvieira.backend.util;

import com.github.javafaker.Faker;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.OffsetDateTime;
import java.util.Locale;

public abstract class ProdutoImagemCreator {
    public static ProdutoImagem createProdutoImagem(){
        String fileCode = RandomStringUtils.randomAlphabetic(8);
        Faker faker = createFaker();
        return ProdutoImagem.builder()
                .id(1L)
                .nome(faker.commerce().productName() + ".png")
                .produto(ProdutoCreator.createProduto())
                .imageCode(fileCode)
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }

    public static ProdutoImagemResponse createProdutoImagemResponse(ProdutoImagem produtoImagem){
        Faker faker = createFaker();
        return ProdutoImagemResponse.builder()
                .nome(produtoImagem.getNome())
                .imageCode(produtoImagem.getImageCode())
                .dataCriacao(produtoImagem.getDataCriacao())
                .dataAtualizacao(produtoImagem.getDataAtualizacao())
                .build();
    }

    private static Faker createFaker(){
        return new Faker(new Locale("pt_BR"));
    }
}
