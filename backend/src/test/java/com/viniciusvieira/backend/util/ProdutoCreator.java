package com.viniciusvieira.backend.util;

import com.github.javafaker.Faker;
import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.request.venda.ids.CategoriaId;
import com.viniciusvieira.backend.api.representation.model.request.venda.ids.MarcaId;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.model.venda.Produto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Locale;

public abstract class ProdutoCreator {
    public static Produto createProduto(){
        Faker faker = createFaker();
        return Produto.builder()
                .marca(MarcaCreator.createMarca())
                .categoria(CategoriaCreator.createCategoria())
                .id(1L)
                .quantidade(faker.number().randomDigitNotZero())
                .descricaoCurta(faker.commerce().productName())
                .descricaoDetalhada(faker.lorem().paragraph())
                .valorVenda(new BigDecimal(priceConverter(faker.commerce().price(20, 99))))
                .valorCusto(new BigDecimal(priceConverter(faker.commerce().price(1, 10))))
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }

    public static Produto createOtherProduto(){
        Produto produto = createProduto();
        produto.setId(2L);
        return produto;
    }

    public static Produto createOtherProduto2(){
        Produto produto = createProduto();
        produto.setId(3L);
        produto.setMarca(MarcaCreator.createOtherMarca());
        produto.setCategoria(CategoriaCreator.createOtherCategoria());
        return produto;
    }

    public static ProdutoResponse createProdutoResponse(Produto produto) {
        return ProdutoResponse.builder()
                .categoria(CategoriaCreator.createCategoriaResponse())
                .marca(MarcaCreator.createMarcaResponse())
                .quantidade(produto.getQuantidade())
                .valorCusto(produto.getValorCusto())
                .valorVenda(produto.getValorVenda())
                .descricaoCurta(produto.getDescricaoCurta())
                .descricaoDetalhada(produto.getDescricaoDetalhada())
                .dataAtualizacao(OffsetDateTime.now())
                .dataCriacao(OffsetDateTime.now())
                .build();
    }

    public static ProdutoRequest createProdutoRequest(Produto produto) {
        return ProdutoRequest.builder()
                .categoria(new CategoriaId(produto.getCategoria().getId()))
                .marca(new MarcaId(produto.getMarca().getId()))
                .descricaoCurta(produto.getDescricaoCurta())
                .descricaoDetalhada(produto.getDescricaoDetalhada())
                .valorCusto(produto.getValorCusto())
                .valorVenda(produto.getValorVenda())
                .quantidade(produto.getQuantidade())
                .build();
    }

    public static ProdutoRequest createInvalidProdutoRequest() {
        return ProdutoRequest.builder()
                .categoria(new CategoriaId(null))
                .marca(null)
                .descricaoCurta(null)
                .descricaoDetalhada(null)
                .valorCusto(null)
                .valorVenda(null)
                .quantidade(null)
                .build();
    }

    private static Faker createFaker(){
        return new Faker(new Locale("pt_BR"));
    }

    private static String priceConverter(String price) {
        return price.replace(",", ".");
    }
}
