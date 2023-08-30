package com.viniciusvieira.backend.domain.repository.venda;

import com.viniciusvieira.backend.domain.model.venda.Marca;
import com.viniciusvieira.backend.util.MarcaCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class MarcaRepositoryTest {
    @Autowired
    private MarcaRepository underTest;
    
    private final Marca marca = MarcaCreator.createMarca();

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @DisplayName("saveAndFlush() insert new marca")
    void givenMarca_whenSaveAndFlush_thenMarcaShouldBeInserted(){
        // given
        // when
        Marca expected = underTest.saveAndFlush(marca);
        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getNome()).isEqualTo(marca.getNome());

    }

    @Test
    @DisplayName("findAll() return list marca")
    void whenFindAll_TheReturnListMarca(){
        // given
        Marca marcaInserted = getMarcaInserted();
        // when
        List<Marca> expected = underTest.findAll();
        // then
        assertThat(expected)
                .hasSize(1)
                .contains(marcaInserted);
    }

    @Test
    @DisplayName("findById() return marca")
    void givenId_whenFindById_thenMarcaShouldBeFound(){
        // given
        Marca marcaInserted = getMarcaInserted();
        // when
        Marca expected = underTest.findById(marcaInserted.getId()).orElse(null);
        // then
        assertThat(expected)
                .isNotNull()
                .isEqualTo(marcaInserted);
    }

    @Test
    @DisplayName("findById() return empty optional marca when not found")
    void givenUnregisteredId_whenFindById_thenReturnEmptyOptional(){
        // given
        // when
        Optional<Marca> expected = underTest.findById(1L);
        // then
        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("findByNome() return marca")
    void givenNomeMarca_whenFindByNome_thenMarcaShouldBeFound(){
        // given
        getMarcaInserted();
        String nome = marca.getNome();
        // when
        Marca expected = underTest.findByNome(nome).orElse(null);
        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("findByNome() return optional empty when not found")
    void givenUnregisteredNomeMarca_whenFindByNome_thenReturnEmptyOptional(){
        // given
        String nome = marca.getNome();
        // when
        Optional<Marca> expected = underTest.findByNome(nome);
        // then
        assertThat(expected).isEmpty();
    }

    @Test
    @DisplayName("delete() remove marca")
    void givenId_whenDelete_thenMarcaShouldBeRemoved(){
        // given
        Marca marcaInserted = getMarcaInserted();
        // when
        underTest.delete(marcaInserted);
        // then
        boolean expected = underTest.findById(marcaInserted.getId()).isPresent();
        assertThat(expected).isFalse();
    }

    private Marca getMarcaInserted(){
        return underTest.saveAndFlush(marca);
    }

}