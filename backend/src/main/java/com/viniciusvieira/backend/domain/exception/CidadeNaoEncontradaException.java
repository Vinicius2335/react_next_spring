package com.viniciusvieira.backend.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CidadeNaoEncontradaException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -7610332350898459661L;

    public CidadeNaoEncontradaException() {
    }

    public CidadeNaoEncontradaException(String message) {
        super(message);
    }
}
