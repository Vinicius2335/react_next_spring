package com.viniciusvieira.backend.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

// NOTE depois de criar o exceptionHandler, excluir o ResponseStatus
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PermissaoNaoEncontradaException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1950817890244937679L;

    public PermissaoNaoEncontradaException() {
    }

    public PermissaoNaoEncontradaException(String message) {
        super(message);
    }
}