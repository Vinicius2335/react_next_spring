package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class CategoriaNaoEncontradaException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -4098125682377374859L;

    public CategoriaNaoEncontradaException() {
    }

    public CategoriaNaoEncontradaException(String message) {
        super(message);
    }
}
