package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.PermissaoResponse;
import com.viniciusvieira.backend.domain.exception.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Permissao;
import com.viniciusvieira.backend.domain.service.CrudPermissaoService;
import com.viniciusvieira.backend.util.PermissaoCreator;
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
@DisplayName("Teste Unitário para a classe PermissaoController")
class PermissaoControllerTest {
    @InjectMocks
    private PermissaoController permissaoController;
    @Mock
    private CrudPermissaoService mockCrudPermissaoService;

    private final Permissao validPermissao = PermissaoCreator.mockPermissao();
    private final PermissaoResponse expectedPermissao = PermissaoCreator.mockPermissaoResponse();
    private final PermissaoResponse expectedPermissaoUpdated = PermissaoCreator.mockPermissaoResponseUpdated();
    private final List<Permissao> expectedListPermissao = List.of(validPermissao);

    @BeforeEach
    void setUp() {
        // buscarTodos
        BDDMockito.when(mockCrudPermissaoService.buscarTodos()).thenReturn(expectedListPermissao);
        // buscarPeloId
        BDDMockito.when(mockCrudPermissaoService.buscarPeloId(anyLong())).thenReturn(validPermissao);
        // inserir
        BDDMockito.when(mockCrudPermissaoService.inserir(any(PermissaoRequest.class))).thenReturn(expectedPermissao);
        // alterar
        BDDMockito.when(mockCrudPermissaoService.alterar(anyLong(), any(PermissaoRequest.class))).thenReturn(expectedPermissaoUpdated);
        // excluir
        BDDMockito.doNothing().when(mockCrudPermissaoService).excluir(anyLong());
    }

    @Test
    @DisplayName("buscarTodos Return list of permissao When successful")
    void buscarTodos_ReturnListPermissao_WhenSuccessful() {
        ResponseEntity<List<Permissao>> response = permissaoController.buscarTodos();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedListPermissao, response.getBody())
        );
    }

    @Test
    @DisplayName("buscarPeloId Return permissao When successful")
    void buscarPeloId_ReturnPermissao_WhenSuccessful() {
        ResponseEntity<Permissao> response = permissaoController.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(validPermissao, response.getBody())
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws PermissaoNaoEncontradaException When permissao not found")
    void buscarPeloId_ThrowsPermissaoNaoEncontradaException_WhenPermissaoNotFound() {
        BDDMockito.when(mockCrudPermissaoService.buscarPeloId(anyLong())).thenThrow(new PermissaoNaoEncontradaException());

        assertThrows(PermissaoNaoEncontradaException.class, () -> permissaoController.buscarPeloId(1L));
    }

    @Test
    @DisplayName("inserir Insert new permissao When successful")
    void inserir_InsertNewPermissao_WhenSuccessful() {
        PermissaoRequest permissaoParaSalvar = PermissaoCreator.mockPermissaoRequest();
        ResponseEntity<PermissaoResponse> response = permissaoController.inserir(permissaoParaSalvar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(validPermissao.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Update permissao when successful")
    void alterar_UpdatePermissao_WhenSuccessul() {
        PermissaoRequest permissaoParaAlterar = PermissaoCreator.mockPermissaoRequestToUpdate();
        ResponseEntity<PermissaoResponse> response = permissaoController.alterar(1L, permissaoParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedPermissaoUpdated.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Throws PermissaoNaoEncontradaException When permissao not found")
    void alterar_ThrowsPermissaoNaoEncontradaException_WhenPermissaoNotFound() {
        BDDMockito.when(mockCrudPermissaoService.alterar(anyLong(), any(PermissaoRequest.class))).thenThrow(new PermissaoNaoEncontradaException());
        PermissaoRequest permissaoParaAlterar = PermissaoCreator.mockPermissaoRequestToUpdate();

        assertThrows(PermissaoNaoEncontradaException.class, () -> permissaoController.alterar(99L, permissaoParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove permissao when successful")
    void excluir_RemovePermissao_WhenSuccessful() {
        ResponseEntity<Void> response = permissaoController.excluir(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Throws PermissaoNaoEncontradaException When permissao not found")
    void excluir_ThrowsPermissaoNaoEncontradaException_WhenPermissaoNotFound() {
        BDDMockito.doThrow(PermissaoNaoEncontradaException.class).when(mockCrudPermissaoService).excluir(anyLong());

        assertThrows(PermissaoNaoEncontradaException.class, () -> permissaoController.excluir(1L));
    }
}