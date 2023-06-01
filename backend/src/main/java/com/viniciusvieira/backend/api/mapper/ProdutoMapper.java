package com.viniciusvieira.backend.api.mapper;

import com.viniciusvieira.backend.api.representation.model.request.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoResponse;
import com.viniciusvieira.backend.domain.model.Produto;
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
