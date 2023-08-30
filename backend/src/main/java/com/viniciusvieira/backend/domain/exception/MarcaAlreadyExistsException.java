package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class MarcaAlreadyExistsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -4415293249072207376L;

    public MarcaAlreadyExistsException() {
    }

    public MarcaAlreadyExistsException(String message) {
        super(message);
    }
}
