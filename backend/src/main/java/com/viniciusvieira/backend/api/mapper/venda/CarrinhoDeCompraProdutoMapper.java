package com.viniciusvieira.backend.api.mapper.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraProdutoResponse;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompraProduto;
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
