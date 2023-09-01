package com.viniciusvieira.backend.util;

import com.github.javafaker.Faker;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Locale;


public abstract class PessoaCreator {
    private static final Faker FAKER = new Faker(new Locale("pt_BR"));

    public static Pessoa createPessoa(){
        return Pessoa.builder()
                .id(1L)
                .permissoes(new ArrayList<>())
                .cpf("302.218.730-07")
                .email(FAKER.internet().emailAddress())
                .nome(FAKER.name().fullName())
                .endereco(EnderecoCreator.createEndereco())
                .senha(FAKER.internet().password())
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();

    }

    public static PessoaRequest createPessoaRequest(){
        return PessoaRequest.builder()
                .nome(FAKER.name().fullName())
                .senha(FAKER.internet().password())
                .cpf("302.218.730-07")
                .nomePermissao("CLIENTE")
                .email(FAKER.internet().emailAddress())
                .endereco(EnderecoCreator.createEnderecoRequest())
                .build();
    }

    public static PessoaRequest createInvalidPessoaRequest(){
        return PessoaRequest.builder()
                .nome(null)
                .senha(null)
                .cpf(null)
                .nomePermissao(null)
                .email(null)
                .endereco(null)
                .build();
    }

    public static PessoaResponse createPessoaResponse(Pessoa pessoa){
        return PessoaResponse.builder()
                .endereco(EnderecoCreator.createEnderecoResponse(pessoa))
                .nome(pessoa.getNome())
                .cpf(pessoa.getCpf())
                .email(pessoa.getEmail())
                .dataCriacao(pessoa.getDataCriacao())
                .dataAtualizacao(pessoa.getDataAtualizacao())
                .senha(pessoa.getSenha())
                .build();
    }

    public static PessoaGerenciamentoRequest createPessoaGerenciamentoRequest(Pessoa pessoa){
        return PessoaGerenciamentoRequest.builder()
                .codigoParaRecuperarSenha(pessoa.getCodigoRecuperacaoSenha())
                .email(pessoa.getEmail())
                .senha("TESTE")
                .build();
    }
}
