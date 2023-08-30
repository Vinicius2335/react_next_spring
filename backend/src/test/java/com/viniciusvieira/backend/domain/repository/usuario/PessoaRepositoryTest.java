package com.viniciusvieira.backend.domain.repository.usuario;

import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PessoaRepositoryTest {
    @Autowired
    private PessoaRepository underTest;

    private final Pessoa pessoa = PessoaCreator.createPessoa();

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @DisplayName("saveAndFlush() save pessoa")
    void givenPessoa_whenSaveAndFlush_thenPessoaShouldBeInserted(){
        // given
        // when
        Pessoa expected = underTest.saveAndFlush(pessoa);
        // then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoa);
    }

    @Test
    @DisplayName("findById() return optional pessoa")
    void givenId_whenFindById_thenPessoaShouldBeFound(){
        // given
        Pessoa pessoaInserted = getPessoaInserted();
        // when
        Pessoa expected = underTest.findById(pessoaInserted.getId()).orElse(null);
        // then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoa);
    }

    @Test
    @DisplayName("findById() return empty optional when not found")
    void givenUnregisteredId_whenFindById_thenPessoaShouldBeNotFound(){
        // given
        // when
        boolean expected = underTest.findById(pessoa.getId()).isPresent();
        // then
        assertThat(expected).isFalse();
    }

    @Test
    @DisplayName("delete() remove pessoa")
    void givenPessoa_whenDelete_thenPessoaShouldBeRemoved(){
        // given
        Pessoa pessoaInserted = getPessoaInserted();
        // when
        underTest.delete(pessoaInserted);
        // then
        boolean expected = underTest.findById(pessoaInserted.getId()).isPresent();
        assertThat(expected).isFalse();
    }

    @Test
    @DisplayName("findByCpf() return optional pessoa")
    void givenCpf_whenFindByCpf_thenPessoaShouldBeFound() {
        // given
        Pessoa saveInserted = getPessoaInserted();
        // when
        Pessoa expected = underTest.findByCpf(saveInserted.getCpf()).orElse(null);
        System.out.println(expected);
        // then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                    .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                    .isEqualTo(pessoa);
    }

    @Test
    @DisplayName("findById() return empty optional when not found")
    void givenUnregisteredCpf_whenFindByCpf_thenPessoaShouldBeNotFound() {
        // given
        // when
        boolean expected = underTest.findByCpf(pessoa.getCpf()).isPresent();
        // then
        assertThat(expected).isFalse();
    }


    @Test
    @DisplayName("findByEmail() return optiona pessoa")
    void givenEmail_whenFindByEmail_thenPessoaShouldBeFound() {
        // given
        Pessoa pessoaInserted = getPessoaInserted();
        // when
        Pessoa expected = underTest.findByEmail(pessoaInserted.getEmail()).orElse(null);
        //then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                    .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                    .isEqualTo(pessoa);
    }

    @Test
    @DisplayName("findByEmail() return empty optional")
    void givenUnregisteredEmail_whenFindByEmail_thenPessoaShouldBeNotFound() {
        // given
        // when
        boolean expected = underTest.findByEmail(pessoa.getEmail()).isPresent();
        //then
        assertThat(expected).isFalse();
    }

    @Test
    @DisplayName("FindByEmailAndCodigoRecuperacaoSenha() return optiona pessoa")
    void givenEmailAndCodigoRecuperacaoSenha_whenFindByEmailAndCodigoRecuperacaoSenha_thenPessoaShouldBeFound() {
        // given
        Pessoa pessoaInserted = getPessoaInserted();
        // when
        Pessoa expected = underTest.findByEmailAndCodigoRecuperacaoSenha(pessoaInserted.getEmail(), pessoaInserted.getCodigoRecuperacaoSenha()).orElse(null);
        // then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoa);
    }

    @Test
    @DisplayName("FindByEmailAndCodigoRecuperacaoSenha() return empty optional")
    void givenUnregisteredEmailAndCodigoRecuperacaoSenha_whenFindByEmailAndCodigoRecuperacaoSenha_thenPessoaShouldBeNotFound() {
        // given
        // when
        boolean expected = underTest.findByEmailAndCodigoRecuperacaoSenha(pessoa.getEmail(), pessoa.getCodigoRecuperacaoSenha()).isPresent();
        // then
        assertThat(expected).isFalse();
    }

    private Pessoa getPessoaInserted() {
        return underTest.saveAndFlush(pessoa);
    }

}