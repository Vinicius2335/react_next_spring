package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.domain.model.Categoria;

import java.time.OffsetDateTime;

public abstract class CategoriaCreator {
    public static Categoria mockValidCategoria(){
        return Categoria.builder()
                .id(1L)
                .nome("Eletronico")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static Categoria mockCategoriaToUpdate(OffsetDateTime dataCriacao){
        return Categoria.builder()
                .id(1L)
                .nome("Celular")
                .dataCriacao(dataCriacao)
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }
}
