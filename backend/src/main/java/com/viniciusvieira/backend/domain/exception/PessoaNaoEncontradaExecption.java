package com.viniciusvieira.backend.domain.exception;

import java.io.Serial;

public class PessoaNaoEncontradaExecption extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -3515135809709471286L;

    public PessoaNaoEncontradaExecption() {
    }

    public PessoaNaoEncontradaExecption(String message) {
        super(message);
    }
}
