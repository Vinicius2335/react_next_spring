package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class ProdutoImagemNaoEncontradoException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -3515135809709471286L;

    public ProdutoImagemNaoEncontradoException() {
    }

    public ProdutoImagemNaoEncontradoException(String message) {
        super(message);
    }
}
