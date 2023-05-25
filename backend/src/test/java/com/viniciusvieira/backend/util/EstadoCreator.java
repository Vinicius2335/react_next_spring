package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.domain.model.Estado;
import com.viniciusvieira.backend.domain.model.Marca;

import java.time.OffsetDateTime;

public abstract class EstadoCreator {
    public static Estado mockValidEstado(){
        return Estado.builder()
                .id(1L)
                .nome("Apple")
                .sigla("Ap")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static Estado mockEstadoToUpdate(OffsetDateTime dataCriacao){
        return Estado.builder()
                .id(1L)
                .nome("Dell")
                .sigla("DL")
                .dataCriacao(dataCriacao)
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }
}
