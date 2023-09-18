package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.mapper.usuario.ClienteMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.usuario.CpfAlreadyExistsException;
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
        verifyIfCpfAlreadyExists(clienteRequest.getCpf());

        Pessoa cliente = clienteMapper.toDomainPessoa(clienteRequest);
        Permissao permissaoEncontrada = crudPermissaoService.buscarPeloNome("CLIENTE");
        cliente.adicionarPermissao(permissaoEncontrada);
        Pessoa clienteSalvo = pessoaRepository.saveAndFlush(cliente);

        sendEmail(clienteSalvo);

        return clienteMapper.toPessoaResponse(clienteSalvo);
    }

    private void verifyIfCpfAlreadyExists(String cpf){
        boolean isCpfEmUso = pessoaRepository.findByCpf(cpf).isPresent();

        if (isCpfEmUso){
            throw new CpfAlreadyExistsException("Já existe uma pessoa cadastrada com esse CPF");
        }
    }

    private void sendEmail(Pessoa clienteSalvo) {
        Map<String, Object> propriedades = new HashMap<>();
        propriedades.put("nome", clienteSalvo.getNome());
        // COMMENT - Lembrando que após o registro o usuário deve solicitar um código para criar/alterar senha

        emailService.sendEmailTemplateBoasVindas(
                clienteSalvo.getEmail(),
                "Registro Concluído com Sucesso",
                propriedades
        );
    }

    // TODO - fazer o alterar

}

/*
        emailService.sendEmailSimples(clienteSalvo.getEmail(), "Cadastro na Loja Tabajara",
        "O registro na loja foi realizado com sucesso. Em breve você receberá a senha de acesso por e-mail")
 */