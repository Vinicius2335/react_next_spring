package com.viniciusvieira.backend.domain.exception.usuario;

import java.io.Serial;

public class PermissaoAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6226876830966794806L;

    public PermissaoAlreadyExistsException(String message) {
        super(message);
    }

    public PermissaoAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
