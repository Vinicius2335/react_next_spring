package com.viniciusvieira.backend.api.mapper.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.MarcaResponse;
import com.viniciusvieira.backend.domain.model.venda.Marca;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MarcaMapper {
    private final ModelMapper modelMapper;

    public Marca toDomainMarca(MarcaRequest marcaRequest) {
        return modelMapper.map(marcaRequest, Marca.class);
    }

    public MarcaResponse toMarcaResponse(Marca marca){
        return modelMapper.map(marca, MarcaResponse.class);
    }
}
