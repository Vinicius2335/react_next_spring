package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;

import java.time.OffsetDateTime;

public abstract class CarrinhoDeCompraCreator {
    public static CarrinhoDeCompra createCarrinhoDeCompra(){
        return CarrinhoDeCompra.builder()
                .id(1L)
                .observacao("Nenhuma")
                .situacao("EM USO")
                .pessoa(PessoaCreator.createPessoa())
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();
    }

    public static CarrinhoDeCompraRequest createCarrinhoDeCompraRequest() {
        return CarrinhoDeCompraRequest.builder()
                .pessoaId(1L)
                .observacao("Nenhuma")
                .situacao("EM USO")
                .build();
    }

    public static CarrinhoDeCompraResponse createCarrinhoDeCompraResponse(PessoaResponse pessoaResponse) {
        return CarrinhoDeCompraResponse.builder()
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .pessoa(pessoaResponse)
                .situacao("EM USO")
                .build();
    }

    public static CarrinhoDeCompraRequest createInvalidCarrinhoDeCompraRequest() {
        return CarrinhoDeCompraRequest.builder()
                .pessoaId(null)
                .observacao(null)
                .situacao(null)
                .build();
    }
}
