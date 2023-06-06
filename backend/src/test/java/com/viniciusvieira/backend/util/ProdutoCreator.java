package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.CategoriaIdRequest;
import com.viniciusvieira.backend.api.representation.model.request.MarcaIdRequest;
import com.viniciusvieira.backend.api.representation.model.request.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoResponse;
import com.viniciusvieira.backend.domain.model.Produto;

import java.math.BigDecimal;

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
}