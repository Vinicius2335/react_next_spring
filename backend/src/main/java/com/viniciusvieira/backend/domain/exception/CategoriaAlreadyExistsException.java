package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class CategoriaAlreadyExistsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -5231692531856558329L;

    public CategoriaAlreadyExistsException() {
        super();
    }

    public CategoriaAlreadyExistsException(String message) {
        super(message);
    }
}
