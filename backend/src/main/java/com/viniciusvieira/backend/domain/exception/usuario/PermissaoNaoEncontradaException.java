package com.viniciusvieira.backend.domain.exception.usuario;

import java.io.Serial;

public class PermissaoNaoEncontradaException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1950817890244937679L;

    public PermissaoNaoEncontradaException() {
    }

    public PermissaoNaoEncontradaException(String message) {
        super(message);
    }
}
