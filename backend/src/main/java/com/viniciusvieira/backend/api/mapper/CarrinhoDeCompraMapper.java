package com.viniciusvieira.backend.api.mapper;

import com.viniciusvieira.backend.api.representation.model.request.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.model.CarrinhoDeCompra;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CarrinhoDeCompraMapper {
    private final ModelMapper modelMapper;

    public CarrinhoDeCompra toDomainCarrinhoDeCompra(CarrinhoDeCompraRequest carrinhoDeCompraRequest){
        return modelMapper.map(carrinhoDeCompraRequest, CarrinhoDeCompra.class);
    }

    public CarrinhoDeCompraResponse toCarrinhoDeCompraResponse(CarrinhoDeCompra carrinhoDeCompra){
        return modelMapper.map(carrinhoDeCompra, CarrinhoDeCompraResponse.class);
    }
}
