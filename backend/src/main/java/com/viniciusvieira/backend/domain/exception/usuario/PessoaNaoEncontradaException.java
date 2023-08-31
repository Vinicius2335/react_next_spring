package com.viniciusvieira.backend.domain.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

public class PessoaNaoEncontradaException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -3515135809709471286L;

    public PessoaNaoEncontradaException() {
    }

    public PessoaNaoEncontradaException(String message) {
        super(message);
    }
}
