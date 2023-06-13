package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.model.ProdutoImagem;

import java.time.OffsetDateTime;

public abstract class ProdutoImagemCreator {

    public static ProdutoImagem mockProdutoImagem(){
        return ProdutoImagem.builder()
                .id(1L)
                .nome("image.png")
                .produto(ProdutoCreator.mockProduto())
                .build();
    }

    public static ProdutoImagem mockProdutoImagemAlterar(){
        return ProdutoImagem.builder()
                .id(1L)
                .nome("teste.png")
                .produto(ProdutoCreator.mockProduto())
                .build();
    }

    public static ProdutoImagemResponse mockProdutoImagemResponse(){
        return new ProdutoImagemResponse("image.png", OffsetDateTime.now(), OffsetDateTime.now());
    }
}
