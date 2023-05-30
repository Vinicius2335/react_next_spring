package com.viniciusvieira.backend.api.mapper;

import com.viniciusvieira.backend.api.representation.model.request.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.PessoaResponse;
import com.viniciusvieira.backend.domain.model.Pessoa;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PessoaMapper {
    private final ModelMapper modelMapper;

    public Pessoa toDomainPessoa(PessoaRequest pessoaRequest){
        return modelMapper.map(pessoaRequest, Pessoa.class);
    }

    public PessoaResponse toPessoaResponse(Pessoa pessoa){
        return modelMapper.map(pessoa, PessoaResponse.class);
    }
}
