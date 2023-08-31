package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class CreateTemplateException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -5826408891769845957L;

    public CreateTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateTemplateException(String message) {
        super(message);
    }
}
