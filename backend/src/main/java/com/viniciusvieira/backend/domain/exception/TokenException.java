package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class TokenException  extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -3708712103238108283L;

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(String message) {
        super(message);
    }
}
