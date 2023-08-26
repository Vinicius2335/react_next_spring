package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.model.venda.Produto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public abstract class ProdutoCreator {

    public static Produto mockProduto(){
        return Produto.builder()
                .id(1L)
                .quantidade(10)
                .descricaoCurta("Computador")
                .descricaoDetalhada("Computador Teste")
                .valorCusto(new BigDecimal("10.00"))
                .valorVenda(new BigDecimal("20.00"))
                .marca(MarcaCreator.mockMarca())
                .categoria(CategoriaCreator.mockCategoria())
                .build();
    }

    public static Produto mockProdutoToUpdate(){
        return Produto.builder()
                .id(1L)
                .quantidade(20)
                .descricaoCurta("Celular")
                .descricaoDetalhada("Celular Teste")
                .valorCusto(new BigDecimal("20.00"))
                .valorVenda(new BigDecimal("40.00"))
                .marca(MarcaCreator.mockMarca())
                .categoria(CategoriaCreator.mockCategoria())
                .build();
    }

    public static ProdutoResponse mockProdutoResponse(){
        return ProdutoResponse.builder()
                .quantidade(10)
                .descricaoCurta("Computador")
                .descricaoDetalhada("Computador Teste")
                .valorCusto(new BigDecimal("10.00"))
                .valorVenda(new BigDecimal("20.00"))
                .marca(MarcaCreator.mockMarcaResponse())
                .categoria(CategoriaCreator.mockCategoriaResponse())
                .build();
    }

    public static ProdutoResponse mockProdutoResponseUpdated(){
        return ProdutoResponse.builder()
                .quantidade(20)
                .descricaoCurta("Celular")
                .descricaoDetalhada("Celular Teste")
                .valorCusto(new BigDecimal("20.00"))
                .valorVenda(new BigDecimal("40.00"))
                .marca(MarcaCreator.mockMarcaResponse())
                .categoria(CategoriaCreator.mockCategoriaResponse())
                .build();
    }

    public static ProdutoRequest mockProdutoRequestToSave(){
        return ProdutoRequest.builder()
                .quantidade(10)
                .descricaoCurta("Computador")
                .descricaoDetalhada("Computador Teste")
                .valorCusto(new BigDecimal("10.00"))
                .valorVenda(new BigDecimal("20.00"))
                .marca(new MarcaIdRequest(1L))
                .categoria(new CategoriaIdRequest(1L))
                .build();
    }

    public static ProdutoRequest mockProdutoRequestToUpdate(){
        return ProdutoRequest.builder()
                .quantidade(20)
                .descricaoCurta("Celular")
                .descricaoDetalhada("Celular Teste")
                .valorCusto(new BigDecimal("20.00"))
                .valorVenda(new BigDecimal("40.00"))
                .marca(new MarcaIdRequest(1L))
                .categoria(new CategoriaIdRequest(1L))
                .build();
    }

    public static ProdutoRequest mockInvalidProdutoRequest(){
        return ProdutoRequest.builder()
                .quantidade(null)
                .descricaoCurta(null)
                .descricaoDetalhada(null)
                .valorCusto(new BigDecimal("-20.00"))
                .valorVenda(new BigDecimal("-40.00"))
                .marca(null)
                .categoria(null)
                .build();
    }

    public static ProdutoImagemResponse mockProdutoImagemResponse() {
        return new ProdutoImagemResponse("imagem.png", OffsetDateTime.now(), OffsetDateTime.now());
    }
}
