package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class PessoaCreator {
    public static Pessoa mockPessoa(){
        return Pessoa.builder()
                .id(1L)
                .cep("01001-000")
                .cpf("302.218.730-07")
                .cidade(CidadeCreator.mockCidade())
                .nome("Teste 01")
                .senha("teste")
                .email("teste@gmail.com")
                .endereco("rua teste")
                .permissoes(new ArrayList<>())
                .build();
    }

    public static Pessoa mockPessoaComCodigo(){
        return Pessoa.builder()
                .id(1L)
                .cep("01001-000")
                .cpf("302.218.730-07")
                .cidade(CidadeCreator.mockCidade())
                .nome("Teste 01")
                .senha("teste")
                .email("teste@gmail.com")
                .endereco("rua teste")
                .codigoRecuperacaoSenha("Teste")
                .dataEnvioCodigo(LocalDateTime.now())
                .permissoes(new ArrayList<>())
                .build();
    }

    public static Pessoa mockPessoaToUpdate(OffsetDateTime dataCriacao) {
        return Pessoa.builder()
                .id(1L)
                .cep("01001-002")
                .cpf("791.419.531-69")
                .cidade(CidadeCreator.mockCidade())
                .nome("Teste Update")
                .senha("update")
                .email("update@gmail.com")
                .endereco("rua teste update")
                .dataCriacao(dataCriacao)
                .permissoes(new ArrayList<>())
                .build();
    }

    public static PessoaResponse mockPessoaResponse() {
        return PessoaResponse.builder()
                .cep("01001-000")
                .cpf("302.218.730-07")
                .nomeCidade("Cascavel")
                .nome("Teste 01")
                .senha("teste")
                .email("teste@gmail.com")
                .endereco("rua teste")
                .build();
    }

    public static PessoaResponse mockPessoaResponseUpdate() {
        return PessoaResponse.builder()
                .cep("01001-002")
                .cpf("791.419.531-69")
                .nomeCidade("Cascavel")
                .nome("Teste Update")
                .senha("update")
                .email("update@gmail.com")
                .endereco("rua teste update")
                .build();
    }

    public static PessoaRequest mockPessoaRequestToSave() {
        return PessoaRequest.builder()
                .cep("01001-000")
                .cpf("302.218.730-07")
                .nome("Teste 01")
                .senha("teste")
                .email("teste@gmail.com")
                .endereco("rua teste")
                .cidadeId(1L)
                .nomePermissao(PermissaoCreator.mockPermissao().getNome())
                .build();
    }

    public static PessoaGerenciamentoRequest mockPessoaGerenciamentoRequest(){
        return PessoaGerenciamentoRequest.builder()
                .email("teste@gmail.com")
                .senha("teste")
                .codigoParaRecuperarSenha("Teste")
                .build();
    }

    public static PessoaRequest mockPessoaRequestToUpdate() {
        return PessoaRequest.builder()
                .cep("01001-002")
                .cpf("791.419.531-69")
                .nome("Teste Update")
                .senha("update")
                .email("update@gmail.com")
                .endereco("rua teste update")
                .cidadeId(1L)
                .nomePermissao(PermissaoCreator.mockPermissao().getNome())
                .build();
    }

    public static PessoaRequest mockInvalidPessoaRequest() {
        return PessoaRequest.builder()
                .cep(null)
                .cpf(null)
                .nome(null)
                .senha(null)
                .email(null)
                .endereco(null)
                .cidadeId(0L)
                .nomePermissao(null)
                .build();
    }

}
