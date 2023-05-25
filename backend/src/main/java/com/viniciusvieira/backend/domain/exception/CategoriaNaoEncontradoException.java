package com.viniciusvieira.backend.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

// NOTE depois de criar o exceptionHandler, excluir o ResponseStatus
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNaoEncontradoException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -4098125682377374859L;

    public CategoriaNaoEncontradoException() {
    }

    public CategoriaNaoEncontradoException(String message) {
        super(message);
    }
}
