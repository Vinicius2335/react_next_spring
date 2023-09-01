package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.venda.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CategoriaResponse;
import com.viniciusvieira.backend.domain.model.venda.Categoria;

import java.time.OffsetDateTime;

public abstract class CategoriaCreator {
    public static Categoria createCategoria(){
        return Categoria.builder()
                .id(1L)
                .nome("Eletrônico")
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }

    public static Categoria createOtherCategoria(){
        return Categoria.builder()
                .id(2L)
                .nome("Roupa")
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }

    public static CategoriaRequest createCategoriaRequest(){
        return CategoriaRequest.builder()
                .nome("Eletrônico")
                .build();
    }

    public static CategoriaRequest createInvalidCategoriaRequest(){
        return CategoriaRequest.builder()
                .nome(null)
                .build();
    }

    public static CategoriaResponse createCategoriaResponse(){
        return CategoriaResponse.builder()
                .nome("Eletrônico")
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }
}
