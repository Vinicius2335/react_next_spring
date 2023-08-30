package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class ProdutoNaoEncontradoException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 5115769234855171207L;

    public ProdutoNaoEncontradoException() {
    }

    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }
}
