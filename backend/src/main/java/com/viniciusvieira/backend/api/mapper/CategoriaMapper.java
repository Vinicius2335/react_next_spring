package com.viniciusvieira.backend.api.mapper;

import com.viniciusvieira.backend.api.representation.model.request.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.CategoriaResponse;
import com.viniciusvieira.backend.domain.model.Categoria;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoriaMapper {
    private final ModelMapper modelMapper;

    public Categoria toDomainCategoria(CategoriaRequest categoriaRequest){
        return modelMapper.map(categoriaRequest, Categoria.class);
    }

    public CategoriaResponse toCategoriaResponse(Categoria categoria){
        return modelMapper.map(categoria, CategoriaResponse.class);
    }
}
