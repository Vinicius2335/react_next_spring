package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Teste Unitário para a interface CategoriaRepository")
class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final Categoria expectedCategoria = CategoriaCreator.mockCategoria();

    public Categoria inserirNovaCategoriaNoBanco(){
        return categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());
    }

    @Test
    @DisplayName("saveAndFlush Insert new categoria when Successful")
    void saveAndFlush_InsertNewCategoria_WhenSuccessful(){
        Categoria categoriaSaved = categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());

        // Não pode comparar a classe inteira pq a data criaçao/alteração será diferente
        assertAll(
                () -> assertNotNull(categoriaSaved),
                () -> assertEquals(expectedCategoria.getId(), categoriaSaved.getId()),
                () -> assertEquals(expectedCategoria.getNome(), categoriaSaved.getNome())
        );
    }

    @Test
    @DisplayName("findAll Return list of categoria When successful")
    void findAll_ReturnListCategoria_WhenSuccessful(){
        Categoria novaCategoriaInserida = inserirNovaCategoriaNoBanco();
        List<Categoria> categorias = categoriaRepository.findAll();

        assertAll(
                () -> assertNotNull(categorias),
                () -> assertFalse(categorias.isEmpty()),
                () -> assertEquals(1, categorias.size()),
                () -> assertTrue(categorias.contains(novaCategoriaInserida))
        );
    }

    @Test
    @DisplayName("findById Return a categoria When successful")
    void findById_ReturnCategoria_WhenSuccessful(){
        Categoria novaCategoriaInserida = inserirNovaCategoriaNoBanco();
        Categoria categoriaEncontrada = categoriaRepository.findById(novaCategoriaInserida.getId()).get();

        assertAll(
                () -> assertNotNull(categoriaEncontrada),
                () -> assertEquals(novaCategoriaInserida, categoriaEncontrada)
        );
    }

    @Test
    @DisplayName("saveAndFlush Update existing categoria when successful")
    void saveAndFlush_UpdateExistingCategoria_WhenSuccessful(){
        Categoria novaCategoriaInserida = inserirNovaCategoriaNoBanco();
        Categoria categoriaParaAtualizar = CategoriaCreator.mockCategoriaToUpdated(novaCategoriaInserida.getDataCriacao());

        Categoria categoriaAtualizada = categoriaRepository.saveAndFlush(categoriaParaAtualizar);
        Categoria categoriaEncontrada = categoriaRepository.findById(categoriaAtualizada.getId()).get();

        assertAll(
                () -> assertNotNull(categoriaAtualizada),
                () -> assertEquals(categoriaEncontrada, categoriaAtualizada)
        );
    }

    @Test
    @DisplayName("delete Remove categoria When successful")
    void delete_RemoveCategoria_WhenSuccessful(){
        Categoria novaCategoriaInserida = inserirNovaCategoriaNoBanco();
        categoriaRepository.delete(novaCategoriaInserida);
        List<Categoria> categorias = categoriaRepository.findAll();

        assertTrue(categorias.isEmpty());
    }
}