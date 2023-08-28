package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.domain.model.usuario.Permissao;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public abstract class PermissaoCreator {

    public static Permissao createPermissao(){
        return Permissao.builder()
                .pessoas(new ArrayList<>())
                .id(1L)
                .nome("CLIENTE")
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }
}
