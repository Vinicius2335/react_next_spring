package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.venda.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.MarcaResponse;
import com.viniciusvieira.backend.domain.model.venda.Marca;

import java.time.OffsetDateTime;

public abstract class MarcaCreator {
    public static Marca mockMarca(){
        return Marca.builder()
                .id(1L)
                .nome("Apple")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static MarcaResponse mockMarcaResponse(){
        return MarcaResponse.builder()
                .nome("Apple")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static MarcaRequest mockMarcaRequestToSave(){
        return MarcaRequest.builder()
                .nome("Apple")
                .build();
    }

    public static MarcaRequest mockInvalidMarcaRequestToSave(){
        return MarcaRequest.builder()
                .nome(null)
                .build();
    }

    public static MarcaRequest mockMarcaRequestToUpdate(){
        return MarcaRequest.builder()
                .nome("Dell")
                .build();
    }

    public static MarcaResponse mockMarcaResponseUpdate(){
        return MarcaResponse.builder()
                .nome("Dell")
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
