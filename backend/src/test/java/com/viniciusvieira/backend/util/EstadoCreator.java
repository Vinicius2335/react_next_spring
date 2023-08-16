package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.usuario.EstadoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.EstadoResponse;
import com.viniciusvieira.backend.domain.model.usuario.Estado;

import java.time.OffsetDateTime;

public abstract class EstadoCreator {
    public static Estado mockEstado(){
        return Estado.builder()
                .id(1L)
                .nome("Paraná")
                .sigla("PR")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static EstadoResponse mockEstadoResponse(){
        return EstadoResponse.builder()
                .nome("São Paulo")
                .sigla("SP")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static EstadoResponse mockEstadoResponseUpdate(){
        return EstadoResponse.builder()
                .nome("São Paulo")
                .sigla("SP")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static EstadoRequest mockEstadoRequestToSave(){
        return EstadoRequest.builder()
                .nome("Paraná")
                .sigla("PR")
                .build();
    }

    public static EstadoRequest mockEstadoRequestToUpdate(){
        return EstadoRequest.builder()
                .nome("São Paulo")
                .sigla("SP")
                .build();
    }

    public static EstadoRequest mockInvalidEstadoRequest(){
        return EstadoRequest.builder()
                .nome(null)
                .sigla(null)
                .build();
    }

    public static Estado mockEstadoToUpdate(OffsetDateTime dataCriacao){
        return Estado.builder()
                .id(1L)
                .nome("São Paulo")
                .sigla("SP")
                .dataCriacao(dataCriacao)
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }
}
