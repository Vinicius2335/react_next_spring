package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.venda.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CategoriaResponse;
import com.viniciusvieira.backend.domain.model.venda.Categoria;

import java.time.OffsetDateTime;

public abstract class CategoriaCreator {
    public static Categoria mockCategoria(){
        return Categoria.builder()
                .id(1L)
                .nome("Eletronico")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static CategoriaResponse mockCategoriaResponse(){
        return CategoriaResponse.builder()
                .nome("Eletronico")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static CategoriaRequest mockCategoriaRequest(){
        return CategoriaRequest.builder()
                .nome("Eletronico")
                .build();
    }

    public static CategoriaRequest mockInvalidCategoriaRequest(){
        return CategoriaRequest.builder()
                .nome(null)
                .build();
    }

    public static Categoria mockCategoriaToUpdated(OffsetDateTime dataCriacao){
        return Categoria.builder()
                .id(1L)
                .nome("Celular")
                .dataCriacao(dataCriacao)
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static CategoriaRequest mockCategoriaRequestToUpdate(){
        return CategoriaRequest.builder()
                .nome("Celular")
                .build();
    }

    public static CategoriaResponse mockCategoriaResponseUpdate(){
        return CategoriaResponse.builder()
                .nome("Celular")
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }
}
