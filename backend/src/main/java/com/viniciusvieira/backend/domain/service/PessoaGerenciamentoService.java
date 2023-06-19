package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.representation.model.request.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.model.Pessoa;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class PessoaGerenciamentoService {
    private final CrudPessoaService crudPessoaService;
    private final EmailService emailService;

    public void solicitarCodigo(String email){
        Pessoa pessoa = crudPessoaService.buscarPeloEmail(email);
        pessoa.setCodigoRecuperacaoSenha(gerarCodigoParaRecuperarSenha());
        pessoa.setDataEnvioCodigo(new Date());
        crudPessoaService.alterarParaGerenciamento(pessoa);

        emailService.sendEmailSimples(pessoa.getEmail(),
                "Código de recuperação de senha",
                "Olá, o seu código para recuperação de senha é o seguinte '" + pessoa.getCodigoRecuperacaoSenha() + "'");
    }

    public void alterarSenha(PessoaGerenciamentoRequest pessoaGerenciamentoRequest){
        Pessoa pessoaEncontrada = crudPessoaService.buscarPeloEmailECodigo(pessoaGerenciamentoRequest.getEmail(),
                pessoaGerenciamentoRequest.getCodigoParaRecuperarSenha());

        // resultado em milesegindos por isso tem que dividir por 1000 depois
        Date diferenca = new Date(new Date().getTime() - pessoaEncontrada.getDataEnvioCodigo().getTime());

        // validade do codigo de recuperação é de 15min
        // 15min = 900 segundos
        if (diferenca.getTime()/1000 < 900){
            // TODO - depois de adicionar o spring security é necessário criptografar a senha
            pessoaEncontrada.setSenha(pessoaGerenciamentoRequest.getSenha());
            pessoaEncontrada.setCodigoRecuperacaoSenha(null);
            crudPessoaService.alterarParaGerenciamento(pessoaEncontrada);
        } else {
            throw new NegocioException("Tempo expirado, solicite um novo código");
        }
    }

    private String gerarCodigoParaRecuperarSenha(){
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
