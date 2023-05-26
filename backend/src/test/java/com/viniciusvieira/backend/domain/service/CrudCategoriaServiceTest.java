package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.CategoriaMapper;
import com.viniciusvieira.backend.api.representation.model.request.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.CategoriaResponse;
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
    @Mock
    private CategoriaMapper mockCategoriaMapper;

    private final Categoria validCategoria = CategoriaCreator.mockCategoria();
    private final CategoriaResponse expectedCategoria = CategoriaCreator.mockCategoriaResponse();
    private final CategoriaResponse expectedCategoriaUpdated = CategoriaCreator.mockCategoriaResponseUpdate();
    private final List<Categoria> expectedListCategoria = List.of(validCategoria);

    @BeforeEach
    void setUp() {
        // findAll
        BDDMockito.when(mockCategoriaRespository.findAll()).thenReturn(expectedListCategoria);

        // findById
        BDDMockito.when(mockCategoriaRespository.findById(anyLong())).thenReturn(Optional.of(validCategoria));

        // saveAndFlush
        BDDMockito.when(mockCategoriaRespository.saveAndFlush(any(Categoria.class))).thenReturn(validCategoria);

        // delete
        BDDMockito.doNothing().when(mockCategoriaRespository).delete(any(Categoria.class));

        // Categoria Mapper - toDomainCategoria
        BDDMockito.when(mockCategoriaMapper.toDomainCategoria(any(CategoriaRequest.class))).thenReturn(validCategoria);

        // toCategoriaResponse
        BDDMockito.when(mockCategoriaMapper.toCategoriaResponse(any(Categoria.class))).thenReturn(expectedCategoria);
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
                () -> assertTrue(categorias.contains(validCategoria))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return categoria When successful")
    void buscarPeloId_ReturnCategoria_WhenSuccessful() {
        Categoria categoria = crudCategoriaService.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(categoria),
                () -> assertEquals(expectedCategoria.getNome(), categoria.getNome())
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
        CategoriaRequest categoriaParaInserir = CategoriaCreator.mockCategoriaRequest();
        CategoriaResponse categoriaInserida = crudCategoriaService.inserir(categoriaParaInserir);

        assertAll(
                () -> assertNotNull(categoriaInserida),
                () -> assertEquals(expectedCategoria.getNome(), categoriaInserida.getNome())
        );
    }

    @Test
    @DisplayName("alterar Update categoria when successful")
    void alterar_UpdateCategoria_WhenSuccessul() {
        BDDMockito.when(mockCategoriaMapper.toCategoriaResponse(any(Categoria.class))).thenReturn(expectedCategoriaUpdated);

        CategoriaRequest categoriaParaAlterar = CategoriaCreator.mockCategoriaRequestToUpdate();
        CategoriaResponse categoriaAlterada = crudCategoriaService.alterar(1L, categoriaParaAlterar);

        assertAll(
                () -> assertNotNull(categoriaAlterada),
                () -> assertEquals(expectedCategoriaUpdated.getNome(), categoriaAlterada.getNome())
        );
    }

    @Test
    @DisplayName("alterar Throws CategoriaNaoEncontradoException When categoria not found")
    void alterar_ThrowsCategoriaNaoEncontradoException_WhenCategoriaNotFound() {
        BDDMockito.when(mockCategoriaRespository.findById(anyLong())).thenReturn(Optional.empty());

        CategoriaRequest categoriaParaAlterar = CategoriaCreator.mockCategoriaRequestToUpdate();

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