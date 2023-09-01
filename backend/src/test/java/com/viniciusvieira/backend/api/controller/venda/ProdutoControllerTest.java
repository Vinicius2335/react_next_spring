package com.viniciusvieira.backend.api.controller.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.exception.venda.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.service.venda.CrudProdutoService;
import com.viniciusvieira.backend.util.ProdutoCreator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {
    private static final String PRODUTO_NOT_FOUND = "Produto não encontrada";

    @InjectMocks
    private ProdutoController undertTest;

    @Mock
    private CrudProdutoService crudProdutoServiceMock;

    private final Produto produto = ProdutoCreator.createProduto();

    @Test
    @DisplayName("buscarTodos() return list of produtos")
    void whenBuscarTodos_thenProdutosShouldBeFound() {
        // given
        given(crudProdutoServiceMock.buscarTodos()).willReturn(List.of(produto));
        // when
        ResponseEntity<List<Produto>> expected = undertTest.buscarTodos();
        // then
        verify(crudProdutoServiceMock, times(1)).buscarTodos();
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .hasSize(1)
                .contains(produto);
    }

    @Test
    @DisplayName("inserir() insert new produto")
    void givenProdutoRequest_whenInserir_thenProdutoShouldBeInserted() {
        // given
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);
        ProdutoResponse produtoResponse = ProdutoCreator.createProdutoResponse(produto);
        given(crudProdutoServiceMock.inserir(any(ProdutoRequest.class))).willReturn(produtoResponse);
        // when
        ResponseEntity<ProdutoResponse> expected = undertTest.inserir(produtoRequest);
        // then
        verify(crudProdutoServiceMock, times(1)).inserir(any(ProdutoRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(produtoResponse);
    }

    @Test
    @Disabled
    @DisplayName("uploadFile()")
    void uploadFile() {
        // TEST - ImagemUploadService PRIMEIRO
    }

    @Test
    @DisplayName("alterar() update produto")
    void givenProdutoRequest_whenAlterar_thenProdutoShouldBeUpdated() {
        // given
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);
        ProdutoResponse produtoResponse = ProdutoCreator.createProdutoResponse(produto);
        given(crudProdutoServiceMock.alterar(anyLong(), any(ProdutoRequest.class))).willReturn(produtoResponse);
        // when
        ResponseEntity<ProdutoResponse> expected = undertTest.alterar(1L, produtoRequest);
        // then
        verify(crudProdutoServiceMock, times(1))
                .alterar(anyLong(), any(ProdutoRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(produtoResponse);
    }

    @Test
    @DisplayName("alterar() Throws ProdutoNaoEncontradoException when produto not found by id")
    void givenUnregisteredProdutoRequest_whenAlterar_thenThrowsProdutoNaoEncontradoException() {
        // given
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);
        ProdutoResponse produtoResponse = ProdutoCreator.createProdutoResponse(produto);
        doThrow(new ProdutoNaoEncontradoException(PRODUTO_NOT_FOUND)).when(crudProdutoServiceMock)
                .alterar(anyLong(), any(ProdutoRequest.class));
        // when
        assertThatThrownBy(() -> undertTest.alterar(1L, produtoRequest))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                        .hasMessageContaining(PRODUTO_NOT_FOUND);
        // then
        verify(crudProdutoServiceMock, times(1))
                .alterar(anyLong(), any(ProdutoRequest.class));
    }

    @Test
    @Disabled
    @DisplayName("alterarImagem()")
    void alterarImagem() {
        // TEST - ImagemUploadService PRIMEIRO
    }

    @Test
    @DisplayName("excluir() remove produto")
    void givenId_whenExcluir_thenProdutoShouldBeRemoved() {
        // given
        doNothing().when(crudProdutoServiceMock).excluir(anyLong());
        // when
        ResponseEntity<Void> expected = undertTest.excluir(1L);
        // then
        verify(crudProdutoServiceMock, times(1)).excluir(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(expected.getBody()).isNull();
    }

    @Test
    @DisplayName("excluir() Throws ProdutoNaoEncontradoException")
    void givenUnregisteredId_whenExcluir_thenThrowsProdutoNaoEncontradoException() {
        // given
        doThrow(new ProdutoNaoEncontradoException(PRODUTO_NOT_FOUND))
                .when(crudProdutoServiceMock).excluir(anyLong());
        // when
        assertThatThrownBy(() -> undertTest.excluir(1L))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                .hasMessageContaining(PRODUTO_NOT_FOUND);
        // then
        verify(crudProdutoServiceMock, times(1)).excluir(anyLong());
    }
}