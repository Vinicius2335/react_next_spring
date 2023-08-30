package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class CategoriaNaoEncontradoException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -4098125682377374859L;

    public CategoriaNaoEncontradoException() {
    }

    public CategoriaNaoEncontradoException(String message) {
        super(message);
    }
}
