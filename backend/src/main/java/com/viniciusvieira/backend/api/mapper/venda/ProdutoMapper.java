package com.viniciusvieira.backend.api.mapper.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProdutoMapper {
    private final ModelMapper modelMapper;

    public Produto toDomainProduto(ProdutoRequest produtoRequest){
        return modelMapper.map(produtoRequest, Produto.class);
    }

    public ProdutoResponse toProdutoResponse(Produto produto){
        return modelMapper.map(produto, ProdutoResponse.class);
    }
}
