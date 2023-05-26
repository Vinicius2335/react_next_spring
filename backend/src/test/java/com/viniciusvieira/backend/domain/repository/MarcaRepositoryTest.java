package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.Marca;
import com.viniciusvieira.backend.util.MarcaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Teste Unitário para a classe MarcaRepository")
class MarcaRepositoryTest {
    @Autowired
    private MarcaRepository marcaRepository;

    private final Marca expectedMarca = MarcaCreator.mockMarca();

    public Marca inserirNovaMarcaNoBanco(){
        return marcaRepository.saveAndFlush(MarcaCreator.mockMarca());
    }

    @Test
    @DisplayName("saveAndFlush Insert new marca when Successful")
    void saveAndFlush_InsertNewMarca_WhenSuccessful(){
        Marca marcaSaved = marcaRepository.saveAndFlush(MarcaCreator.mockMarca());

        // Não pode comparar a classe inteira pq a data criaçao/alteração será diferente
        assertAll(
                () -> assertNotNull(marcaSaved),
                () -> assertEquals(expectedMarca.getId(), marcaSaved.getId()),
                () -> assertEquals(expectedMarca.getNome(), marcaSaved.getNome())
        );
    }

    @Test
    @DisplayName("findAll Return list of marca When successful")
    void findAll_ReturnListMarca_WhenSuccessful(){
        Marca novaMarcaInserida = inserirNovaMarcaNoBanco();
        List<Marca> marcas = marcaRepository.findAll();

        assertAll(
                () -> assertNotNull(marcas),
                () -> assertFalse(marcas.isEmpty()),
                () -> assertEquals(1, marcas.size()),
                () -> assertTrue(marcas.contains(novaMarcaInserida))
        );
    }

    @Test
    @DisplayName("findById Return a marca When successful")
    void findById_ReturnMarca_WhenSuccessful(){
        Marca novaMarcaInserida = inserirNovaMarcaNoBanco();
        Marca marcaEncontrada = marcaRepository.findById(novaMarcaInserida.getId()).get();

        assertAll(
                () -> assertNotNull(marcaEncontrada),
                () -> assertEquals(novaMarcaInserida, marcaEncontrada)
        );
    }

    @Test
    @DisplayName("saveAndFlush Update existing marca when successful")
    void saveAndFlush_UpdateExistingMarca_WhenSuccessful(){
        Marca novaMarcaInserida = inserirNovaMarcaNoBanco();
        Marca marcaParaAtualizar = MarcaCreator.mockMarcaToUpdate(novaMarcaInserida.getDataCriacao());

        Marca marcaAtualizada = marcaRepository.saveAndFlush(marcaParaAtualizar);
        Marca marcaEncontrada = marcaRepository.findById(marcaAtualizada.getId()).get();

        assertAll(
                () -> assertNotNull(marcaAtualizada),
                () -> assertEquals(marcaEncontrada, marcaAtualizada)
        );
    }

    @Test
    @DisplayName("delete Remove marca When successful")
    void delete_RemoveMarca_WhenSuccessful(){
        Marca novaMarcaInserida = inserirNovaMarcaNoBanco();
        marcaRepository.delete(novaMarcaInserida);
        List<Marca> marcas = marcaRepository.findAll();

        assertTrue(marcas.isEmpty());
    }
}