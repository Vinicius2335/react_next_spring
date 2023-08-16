package com.viniciusvieira.backend.api.mapper.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CategoriaResponse;
import com.viniciusvieira.backend.domain.model.venda.Categoria;
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
