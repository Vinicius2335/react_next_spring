package com.viniciusvieira.backend.domain.repository.usuario;

import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.util.PermissaoCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class PermissaoRepositoryTest {
    @Autowired
    private PermissaoRepository underTest;

    private final Permissao permissao = PermissaoCreator.createPermissao();

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @DisplayName("saveAndFlush() insert new permissao")
    void givenPermissao_whenSaveAndFlush_thenPermissaoShouldBeInserted(){
        // given
        // when
        Permissao expected = underTest.saveAndFlush(permissao);
        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getNome()).isEqualTo(permissao.getNome());

    }

    @Test
    @DisplayName("findByNome() return permissao")
    void givenNomePermissao_whenFindByNome_thenPermissaoShouldBeFound(){
        // given
        getPermissaoInserted();
        String nome = permissao.getNome();
        // when
        Permissao expected = underTest.findByNome(nome).orElse(null);
        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("findByNome() return optional empty when not found")
    void givenUnregisteredNomePermissao_whenFindByNome_thenReturnEmptyOptional(){
        // given
        String nome = permissao.getNome();
        // when
        Optional<Permissao> expected = underTest.findByNome(nome);
        // then
        assertThat(expected).isEmpty();
    }



    private Permissao getPermissaoInserted(){
        return underTest.saveAndFlush(permissao);
    }

    @Test
    @DisplayName("findAll() return list permissao")
    void whenFindAll_TheReturnListPermissao(){
        // given
        Permissao permissaoInserted = getPermissaoInserted();
        // when
        List<Permissao> expected = underTest.findAll();
        // then
        assertThat(expected)
                .hasSize(1)
                .contains(permissaoInserted);
    }

    @Test
    @DisplayName("findById() return permissao")
    void givenId_whenFindById_thenPermissaoShouldBeFound(){
        // given
        Permissao permissaoInserted = getPermissaoInserted();
        // when
        Permissao expected = underTest.findById(permissaoInserted.getId()).orElse(null);
        // then
        assertThat(expected)
                .isNotNull()
                .isEqualTo(permissaoInserted);
    }

    @Test
    @DisplayName("findById() return empty optional permissao when not found")
    void givenUnregisteredId_whenFindById_thenReturnEmptyOptional(){
        // given
        // when
        Optional<Permissao> expected = underTest.findById(1L);
        // then
        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("delete() remove permissao")
    void givenId_whenDelete_thenPermissaoShouldBeRemoved(){
        // given
        Permissao permissaoInserted = getPermissaoInserted();
        // when
        underTest.delete(permissaoInserted);
        // then
        boolean expected = underTest.findById(permissaoInserted.getId()).isPresent();
        assertThat(expected).isFalse();
    }

}