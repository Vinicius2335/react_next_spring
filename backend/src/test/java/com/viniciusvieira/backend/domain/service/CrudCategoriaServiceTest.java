package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.exception.CategoriaNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Categoria;
import com.viniciusvieira.backend.domain.repository.CategoriaRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste Unitário para a classe CrudCategoriaService")
class CrudCategoriaServiceTest {
    @InjectMocks
    private CrudCategoriaService crudCategoriaService;

    @Mock
    private CategoriaRepository mockCategoriaRespository;

    private final Categoria expectedCategoria = CategoriaCreator.mockValidCategoria();
    private final List<Categoria> expectedListCategoria = List.of(expectedCategoria);

    @BeforeEach
    void setUp() {
        // findAll
        BDDMockito.when(mockCategoriaRespository.findAll()).thenReturn(expectedListCategoria);

        // findById
        BDDMockito.when(mockCategoriaRespository.findById(anyLong())).thenReturn(Optional.of(expectedCategoria));

        // saveAndFlush
        BDDMockito.when(mockCategoriaRespository.saveAndFlush(any(Categoria.class))).thenReturn(expectedCategoria);

        // delete
        BDDMockito.doNothing().when(mockCategoriaRespository).delete(any(Categoria.class));
    }

    @Test
    @DisplayName("buscarTodos Return a list of Categoria When successful")
    void buscarTodos_ReturnListCategoria_WhenSuccessful() {
        List<Categoria> categorias = crudCategoriaService.buscarTodos();
        crudCategoriaService.buscarTodos();

        assertAll(
                () -> assertNotNull(categorias),
                () -> assertFalse(categorias.isEmpty()),
                () -> assertEquals(1, categorias.size()),
                () -> assertTrue(categorias.contains(expectedCategoria))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return categoria When successful")
    void buscarPeloId_ReturnCategoria_WhenSuccessful() {
        Categoria categoria = crudCategoriaService.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(expectedCategoria, categoria)
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws CategoriaNaoEncontradoException When categoria not found")
    void buscarPeloId_ThrowsCategoriaNaoEncontradoException_WhenCategoriaNotFound() {
        BDDMockito.when(mockCategoriaRespository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoriaNaoEncontradoException.class, () -> crudCategoriaService.buscarPeloId(99L));
    }

    @Test
    @DisplayName("inserir Insert new categoria When successful")
    void inserir_InsertNewCategoria_WhenSuccessful() {
        Categoria categoriaParaInserir = CategoriaCreator.mockValidCategoria();
        Categoria categoriaInserida = crudCategoriaService.inserir(categoriaParaInserir);

        assertAll(
                () -> assertNotNull(categoriaInserida),
                () -> assertEquals(expectedCategoria.getId(), categoriaInserida.getId()),
                () -> assertEquals(expectedCategoria.getNome(), categoriaInserida.getNome())
        );
    }

    @Test
    @DisplayName("alterar Update categoria when successful")
    void alterar_UpdateCategoria_WhenSuccessul() {
        Categoria categoriaParaAlterar = CategoriaCreator.mockCategoriaToUpdate(expectedCategoria.getDataCriacao());
        Categoria categoriaAlterada = crudCategoriaService.alterar(1L, categoriaParaAlterar);
        categoriaAlterada.setDataAtualizacao(categoriaParaAlterar.getDataAtualizacao());

        assertAll(
                () -> assertNotNull(categoriaAlterada),
                () -> assertEquals(categoriaParaAlterar, categoriaAlterada)
        );
    }

    @Test
    @DisplayName("alterar Throws CategoriaNaoEncontradoException When categoria not found")
    void alterar_ThrowsCategoriaNaoEncontradoException_WhenCategoriaNotFound() {
        BDDMockito.when(mockCategoriaRespository.findById(anyLong())).thenReturn(Optional.empty());

        Categoria categoriaParaAlterar = CategoriaCreator.mockCategoriaToUpdate(expectedCategoria.getDataCriacao());

        assertThrows(CategoriaNaoEncontradoException.class, () -> crudCategoriaService.alterar(99L, categoriaParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove categoria when successful")
    void excluir_RemoveCategoria_WhenSuccessful() {
        assertDoesNotThrow(() -> crudCategoriaService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws CategoriaNaoEncontradoException When categoria not found")
    void excluir_ThrowsCategoriaNaoEncontradoException_WhenCategoriaNotFound() {
        BDDMockito.when(mockCategoriaRespository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoriaNaoEncontradoException.class, () -> crudCategoriaService.excluir(1L));
    }
}