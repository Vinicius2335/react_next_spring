package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.CategoriaResponse;
import com.viniciusvieira.backend.domain.exception.CategoriaNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Categoria;
import com.viniciusvieira.backend.domain.service.CrudCategoriaService;
import com.viniciusvieira.backend.util.CategoriaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste Unitário para a classe CategoriaController")
class CategoriaControllerTest {
    @InjectMocks
    private CategoriaController categoriaController;
    @Mock
    private CrudCategoriaService crudCategoriaService;

    private final Categoria validCategoria = CategoriaCreator.mockCategoria();
    private final CategoriaResponse expectedCategoria = CategoriaCreator.mockCategoriaResponse();
    private final List<Categoria> expectedListCategorias = List.of(validCategoria);
    private final CategoriaResponse expectedCategoriaUpdated = CategoriaCreator.mockCategoriaResponseUpdate();

    @BeforeEach
    void setUp() {
        // buscarTodos
        BDDMockito.when(crudCategoriaService.buscarTodos()).thenReturn(expectedListCategorias);

        // buscarPeloId
        BDDMockito.when(crudCategoriaService.buscarPeloId(anyLong())).thenReturn(validCategoria);

        // inserir
        BDDMockito.when(crudCategoriaService.inserir(any(CategoriaRequest.class))).thenReturn(expectedCategoria);

        // alterar
        BDDMockito.when(crudCategoriaService.alterar(anyLong(), any(CategoriaRequest.class))).thenReturn(expectedCategoriaUpdated);

        // excluir
        BDDMockito.doNothing().when(crudCategoriaService).excluir(anyLong());
    }

    @Test
    @DisplayName("buscarTodos Return list of categoria When successful")
    void buscarTodos_ReturnListCategoria_WhenSuccessful() {
        ResponseEntity<List<Categoria>> response = categoriaController.buscarTodos();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedListCategorias, response.getBody())
        );
    }

    @Test
    @DisplayName("inserir Insert new categoria When successful")
    void inserir_InsertNewCategoria_WhenSuccessful() {
        CategoriaRequest categoriaParaSalvar = CategoriaCreator.mockCategoriaRequest();
        ResponseEntity<CategoriaResponse> response = categoriaController.inserir(categoriaParaSalvar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(expectedCategoria.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Update categoria when successful")
    void alterar_UpdateCategoria_WhenSuccessul() {
        CategoriaRequest categoriaParaAlterar = CategoriaCreator.mockCategoriaRequestToUpdate();
        ResponseEntity<CategoriaResponse> response = categoriaController.alterar(1L, categoriaParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedCategoriaUpdated.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Throws CategoriaNaoEncontradoException When categoria not found")
    void alterar_ThrowsCategoriaNaoEncontradoException_WhenCategoriaNotFound() {
        BDDMockito.when(crudCategoriaService.alterar(anyLong(), any(CategoriaRequest.class))).thenThrow(new CategoriaNaoEncontradoException());
        CategoriaRequest categoriaParaAlterar = CategoriaCreator.mockCategoriaRequestToUpdate();

        assertThrows(CategoriaNaoEncontradoException.class, () -> categoriaController.alterar(99L, categoriaParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove categoria when successful")
    void excluir_RemoveCategoria_WhenSuccessful() {
        ResponseEntity<Void> response = categoriaController.excluir(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Throws CategoriaNaoEncontradoException When categoria not found")
    void excluir_ThrowsCategoriaNaoEncontradoException_WhenCategoriaNotFound() {
        BDDMockito.doThrow(CategoriaNaoEncontradoException.class).when(crudCategoriaService).excluir(anyLong());

        assertThrows(CategoriaNaoEncontradoException.class, () -> categoriaController.excluir(1L));
    }
}