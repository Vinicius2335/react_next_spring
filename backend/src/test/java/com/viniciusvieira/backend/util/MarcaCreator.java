package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.venda.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.MarcaResponse;
import com.viniciusvieira.backend.domain.model.venda.Marca;

import java.time.OffsetDateTime;

public abstract class MarcaCreator {
    public static Marca createMarca(){
        return Marca.builder()
                .id(1L)
                .nome("Avell")
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }

    public static MarcaRequest createMarcaRequest(){
        return MarcaRequest.builder()
                .nome("Avell")
                .build();
    }

    public static MarcaRequest createInvalidMarcaRequest(){
        return MarcaRequest.builder()
                .nome(null)
                .build();
    }

    public static MarcaResponse createMarcaResponse(){
        return MarcaResponse.builder()
                .nome("Avell")
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }
}
