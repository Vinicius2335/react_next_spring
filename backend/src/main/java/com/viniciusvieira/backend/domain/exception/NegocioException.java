package com.viniciusvieira.backend.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NegocioException  extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 405438010308145926L;

    public NegocioException() {
    }

    public NegocioException(String message) {
        super(message);
    }
}
