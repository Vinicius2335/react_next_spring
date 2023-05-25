package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.domain.exception.EstadoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Estado;
import com.viniciusvieira.backend.domain.service.CrudEstadoService;
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

    private final Estado expectedEstado = EstadoCreator.mockValidEstado();
    private final List<Estado> expectedListEstados = List.of(expectedEstado);
    private final Estado expectedEstadoToUpdate = EstadoCreator.mockEstadoToUpdate(expectedEstado.getDataCriacao());

    @BeforeEach
    void setUp() {
        // buscarTodos
        BDDMockito.when(mockCrudEstadoService.buscarTodos()).thenReturn(expectedListEstados);

        // buscarPeloId
        BDDMockito.when(mockCrudEstadoService.buscarPeloId(anyLong())).thenReturn(expectedEstado);

        // inserir
        BDDMockito.when(mockCrudEstadoService.inserir(any(Estado.class))).thenReturn(expectedEstado);

        // alterar
        BDDMockito.when(mockCrudEstadoService.alterar(anyLong(), any(Estado.class))).thenReturn(expectedEstadoToUpdate);

        // excluir
        BDDMockito.doNothing().when(mockCrudEstadoService).excluir(anyLong());
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
        Estado estadoParaSalvar = EstadoCreator.mockValidEstado();
        ResponseEntity<Estado> response = estadoController.inserir(estadoParaSalvar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(expectedEstado.getId(), response.getBody().getId()),
                () -> assertEquals(expectedEstado.getNome(), response.getBody().getNome()),
                () -> assertEquals(expectedEstado.getSigla(), response.getBody().getSigla())
        );
    }

    @Test
    @DisplayName("alterar Update estado when successful")
    void alterar_UpdateEstado_WhenSuccessul() {
        Estado estadoParaAlterar = EstadoCreator.mockEstadoToUpdate(expectedEstado.getDataCriacao());
        ResponseEntity<Estado> response = estadoController.alterar(1L, estadoParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(estadoParaAlterar.getId(), response.getBody().getId()),
                () -> assertEquals(estadoParaAlterar.getNome(), response.getBody().getNome()),
                () -> assertEquals(expectedEstado.getSigla(), response.getBody().getSigla())
        );
    }

    @Test
    @DisplayName("alterar Throws EstadoNaoEncontradoException When estado not found")
    void alterar_ThrowsEstadoNaoEncontradoException_WhenEstadoNotFound() {
        BDDMockito.when(mockCrudEstadoService.alterar(anyLong(), any(Estado.class))).thenThrow(new EstadoNaoEncontradoException());
        Estado estadoParaAlterar = EstadoCreator.mockEstadoToUpdate(expectedEstado.getDataCriacao());

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
        BDDMockito.doThrow(EstadoNaoEncontradoException.class).when(mockCrudEstadoService).excluir(anyLong());

        assertThrows(EstadoNaoEncontradoException.class, () -> estadoController.excluir(1L));
    }
}