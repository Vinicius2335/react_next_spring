package com.viniciusvieira.backend.util;

import java.time.OffsetDateTime;

public abstract class CidadeCreator {
    public static Cidade mockCidade() {
        return Cidade.builder()
                .id(1L)
                .estado(EstadoCreator.mockEstado())
                .nome("Cascavel")
                .build();
    }

    public static Cidade mockCidateToUpdate(OffsetDateTime dataCriacao) {
        return Cidade.builder()
                .id(1L)
                .dataCriacao(dataCriacao)
                .estado(EstadoCreator.mockEstado())
                .nome("Foz do Iguaçu")
                .build();
    }

    public static CidadeResponse mockCidadeResponse() {
        return CidadeResponse.builder()
                .nome("Cascavel")
                .nomeEstado(EstadoCreator.mockEstado().getNome())
                .build();
    }

    public static CidadeRequest mockCidadeRequestToSave(){
        return CidadeRequest.builder()
                .nome("Cascavel")
                .estadoId(1L)
                .build();
    }

    public static CidadeRequest mockCidadeRequestToUpdate(){
        return CidadeRequest.builder()
                .nome("Foz do Iguaçu")
                .estadoId(1L)
                .build();
    }

    public static CidadeRequest mockInvalidCidadeRequestToSave(){
        return CidadeRequest.builder()
                .nome(null)
                .estadoId(0L)
                .build();
    }

    public static CidadeResponse mockCidadeResponseUpdated(){
        return CidadeResponse.builder()
                .nome("Foz do Iguaçu")
                .nomeEstado(EstadoCreator.mockEstado().getNome())
                .build();
    }
}
