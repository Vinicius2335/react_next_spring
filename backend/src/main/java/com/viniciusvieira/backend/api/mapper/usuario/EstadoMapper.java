package com.viniciusvieira.backend.api.mapper.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.EstadoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.EstadoResponse;
import com.viniciusvieira.backend.domain.model.usuario.Estado;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EstadoMapper {
    private final ModelMapper modelMapper;

    public Estado toDomainEstado(EstadoRequest estadoRequest) {
        return modelMapper.map(estadoRequest, Estado.class);
    }

    public EstadoResponse toEstadoResponse(Estado estado){
        return modelMapper.map(estado, EstadoResponse.class);
    }
}
