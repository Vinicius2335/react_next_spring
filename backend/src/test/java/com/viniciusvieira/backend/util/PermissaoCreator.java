package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PermissaoResponse;
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

    public static PermissaoResponse createPermissaoResponse(Permissao permissao){
        return PermissaoResponse.builder()
                .nome(permissao.getNome())
                .dataAtualizacao(permissao.getDataAtualizacao())
                .dataCriacao(permissao.getDataCriacao())
                .build();
    }

    public static PermissaoRequest createPermissaoRequest(){
        return PermissaoRequest.builder()
                .nome("CLIENTE")
                .build();
    }

    public static PermissaoRequest createInvalidPermissaoRequest(){
        return PermissaoRequest.builder()
                .nome("")
                .build();
    }
}
