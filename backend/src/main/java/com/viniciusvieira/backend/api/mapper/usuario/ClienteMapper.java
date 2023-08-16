package com.viniciusvieira.backend.api.mapper.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ClienteMapper {
    private final ModelMapper modelMapper;

    public Pessoa toDomainPessoa(ClienteRequest clienteRequestessoaRequest){
        return modelMapper.map(clienteRequestessoaRequest, Pessoa.class);
    }

    public PessoaResponse toPessoaResponse(Pessoa pessoa){
        return modelMapper.map(pessoa, PessoaResponse.class);
    }
}
