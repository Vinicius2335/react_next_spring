package com.viniciusvieira.backend.domain.model.usuario;

import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PermissaoTest {
    private final Permissao underTest = PermissaoCreator.createPermissao();
    private final Pessoa pessoa = PessoaCreator.createPessoa();

    @Test
    @DisplayName("addNewPessoa() add pessoa to the list of pessoas")
    void givenPessoa_whenAddNewPessoa_thenPessoaShouldBeAddedToTheListOfPessoas() {
        // given
        // when
        underTest.addNewPessoa(pessoa);
        // then
        assertThat(underTest.getPessoas())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(pessoa);
    }

    @Test
    @DisplayName("removePessoa() remove pessoa to the list of pessoas")
    void givenPessoa_whenRemovePessoa_thenPessoaShouldBeDeleteToTheListOfPessoas() {
        // given
        underTest.addNewPessoa(pessoa);
        // when
        underTest.removePessoa(pessoa);
        // then
        assertThat(underTest.getPessoas()).isEmpty();
    }
}