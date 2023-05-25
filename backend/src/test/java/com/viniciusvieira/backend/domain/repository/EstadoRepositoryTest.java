package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.Estado;
import com.viniciusvieira.backend.util.EstadoCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Teste Unitário para a classe EstadoRepository")
class EstadoRepositoryTest {
    @Autowired
    private EstadoRepository estadoRepository;

    private final Estado expectedEstado = EstadoCreator.mockValidEstado();

    public Estado inserirNovaEstadoNoBanco(){
        return estadoRepository.saveAndFlush(EstadoCreator.mockValidEstado());
    }

    @Test
    @DisplayName("saveAndFlush Insert new estado when Successful")
    void saveAndFlush_InsertNewEstado_WhenSuccessful(){
        Estado estadoSaved = estadoRepository.saveAndFlush(EstadoCreator.mockValidEstado());

        // Não pode comparar a classe inteira pq a data criaçao/alteração será diferente
        assertAll(
                () -> assertNotNull(estadoSaved),
                () -> assertEquals(expectedEstado.getId(), estadoSaved.getId()),
                () -> assertEquals(expectedEstado.getNome(), estadoSaved.getNome()),
                () -> assertEquals(expectedEstado.getSigla(), estadoSaved.getSigla())
        );
    }

    @Test
    @DisplayName("findAll Return list of estado When successful")
    void findAll_ReturnListEstado_WhenSuccessful(){
        Estado novaEstadoInserida = inserirNovaEstadoNoBanco();
        List<Estado> estados = estadoRepository.findAll();

        assertAll(
                () -> assertNotNull(estados),
                () -> assertFalse(estados.isEmpty()),
                () -> assertEquals(1, estados.size()),
                () -> assertTrue(estados.contains(novaEstadoInserida))
        );
    }

    @Test
    @DisplayName("findById Return a estado When successful")
    void findById_ReturnEstado_WhenSuccessful(){
        Estado novaEstadoInserida = inserirNovaEstadoNoBanco();
        Estado estadoEncontrada = estadoRepository.findById(novaEstadoInserida.getId()).get();

        assertAll(
                () -> assertNotNull(estadoEncontrada),
                () -> assertEquals(novaEstadoInserida, estadoEncontrada)
        );
    }

    @Test
    @DisplayName("saveAndFlush Update existing estado when successful")
    void saveAndFlush_UpdateExistingEstado_WhenSuccessful(){
        Estado novaEstadoInserida = inserirNovaEstadoNoBanco();
        Estado estadoParaAtualizar = EstadoCreator.mockEstadoToUpdate(novaEstadoInserida.getDataCriacao());

        Estado estadoAtualizada = estadoRepository.saveAndFlush(estadoParaAtualizar);
        Estado estadoEncontrada = estadoRepository.findById(estadoAtualizada.getId()).get();

        assertAll(
                () -> assertNotNull(estadoAtualizada),
                () -> assertEquals(estadoEncontrada, estadoAtualizada)
        );
    }

    @Test
    @DisplayName("delete Remove estado When successful")
    void delete_RemoveEstado_WhenSuccessful(){
        Estado novaEstadoInserida = inserirNovaEstadoNoBanco();
        estadoRepository.delete(novaEstadoInserida);
        List<Estado> estados = estadoRepository.findAll();

        assertTrue(estados.isEmpty());
    }
}