package com.viniciusvieira.backend.domain.exception.venda;

import java.io.Serial;

public class CarrinhoDeCompraNaoEncontradoException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2606955861255730884L;

    public CarrinhoDeCompraNaoEncontradoException() {
    }

    public CarrinhoDeCompraNaoEncontradoException(String message) {
        super(message);
    }
}
