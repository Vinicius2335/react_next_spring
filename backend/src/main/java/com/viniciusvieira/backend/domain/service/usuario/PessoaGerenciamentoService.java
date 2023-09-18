package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PessoaGerenciamentoService {
    private final CrudPessoaService crudPessoaService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void solicitarCodigo(String email){
        Pessoa pessoa = crudPessoaService.buscarPeloEmail(email);

        pessoa.setCodigoRecuperacaoSenha(gerarCodigoParaRecuperarSenha());
        pessoa.setDataEnvioCodigo(LocalDateTime.now());
        crudPessoaService.alterarParaGerenciamento(pessoa);

        Map<String, Object> propriedades = new HashMap<>();
        propriedades.put("nome", pessoa.getNome());
        propriedades.put("codigo", pessoa.getCodigoRecuperacaoSenha());

        emailService.sendEmailTemplateRecuperacaoCodigo(
                pessoa.getEmail(),
                "Código de Segurança - Loja Sakai",
                propriedades
        );

    }

    public void alterarSenha(PessoaGerenciamentoRequest pessoaGerenciamentoRequest){
        Pessoa pessoaEncontrada = crudPessoaService.buscarPeloEmailECodigo(pessoaGerenciamentoRequest.getEmail(),
                pessoaGerenciamentoRequest.getCodigoParaRecuperarSenha());

        long diferenca = ChronoUnit.MINUTES.between(pessoaEncontrada.getDataEnvioCodigo(), LocalDateTime.now());

        // validade do código de recuperação é de 15min
        if (diferenca >= 0 && diferenca <= 15){
            pessoaEncontrada.setSenha(passwordEncoder.encode(pessoaGerenciamentoRequest.getSenha()));
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
