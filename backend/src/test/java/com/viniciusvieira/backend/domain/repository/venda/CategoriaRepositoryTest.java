package com.viniciusvieira.backend.domain.repository.venda;

import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.util.CategoriaCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository underTest;

    private final Categoria categoria = CategoriaCreator.createCategoria();

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @DisplayName("saveAndFlush() insert new categoria")
    void givenCategoria_whenSaveAndFlush_thenCategoriaShouldBeInserted(){
        // given
        // when
        Categoria expected = underTest.saveAndFlush(categoria);
        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getNome()).isEqualTo(categoria.getNome());

    }

    @Test
    @DisplayName("findAll() return list categoria")
    void whenFindAll_TheReturnListCategoria(){
        // given
        Categoria categoriaInserted = getCategoriaInserted();
        // when
        List<Categoria> expected = underTest.findAll();
        // then
        assertThat(expected)
                .hasSize(1)
                .contains(categoriaInserted);
    }

    @Test
    @DisplayName("findById() return categoria")
    void givenId_whenFindById_thenCategoriaShouldBeFound(){
        // given
        Categoria categoriaInserted = getCategoriaInserted();
        // when
        Categoria expected = underTest.findById(categoriaInserted.getId()).orElse(null);
        // then
        assertThat(expected)
                .isNotNull()
                .isEqualTo(categoriaInserted);
    }

    @Test
    @DisplayName("findById() return empty optional categoria when not found")
    void givenUnregisteredId_whenFindById_thenReturnEmptyOptional(){
        // given
        // when
        Optional<Categoria> expected = underTest.findById(1L);
        // then
        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("findByNome() return categoria")
    void givenNomeCategoria_whenFindByNome_thenCategoriaShouldBeFound(){
        // given
        getCategoriaInserted();
        String nome = categoria.getNome();
        // when
        Categoria expected = underTest.findByNome(nome).orElse(null);
        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("findByNome() return optional empty when not found")
    void givenUnregisteredNomeCategoria_whenFindByNome_thenReturnEmptyOptional(){
        // given
        String nome = categoria.getNome();
        // when
        Optional<Categoria> expected = underTest.findByNome(nome);
        // then
        assertThat(expected).isEmpty();
    }

    @Test
    @DisplayName("delete() remove categoria")
    void givenId_whenDelete_thenCategoriaShouldBeRemoved(){
        // given
        Categoria categoriaInserted = getCategoriaInserted();
        // when
        underTest.delete(categoriaInserted);
        // then
        boolean expected = underTest.findById(categoriaInserted.getId()).isPresent();
        assertThat(expected).isFalse();
    }

    private Categoria getCategoriaInserted(){
        return underTest.saveAndFlush(categoria);
    }

}