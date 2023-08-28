package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.domain.service.CascadeDeleteService;
import com.viniciusvieira.backend.util.EstadoCreator;
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
@DisplayName("Teste Unitário para a classe EstadoController")
class EstadoControllerTest {
    @InjectMocks
    private EstadoController estadoController;

    @Mock
    private CrudEstadoService mockCrudEstadoService;
    @Mock
    private CascadeDeleteService mockCascadeDeleteService;

    private final Estado validEstado = EstadoCreator.mockEstado();
    private final EstadoResponse expectedEstado = EstadoCreator.mockEstadoResponse();
    private final List<Estado> expectedListEstados = List.of(validEstado);
    private final EstadoResponse expectedEstadoUpdated = EstadoCreator.mockEstadoResponseUpdate();

    @BeforeEach
    void setUp() {
        // buscarTodos
        BDDMockito.when(mockCrudEstadoService.buscarTodos()).thenReturn(expectedListEstados);
        // buscarPeloId
        BDDMockito.when(mockCrudEstadoService.buscarPeloId(anyLong())).thenReturn(validEstado);
        // inserir
        BDDMockito.when(mockCrudEstadoService.inserir(any(EstadoRequest.class))).thenReturn(expectedEstado);
        // alterar
        BDDMockito.when(mockCrudEstadoService.alterar(anyLong(), any(EstadoRequest.class))).thenReturn(expectedEstadoUpdated);
        // excluir
        BDDMockito.doNothing().when(mockCrudEstadoService).excluir(anyLong());

        // CascadeDeleteService
        BDDMockito.doNothing().when(mockCascadeDeleteService).cascadeDeleteEstado(anyLong());
    }

    @Test
    @DisplayName("buscarTodos Return list of estado When successful")
    void buscarTodos_ReturnListEstado_WhenSuccessful() {
        ResponseEntity<List<Estado>> response = estadoController.buscarTodos();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedListEstados, response.getBody())
        );
    }

    @Test
    @DisplayName("inserir Insert new estado When successful")
    void inserir_InsertNewEstado_WhenSuccessful() {
        EstadoRequest estadoParaSalvar = EstadoCreator.mockEstadoRequestToSave();
        ResponseEntity<EstadoResponse> response = estadoController.inserir(estadoParaSalvar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(expectedEstado.getNome(), response.getBody().getNome()),
                () -> assertEquals(expectedEstado.getSigla(), response.getBody().getSigla())
        );
    }

    @Test
    @DisplayName("alterar Update estado when successful")
    void alterar_UpdateEstado_WhenSuccessul() {
        EstadoRequest estadoParaAlterar = EstadoCreator.mockEstadoRequestToUpdate();
        ResponseEntity<EstadoResponse> response = estadoController.alterar(1L, estadoParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedEstadoUpdated.getNome(), response.getBody().getNome()),
                () -> assertEquals(expectedEstadoUpdated.getSigla(), response.getBody().getSigla())
        );
    }

    @Test
    @DisplayName("alterar Throws EstadoNaoEncontradoException When estado not found")
    void alterar_ThrowsEstadoNaoEncontradoException_WhenEstadoNotFound() {
        BDDMockito.when(mockCrudEstadoService.alterar(anyLong(), any(EstadoRequest.class))).thenThrow(new EstadoNaoEncontradoException());
        EstadoRequest estadoParaAlterar = EstadoCreator.mockEstadoRequestToUpdate();

        assertThrows(EstadoNaoEncontradoException.class, () -> estadoController.alterar(99L, estadoParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove estado when successful")
    void excluir_RemoveEstado_WhenSuccessful() {
        ResponseEntity<Void> response = estadoController.excluir(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Throws EstadoNaoEncontradoException When estado not found")
    void excluir_ThrowsEstadoNaoEncontradoException_WhenEstadoNotFound() {
        BDDMockito.doThrow(EstadoNaoEncontradoException.class).when(mockCrudEstadoService).buscarPeloId(anyLong());

        assertThrows(EstadoNaoEncontradoException.class, () -> estadoController.excluir(1L));
    }
}