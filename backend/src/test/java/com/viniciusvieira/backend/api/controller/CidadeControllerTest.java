package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.controller.usuario.CidadeController;
import com.viniciusvieira.backend.api.representation.model.request.usuario.CidadeRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.CidadeResponse;
import com.viniciusvieira.backend.domain.exception.CidadeNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Cidade;
import com.viniciusvieira.backend.domain.service.CascadeDeleteService;
import com.viniciusvieira.backend.domain.service.usuario.CrudCidadeService;
import com.viniciusvieira.backend.util.CidadeCreator;
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
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste Unitário para CidadeController")
class CidadeControllerTest {
    @InjectMocks
    private CidadeController cidadeController;
    @Mock
    private CrudCidadeService mockCrudCidadeService;
    @Mock
    private CascadeDeleteService mockCascadeDeleteService;

    private final Cidade validCidade = CidadeCreator.mockCidade();
    private final CidadeResponse expectedCidade = CidadeCreator.mockCidadeResponse();
    private final CidadeResponse expectedCidadeUpdate = CidadeCreator.mockCidadeResponseUpdated();

    @BeforeEach
    void setUp(){
        // buscarTodos
        BDDMockito.when(mockCrudCidadeService.buscarTodos()).thenReturn(List.of(validCidade));
        // buscarPeloId
        BDDMockito.when(mockCrudCidadeService.buscarPeloId(anyLong())).thenReturn(validCidade);
        // inserir
        BDDMockito.when(mockCrudCidadeService.inserir(any(CidadeRequest.class))).thenReturn(expectedCidade);
        // alterar
        BDDMockito.when(mockCrudCidadeService.alterar(anyLong(), any(CidadeRequest.class))).thenReturn(expectedCidadeUpdate);
        // excluir
        BDDMockito.doNothing().when(mockCrudCidadeService).excluir(anyLong());

        // CascadeDeleteService
        BDDMockito.doNothing().when(mockCascadeDeleteService).cascadeDeleteCidade(anyLong());
    }

    @Test
    @DisplayName("buscarTodos Return list of cidade When successful")
    void buscarTodos_ReturnListCidade_WhenSuccessful() {
        ResponseEntity<List<Cidade>> response = cidadeController.buscarTodos();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertTrue(response.getBody().contains(validCidade))
        );
    }

    @Test
    @DisplayName("inserir Insert new cidade When successful")
    void inserir_InsertNewCidade_WhenSuccessful() {
        CidadeRequest cidadeParaInserir = CidadeCreator.mockCidadeRequestToSave();
        ResponseEntity<CidadeResponse> response = cidadeController.inserir(cidadeParaInserir);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(expectedCidade.getNome(), response.getBody().getNome()),
                () -> assertEquals(expectedCidade.getNomeEstado(), response.getBody().getNomeEstado())
        );
    }

    @Test
    @DisplayName("alterar Update cidade when successful")
    void alterar_UpdateCidade_WhenSuccessul() {
        CidadeRequest cidadeParaAlterar = CidadeCreator.mockCidadeRequestToUpdate();
        ResponseEntity<CidadeResponse> response = cidadeController.alterar(1L, cidadeParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedCidadeUpdate.getNome(), response.getBody().getNome()),
                () -> assertEquals(expectedCidadeUpdate.getNomeEstado(), response.getBody().getNomeEstado())
        );

    }

    @Test
    @DisplayName("alterar Throws CidadeNaoEncontradoException When cidade not found")
    void alterar_ThrowsCidadeNaoEncontradoException_WhenCidadeNotFound() {
        BDDMockito.when(mockCrudCidadeService.alterar(anyLong(), any(CidadeRequest.class))).thenThrow(new CidadeNaoEncontradaException());
        CidadeRequest cidadeParaAlterar = CidadeCreator.mockCidadeRequestToUpdate();

        assertThrows(CidadeNaoEncontradaException.class, () -> cidadeController.alterar(2L, cidadeParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove cidade when successful")
    void excluir_RemoveCidade_WhenSuccessful() {
        ResponseEntity<Void> response = cidadeController.excluir(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Throws CidadeNaoEncontradoException When cidade not found")
    void excluir_ThrowsCidadeNaoEncontradoException_WhenCidadeNotFound() {
        BDDMockito.doThrow(CidadeNaoEncontradaException.class).when(mockCrudCidadeService).buscarPeloId(anyLong());

        assertThrows(CidadeNaoEncontradaException.class, () -> cidadeController.excluir(9L));
    }
}