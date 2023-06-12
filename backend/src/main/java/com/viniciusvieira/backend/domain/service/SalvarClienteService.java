package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.ClienteMapper;
import com.viniciusvieira.backend.api.representation.model.request.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.PessoaResponse;
import com.viniciusvieira.backend.domain.model.Permissao;
import com.viniciusvieira.backend.domain.model.Pessoa;
import com.viniciusvieira.backend.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SalvarClienteService {
    private final PessoaRepository pessoaRepository;
    private final ClienteMapper clienteMapper;
    private final CrudPermissaoService crudPermissaoService;

    public PessoaResponse inserirCliente(ClienteRequest clienteRequest){
        Pessoa cliente = clienteMapper.toDomainPessoa(clienteRequest);

        Permissao permissaoEncontrada = crudPermissaoService.buscarPeloNome("CLIENTE");
        cliente.adicionarPermissao(permissaoEncontrada);
        // TODO - por enquanto a senha está vazia

        Pessoa clienteSalvo = pessoaRepository.saveAndFlush(cliente);
        return clienteMapper.toPessoaResponse(clienteSalvo);
    }

    // TODO - fazer o alterar

}
