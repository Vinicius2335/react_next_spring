package com.viniciusvieira.backend.api.controller.venda;


import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.exception.venda.CarrinhoDeCompraNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.service.venda.CrudCarrinhoDeCompraService;
import com.viniciusvieira.backend.util.CarrinhoDeCompraCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrinhoDeCompraControllerTest {
    @InjectMocks
    private CarrinhoDeCompraController underTest;
    
    @Mock
    private CrudCarrinhoDeCompraService crudCarrinhoDeCompraServiceMock;

    private final CarrinhoDeCompra carrinhoDeCompra = CarrinhoDeCompraCreator.createCarrinhoDeCompra();
    private static final String CARRINHO_NOT_FOUND = "CarrinhoDeCompra não cadastrado";

    @Test
    @DisplayName("buscarTodos() return list of carrinhoDeCompras")
    void whenBuscarTodos_thenCarrinhosShouldBeFound() {
        // given
        given(crudCarrinhoDeCompraServiceMock.buscarTodos()).willReturn(List.of(carrinhoDeCompra));
        // when
        ResponseEntity<List<CarrinhoDeCompra>> expected = underTest.buscarTodos();
        // then
        verify(crudCarrinhoDeCompraServiceMock, times(1)).buscarTodos();
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .hasSize(1)
                .contains(carrinhoDeCompra);
    }

    @Test
    @DisplayName("inserir() insert new carrinhoDeCompra")
    void givenCarrinhoDeCompraRequest_whenInserir_thenCarrinhoDeCompraShouldBeInserted() {
        // given
        Pessoa pessoa = PessoaCreator.createPessoa();
        PessoaResponse pessoaResponse = PessoaCreator.createPessoaResponse(pessoa);
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();
        CarrinhoDeCompraResponse carrinhoDeCompraResponse = CarrinhoDeCompraCreator.createCarrinhoDeCompraResponse(pessoaResponse);
        given(crudCarrinhoDeCompraServiceMock.inserir(any(CarrinhoDeCompraRequest.class))).willReturn(carrinhoDeCompraResponse);
        // when
        ResponseEntity<CarrinhoDeCompraResponse> expected = underTest.inserir(carrinhoDeCompraRequest);
        // then
        verify(crudCarrinhoDeCompraServiceMock, times(1)).inserir(any(CarrinhoDeCompraRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(carrinhoDeCompraResponse);
    }

    @Test
    @DisplayName("buscarPeloId() return carrinhoDeCompra")
    void givenId_whenbuscarPeloId_thenCarrinhoDeCompraShouldBeFound() {
        // given
        given(crudCarrinhoDeCompraServiceMock.buscarPeloId(anyLong())).willReturn(carrinhoDeCompra);
        // when
        ResponseEntity<CarrinhoDeCompra> expected = underTest.buscarPeloId(1L);
        // then
        verify(crudCarrinhoDeCompraServiceMock, times(1)).buscarPeloId(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(carrinhoDeCompra);
    }

    @Test
    @DisplayName("buscarPorId() Throws CarrinhoDeCompraNaoEncontradoException when carrinhoDeCompra not found by id")
    void givenUnregisteredId_whenBuscarPorId_thenThrowsCarrinhoDeCompraNaoEncontradoException() {
        // given
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();
        doThrow(new CarrinhoDeCompraNaoEncontradoException(CARRINHO_NOT_FOUND)).when(crudCarrinhoDeCompraServiceMock)
                .alterar(anyLong(), any(CarrinhoDeCompraRequest.class));
        // when
        assertThatThrownBy(() -> underTest.alterar(1L, carrinhoDeCompraRequest))
                .isInstanceOf(CarrinhoDeCompraNaoEncontradoException.class)
                .hasMessageContaining(CARRINHO_NOT_FOUND);
        // then
        verify(crudCarrinhoDeCompraServiceMock, times(1))
                .alterar(anyLong(), any(CarrinhoDeCompraRequest.class));
    }

    @Test
    @DisplayName("alterar() update carrinhoDeCompra")
    void givenCarrinhoDeCompraRequest_whenAlterar_thenCarrinhoDeCompraShouldBeUpdated() {
        // given
        Pessoa pessoa = PessoaCreator.createPessoa();
        PessoaResponse pessoaResponse = PessoaCreator.createPessoaResponse(pessoa);
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();
        CarrinhoDeCompraResponse carrinhoDeCompraResponse = CarrinhoDeCompraCreator.createCarrinhoDeCompraResponse(pessoaResponse);
        given(crudCarrinhoDeCompraServiceMock.alterar(anyLong(), any(CarrinhoDeCompraRequest.class))).willReturn(carrinhoDeCompraResponse);
        // when
        ResponseEntity<CarrinhoDeCompraResponse> expected = underTest.alterar(1L, carrinhoDeCompraRequest);
        // then
        verify(crudCarrinhoDeCompraServiceMock, times(1))
                .alterar(anyLong(), any(CarrinhoDeCompraRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(carrinhoDeCompraResponse);
    }

    @Test
    @DisplayName("alterar() Throws CarrinhoDeCompraNaoEncontradoException when carrinhoDeCompra not found by id")
    void givenUnregisteredCarrinhoDeCompraRequest_whenAlterar_thenThrowsCarrinhoDeCompraNaoEncontradoException() {
        // given
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();
        doThrow(new CarrinhoDeCompraNaoEncontradoException(CARRINHO_NOT_FOUND)).when(crudCarrinhoDeCompraServiceMock)
                .alterar(anyLong(), any(CarrinhoDeCompraRequest.class));
        // when
        assertThatThrownBy(() -> underTest.alterar(1L, carrinhoDeCompraRequest))
                .isInstanceOf(CarrinhoDeCompraNaoEncontradoException.class)
                .hasMessageContaining(CARRINHO_NOT_FOUND);
        // then
        verify(crudCarrinhoDeCompraServiceMock, times(1))
                .alterar(anyLong(), any(CarrinhoDeCompraRequest.class));
    }

    @Test
    @DisplayName("excluir() remove carrinhoDeCompra")
    void givenId_whenExcluir_thenCarrinhoDeCompraShouldBeRemoved() {
        // given
        doNothing().when(crudCarrinhoDeCompraServiceMock).excluir(anyLong());
        // when
        ResponseEntity<Void> expected = underTest.excluir(1L);
        // then
        verify(crudCarrinhoDeCompraServiceMock, times(1)).excluir(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(expected.getBody()).isNull();
    }

    @Test
    @DisplayName("excluir() Throws CarrinhoDeCompraNaoEncontradoException")
    void givenUnregisteredId_whenExcluir_thenThrowsCarrinhoDeCompraNaoEncontradoException() {
        // given
        doThrow(new CarrinhoDeCompraNaoEncontradoException(CARRINHO_NOT_FOUND))
                .when(crudCarrinhoDeCompraServiceMock).excluir(anyLong());
        // when
        assertThatThrownBy(() -> underTest.excluir(1L))
                .isInstanceOf(CarrinhoDeCompraNaoEncontradoException.class)
                .hasMessageContaining(CARRINHO_NOT_FOUND);
        // then
        verify(crudCarrinhoDeCompraServiceMock, times(1)).excluir(anyLong());
    }
}