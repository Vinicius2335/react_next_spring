package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class PessoaGerenciamentoService {
    private final CrudPessoaService crudPessoaService;
    private final EmailService emailService;

    public void solicitarCodigo(String email){
        Pessoa pessoa = crudPessoaService.buscarPeloEmail(email);

        pessoa.setCodigoRecuperacaoSenha(gerarCodigoParaRecuperarSenha());
        pessoa.setDataEnvioCodigo(LocalDateTime.now());
        crudPessoaService.alterarParaGerenciamento(pessoa);

        emailService.sendEmailSimples(pessoa.getEmail(),
                "Código de recuperação de senha",
                "Olá, o seu código para recuperação de senha é o seguinte '" + pessoa.getCodigoRecuperacaoSenha() + "'");
    }

    public void alterarSenha(PessoaGerenciamentoRequest pessoaGerenciamentoRequest){
        Pessoa pessoaEncontrada = crudPessoaService.buscarPeloEmailECodigo(pessoaGerenciamentoRequest.getEmail(),
                pessoaGerenciamentoRequest.getCodigoParaRecuperarSenha());

        long diferenca = ChronoUnit.MINUTES.between(pessoaEncontrada.getDataEnvioCodigo(), LocalDateTime.now());

        // validade do código de recuperação é de 15min
        if (diferenca >= 0 && diferenca <= 15){
            // TODO - depois de adicionar o spring security é necessário criptografar a senha
            pessoaEncontrada.setSenha(pessoaGerenciamentoRequest.getSenha());
            pessoaEncontrada.setCodigoRecuperacaoSenha(null);
            pessoaEncontrada.setDataEnvioCodigo(null);
            crudPessoaService.alterarParaGerenciamento(pessoaEncontrada);
        } else {
            throw new NegocioException("Tempo expirado, solicite um novo código");
        }
    }

    private String gerarCodigoParaRecuperarSenha(){
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
