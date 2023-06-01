package com.viniciusvieira.backend.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProdutoNaoEncontradoException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 5115769234855171207L;

    public ProdutoNaoEncontradoException() {
    }

    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }
}
