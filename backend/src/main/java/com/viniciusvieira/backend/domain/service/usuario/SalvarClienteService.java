package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.mapper.usuario.ClienteMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SalvarClienteService {
    private final PessoaRepository pessoaRepository;
    private final ClienteMapper clienteMapper;
    private final CrudPermissaoService crudPermissaoService;
    private final EmailService emailService;

    public PessoaResponse inserirCliente(ClienteRequest clienteRequest){
        Pessoa cliente = clienteMapper.toDomainPessoa(clienteRequest);
        boolean isCpfEmUso = pessoaRepository.findByCpf(cliente.getCpf()).isPresent();

        // TODO - CpfAlreadyExistsException
        if (isCpfEmUso){
            throw new NegocioException("Já existe uma pessoa cadastrada com esse CPF");
        }

        Permissao permissaoEncontrada = crudPermissaoService.buscarPeloNome("CLIENTE");
        cliente.adicionarPermissao(permissaoEncontrada);
        Pessoa clienteSalvo = pessoaRepository.saveAndFlush(cliente);

        // enviando email
        // COMMENT -- Comentado para testes
        //Map<String, Object> propriedades = new HashMap<>();
        //propriedades.put("nome", clienteSalvo.getNome());
        //propriedades.put("mensagem", "O registro na loja foi realizado com sucesso. Em breve você receberá a senha de acesso por e-mail");
        //
        //emailService.sendEmailTemplate(
        //        clienteSalvo.getEmail(),
        //        "Cadastro na Loja Tabajara",
        //        propriedades
        //);

        return clienteMapper.toPessoaResponse(clienteSalvo);
    }

    // TODO - fazer o alterar

}

/*
//        emailService.sendEmailSimples(clienteSalvo.getEmail(), "Cadastro na Loja Tabajara",
//        "O registro na loja foi realizado com sucesso. Em breve você receberá a senha de acesso por e-mail")
 */