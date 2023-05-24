package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.domain.exception.MarcaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Marca;
import com.viniciusvieira.backend.domain.service.CrudMarcaService;
import com.viniciusvieira.backend.util.MarcaCreator;
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
@DisplayName("Teste Unitário para a classe MarcaController")
class MarcaControllerTest {
    @InjectMocks
    private MarcaController marcaController;

    @Mock
    private CrudMarcaService mockCrudMarcaService;

    private final Marca expectedMarca = MarcaCreator.mockValidMarca();
    private final List<Marca> expectedListMarcas = List.of(expectedMarca);
    private final Marca expectedMarcaToUpdate = MarcaCreator.mockMarcaToUpdate(expectedMarca.getDataCriacao());

    @BeforeEach
    void setUp() {
        // buscarTodos
        BDDMockito.when(mockCrudMarcaService.buscarTodos()).thenReturn(expectedListMarcas);

        // buscarPeloId
        BDDMockito.when(mockCrudMarcaService.buscarPeloId(anyLong())).thenReturn(expectedMarca);

        // inserir
        BDDMockito.when(mockCrudMarcaService.inserir(any(Marca.class))).thenReturn(expectedMarca);

        // alterar
        BDDMockito.when(mockCrudMarcaService.alterar(anyLong(), any(Marca.class))).thenReturn(expectedMarcaToUpdate);

        // excluir
        BDDMockito.doNothing().when(mockCrudMarcaService).excluir(anyLong());
    }

    @Test
    @DisplayName("buscarTodos Return list of marca When successful")
    void buscarTodos_ReturnListMarca_WhenSuccessful() {
        ResponseEntity<List<Marca>> response = marcaController.buscarTodos();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedListMarcas, response.getBody())
        );
    }

    @Test
    @DisplayName("buscarPeloId Return marca When successful")
    void buscarPeloId_ReturnMarca_WhenSuccessful() {
        ResponseEntity<Marca> response = marcaController.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedMarca, response.getBody())
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws MarcaNaoEncontradaException When marca not found")
    void buscarPeloId_ThrowsMarcaNaoEncontradaException_WhenMarcaNotFound() {
        BDDMockito.when(mockCrudMarcaService.buscarPeloId(anyLong())).thenThrow(new MarcaNaoEncontradaException());

        assertThrows(MarcaNaoEncontradaException.class, () -> marcaController.buscarPeloId(1L));
    }

    @Test
    @DisplayName("inserir Insert new marca When successful")
    void inserir_InsertNewMarca_WhenSuccessful() {
        Marca marcaParaSalvar = MarcaCreator.mockValidMarca();
        ResponseEntity<Marca> response = marcaController.inserir(marcaParaSalvar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(expectedMarca.getId(), response.getBody().getId()),
                () -> assertEquals(expectedMarca.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Update marca when successful")
    void alterar_UpdateMarca_WhenSuccessul() {
        Marca marcaParaAlterar = MarcaCreator.mockMarcaToUpdate(expectedMarca.getDataCriacao());
        ResponseEntity<Marca> response = marcaController.alterar(1L, marcaParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(marcaParaAlterar.getId(), response.getBody().getId()),
                () -> assertEquals(marcaParaAlterar.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Throws MarcaNaoEncontradaException When marca not found")
    void alterar_ThrowsMarcaNaoEncontradaException_WhenMarcaNotFound() {
        BDDMockito.when(mockCrudMarcaService.alterar(anyLong(), any(Marca.class))).thenThrow(new MarcaNaoEncontradaException());
        Marca marcaParaAlterar = MarcaCreator.mockMarcaToUpdate(expectedMarca.getDataCriacao());

        assertThrows(MarcaNaoEncontradaException.class, () -> marcaController.alterar(99L, marcaParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove marca when successful")
    void excluir_RemoveMarca_WhenSuccessful() {
        ResponseEntity<Void> response = marcaController.excluir(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Throws MarcaNaoEncontradaException When marca not found")
    void excluir_ThrowsMarcaNaoEncontradaException_WhenMarcaNotFound() {
        BDDMockito.doThrow(MarcaNaoEncontradaException.class).when(mockCrudMarcaService).excluir(anyLong());

        assertThrows(MarcaNaoEncontradaException.class, () -> marcaController.excluir(1L));
    }
}