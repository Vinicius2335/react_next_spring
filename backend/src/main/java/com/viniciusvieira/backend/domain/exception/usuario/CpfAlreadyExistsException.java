package com.viniciusvieira.backend.domain.exception.usuario;

import java.io.Serial;

public class CpfAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -565957658568674909L;

    public CpfAlreadyExistsException(String message) {
        super(message);
    }

    public CpfAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
