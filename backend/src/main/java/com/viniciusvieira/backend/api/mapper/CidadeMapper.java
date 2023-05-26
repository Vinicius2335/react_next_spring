package com.viniciusvieira.backend.api.mapper;

import com.viniciusvieira.backend.api.representation.model.request.CidadeRequest;
import com.viniciusvieira.backend.api.representation.model.response.CidadeResponse;
import com.viniciusvieira.backend.domain.model.Cidade;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CidadeMapper {
    private final ModelMapper modelMapper;

    public Cidade toDomainCidade(CidadeRequest cidadeRequest){
        return modelMapper.map(cidadeRequest, Cidade.class);
    }

    public CidadeResponse toCidadeResponse(Cidade cidade){
        return modelMapper.map(cidade, CidadeResponse.class);
    }
}
