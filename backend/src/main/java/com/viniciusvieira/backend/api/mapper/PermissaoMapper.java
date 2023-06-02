package com.viniciusvieira.backend.api.mapper;

import com.viniciusvieira.backend.api.representation.model.request.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.PermissaoResponse;
import com.viniciusvieira.backend.domain.model.Permissao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PermissaoMapper {
    private final ModelMapper modelMapper;

    public Permissao toDomainPermissao(PermissaoRequest permissaoRequest) {
        return modelMapper.map(permissaoRequest, Permissao.class);
    }

    public PermissaoResponse toPermissaoResponse(Permissao permissao){
        return modelMapper.map(permissao, PermissaoResponse.class);
    }
}
