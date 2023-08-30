package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class NegocioException  extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 405438010308145926L;

    public NegocioException() {
    }

    public NegocioException(String message) {
        super(message);
    }

    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
