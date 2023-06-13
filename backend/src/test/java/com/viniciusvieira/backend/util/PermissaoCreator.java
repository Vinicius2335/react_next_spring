package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.PermissaoResponse;
import com.viniciusvieira.backend.domain.model.Permissao;

public abstract class PermissaoCreator {
    public static Permissao mockPermissao(){
        return Permissao.builder()
                .id(1L)
                .nome("Usuário")
                .build();
    }

    public static Permissao mockPermissaoUpdated(){
        return Permissao.builder()
                .id(1L)
                .nome("Usuário")
                .build();
    }

    public static PermissaoResponse mockPermissaoResponse(){
        return PermissaoResponse.builder()
                .nome("Usuário")
                .build();
    }

    public static PermissaoResponse mockPermissaoResponseUpdated(){
        return PermissaoResponse.builder()
                .nome("Administrador")
                .build();
    }

    public static PermissaoRequest mockPermissaoRequest(){
        return PermissaoRequest.builder()
                .nome("Usuário")
                .build();
    }

    public static PermissaoRequest mockPermissaoRequestToUpdate(){
        return PermissaoRequest.builder()
                .nome("Administrador")
                .build();
    }

    public static PermissaoRequest mockInvalidPermissaoRequest(){
        return PermissaoRequest.builder()
                .nome(null)
                .build();
    }
}
