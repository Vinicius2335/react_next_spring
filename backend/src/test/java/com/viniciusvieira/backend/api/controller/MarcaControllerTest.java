package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.MarcaResponse;
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

    private final Marca validMarca = MarcaCreator.mockMarca();
    private final MarcaResponse expectedMarca = MarcaCreator.mockMarcaResponse();
    private final List<Marca> expectedListMarcas = List.of(validMarca);
    private final MarcaResponse expectedMarcaUpdated = MarcaCreator.mockMarcaResponseUpdate();

    @BeforeEach
    void setUp() {
        // buscarTodos
        BDDMockito.when(mockCrudMarcaService.buscarTodos()).thenReturn(expectedListMarcas);

        // buscarPeloId
        BDDMockito.when(mockCrudMarcaService.buscarPeloId(anyLong())).thenReturn(validMarca);

        // inserir
        BDDMockito.when(mockCrudMarcaService.inserir(any(MarcaRequest.class))).thenReturn(expectedMarca);

        // alterar
        BDDMockito.when(mockCrudMarcaService.alterar(anyLong(), any(MarcaRequest.class))).thenReturn(expectedMarcaUpdated);

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
                () -> assertEquals(validMarca, response.getBody())
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
        MarcaRequest marcaParaSalvar = MarcaCreator.mockMarcaRequestToSave();
        ResponseEntity<MarcaResponse> response = marcaController.inserir(marcaParaSalvar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(validMarca.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Update marca when successful")
    void alterar_UpdateMarca_WhenSuccessul() {
        MarcaRequest marcaParaAlterar = MarcaCreator.mockMarcaRequestToUpdate();
        ResponseEntity<MarcaResponse> response = marcaController.alterar(1L, marcaParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedMarcaUpdated.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Throws MarcaNaoEncontradaException When marca not found")
    void alterar_ThrowsMarcaNaoEncontradaException_WhenMarcaNotFound() {
        BDDMockito.when(mockCrudMarcaService.alterar(anyLong(), any(MarcaRequest.class))).thenThrow(new MarcaNaoEncontradaException());
        MarcaRequest marcaParaAlterar = MarcaCreator.mockMarcaRequestToUpdate();

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