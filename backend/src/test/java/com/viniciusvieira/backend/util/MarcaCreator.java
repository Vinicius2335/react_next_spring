package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.domain.model.Marca;

import java.time.OffsetDateTime;

public abstract class MarcaCreator {
    public static Marca mockValidMarca(){
        return Marca.builder()
                .id(1L)
                .nome("Apple")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static Marca mockMarcaToUpdate(OffsetDateTime dataCriacao){
        return Marca.builder()
                .id(1L)
                .nome("Dell")
                .dataCriacao(dataCriacao)
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }
}
