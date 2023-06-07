package com.viniciusvieira.backend.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CarrinhoDeCompraNaoEncontradoException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2606955861255730884L;

    public CarrinhoDeCompraNaoEncontradoException() {
    }

    public CarrinhoDeCompraNaoEncontradoException(String message) {
        super(message);
    }
}
