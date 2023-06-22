package com.viniciusvieira.backend.api.mapper;

import com.viniciusvieira.backend.api.representation.model.request.CarrinhoDeCompraProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.CarrinhoDeCompraProdutoResponse;
import com.viniciusvieira.backend.domain.model.CarrinhoDeCompraProduto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CarrinhoDeCompraProdutoMapper {
    private final ModelMapper modelMapper;

    public CarrinhoDeCompraProduto toDomainCarrinhoDeCompraProduto(CarrinhoDeCompraProdutoRequest carrinhoDeCompraProdutoRequest){
        return modelMapper.map(carrinhoDeCompraProdutoRequest, CarrinhoDeCompraProduto.class);
    }

    public CarrinhoDeCompraProdutoResponse toCarrinhoDeCompraProdutoResponse(CarrinhoDeCompraProduto carrinhoDeCompraProduto){
        return modelMapper.map(carrinhoDeCompraProduto, CarrinhoDeCompraProdutoResponse.class);
    }
}
