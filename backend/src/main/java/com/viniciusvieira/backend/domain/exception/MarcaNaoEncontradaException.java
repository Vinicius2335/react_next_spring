package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class MarcaNaoEncontradaException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1950817890244937679L;

    public MarcaNaoEncontradaException() {
    }

    public MarcaNaoEncontradaException(String message) {
        super(message);
    }
}
