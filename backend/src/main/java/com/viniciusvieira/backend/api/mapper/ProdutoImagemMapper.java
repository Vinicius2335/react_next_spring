package com.viniciusvieira.backend.api.mapper;

import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProdutoImagemMapper {
    private final ModelMapper modelMapper;

    public ProdutoImagemResponse toProdutoImagemResponse(ProdutoImagem produtoImagem){
        return modelMapper.map(produtoImagem, ProdutoImagemResponse.class);
    }
}
