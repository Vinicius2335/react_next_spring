package com.viniciusvieira.backend.api.mapper;

import com.viniciusvieira.backend.api.representation.model.request.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.MarcaResponse;
import com.viniciusvieira.backend.domain.model.Marca;
import lombok.AllArgsConstructor;
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
