package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaIdRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;

public abstract class CarrinhoDeCompraCreator {
    public static CarrinhoDeCompra mockCarrinhoDeCompra(){
        return CarrinhoDeCompra.builder()
                .id(1L)
                .pessoa(PessoaCreator.mockPessoa())
                .situacao("carrinho de compra")
                .observacao("carrinho de compra")
                .build();
    }

    public static CarrinhoDeCompra mockCarrinhoDeCompraUpdate(){
        return CarrinhoDeCompra.builder()
                .id(1L)
                .pessoa(PessoaCreator.mockPessoa())
                .situacao("teste")
                .observacao("teste")
                .build();
    }

    public static CarrinhoDeCompraResponse mockCarrinhoDeCompraResponse(){
        return CarrinhoDeCompraResponse.builder()
                .situacao("carrinho de compra")
                .observacao("carrinho de compra")
                .pessoa(PessoaCreator.mockPessoaResponse())
                .build();
    }

    public static CarrinhoDeCompraResponse mockCarrinhoDeCompraResponseUpdated(){
        return CarrinhoDeCompraResponse.builder()
                .situacao("teste")
                .observacao("teste")
                .pessoa(PessoaCreator.mockPessoaResponse())
                .build();
    }

    public static CarrinhoDeCompraRequest mockCarrinhoDeCompraRequest(){
        return CarrinhoDeCompraRequest.builder()
                .situacao("carrinho de compra")
                .observacao("carrinho de compra")
                .pessoa(new PessoaIdRequest(1L))
                .build();
    }

    public static CarrinhoDeCompraRequest mockCarrinhoDeCompraRequestToUpdate(){
        return CarrinhoDeCompraRequest.builder()
                .situacao("teste")
                .observacao("teste")
                .pessoa(new PessoaIdRequest(1L))
                .build();
    }

    public static CarrinhoDeCompraRequest mockInvalidCarrinhoDeCompraRequest(){
        return CarrinhoDeCompraRequest.builder()
                .situacao(null)
                .observacao(null)
                .pessoa(null)
                .build();
    }
}
