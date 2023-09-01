package com.viniciusvieira.backend.domain.service.venda;


import com.viniciusvieira.backend.api.mapper.venda.CarrinhoDeCompraMapper;
import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.exception.venda.CarrinhoDeCompraNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.repository.venda.CarrinhoCompraRepository;
import com.viniciusvieira.backend.util.CarrinhoDeCompraCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrudCarrinhoDeCompraServiceTest {
    @InjectMocks
    private CrudCarrinhoDeCompraService underTest;

    @Mock
    private  CarrinhoCompraRepository carrinhoCompraRepositoryMock;
    @Mock
    private  CarrinhoDeCompraMapper carrinhoDeCompraMapperMock;
    
    private final CarrinhoDeCompra carrinhoDeCompra = CarrinhoDeCompraCreator.createCarrinhoDeCompra();
    private static final String CARRINHO_NOT_FOUND = "CarrinhoDeCompra não cadastrado";

    @Test
    @DisplayName("buscarTodos() return list of pessoa")
    void whenBuscarTodos_thenCarrinhosShouldBeFound() {
        // given
        given(carrinhoCompraRepositoryMock.findAll()).willReturn(List.of(carrinhoDeCompra));
        // when
        List<CarrinhoDeCompra> expected = underTest.buscarTodos();
        // then
        verify(carrinhoCompraRepositoryMock, times(1)).findAll();
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .contains(carrinhoDeCompra);
    }

    @Test
    @DisplayName("buscarPeloId() return pessoa by id")
    void givenId_whenBuscarPorId_thenPessoaShoudBeFound() {
        // given
        buscarPeloIdConfig();
        // then
        CarrinhoDeCompra expected = underTest.buscarPeloId(1L);
        // when
        verify(carrinhoCompraRepositoryMock, times(1)).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .isEqualTo(carrinhoDeCompra);
    }

    private void buscarPeloIdConfig() {
        given(carrinhoCompraRepositoryMock.findById(anyLong())).willReturn(Optional.of(carrinhoDeCompra));
    }

    @Test
    @DisplayName("buscarPeloId() Throws CarrinhoDeCompraNaoEncontradoException when carrinhoDeCompra not found by id")
    void givenUnregisteredId_whenBuscarPorId_thenThrowsCarrinhoDeCompraNaoEncontradoException() {
        // given
        buscarPeloIdTestException();
        // then
        assertThatThrownBy(() -> underTest.buscarPeloId(1L))
                .isInstanceOf(CarrinhoDeCompraNaoEncontradoException.class)
                .hasMessageContaining(CARRINHO_NOT_FOUND);
        // when
        verify(carrinhoCompraRepositoryMock, times(1)).findById(anyLong());
    }

    private void buscarPeloIdTestException() {
        given(carrinhoCompraRepositoryMock.findById(anyLong())).willReturn(Optional.empty());
    }

    @Test
    @DisplayName("inserir() insert new carrinhoDeCompra")
    void givenCarrinhoDeCompra_whenInserir_thenCarrinhoDeCompraShouldBeInserted() {
        // given
        inserirConfig();
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();
        // when
        CarrinhoDeCompraResponse expected = underTest.inserir(carrinhoDeCompraRequest);
        // then
        verify(carrinhoCompraRepositoryMock, times(1)).saveAndFlush(any(CarrinhoDeCompra.class));
        assertThat(expected).isNotNull();
        assertThat(expected.getSituacao()).isEqualTo(carrinhoDeCompraRequest.getSituacao());
    }

    private void inserirConfig(){
        Pessoa pessoa = PessoaCreator.createPessoa();
        PessoaResponse pessoaResponse = PessoaCreator.createPessoaResponse(pessoa);
        CarrinhoDeCompraResponse carrinhoDeCompraResponse = CarrinhoDeCompraCreator
                .createCarrinhoDeCompraResponse(pessoaResponse);

        given(carrinhoDeCompraMapperMock.toDomainCarrinhoDeCompra(any(CarrinhoDeCompraRequest.class)))
                .willReturn(carrinhoDeCompra);
        given(carrinhoCompraRepositoryMock.saveAndFlush(any(CarrinhoDeCompra.class)))
                .willReturn(carrinhoDeCompra);
        given(carrinhoDeCompraMapperMock.toCarrinhoDeCompraResponse(any(CarrinhoDeCompra.class)))
                .willReturn(carrinhoDeCompraResponse);
    }

    @Test
    @DisplayName("alterar() update carrinhoDeCompra")
    void givenCarrinhoDeCompra_whenAlterar_thenCarrinhoDeCompraShouldBeUpdated() {
        // given
        alterarConfig();
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();
        // when
        CarrinhoDeCompraResponse expected = underTest.alterar(1L, carrinhoDeCompraRequest);
        // then
        verify(carrinhoCompraRepositoryMock, times(1)).saveAndFlush(any(CarrinhoDeCompra.class));
        assertThat(expected).isNotNull();
        assertThat(expected.getSituacao()).isEqualTo(carrinhoDeCompraRequest.getSituacao());
    }

    private void alterarConfig(){
        buscarPeloIdConfig();
        inserirConfig();
    }

    @Test
    @DisplayName("alterar() Throws CarrinhoDeCompraNaoEncontradaException when carrinhoDeCompra not found by id")
    void givenCarrinhoDeCompra_whenAlterar_thenThrowsCarrinhoDeCompraNaoEncontradaException() {
        // given
        buscarPeloIdTestException();
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();
        // when
        assertThatThrownBy(() -> underTest.alterar(1L, carrinhoDeCompraRequest))
                .isInstanceOf(CarrinhoDeCompraNaoEncontradoException.class)
                .hasMessageContaining(CARRINHO_NOT_FOUND);
        // then
        verify(carrinhoCompraRepositoryMock, never()).saveAndFlush(any(CarrinhoDeCompra.class));
    }

    @Test
    @DisplayName("excluir() remove carrinhoDeCompra")
    void givenId_whenExcluir_thenCarrinhoDeCompraShouldBeRemoved() {
        // given
        buscarPeloIdConfig();
        // when
        underTest.excluir(1L);
        // then
        verify(carrinhoCompraRepositoryMock, times(1)).delete(any(CarrinhoDeCompra.class));
    }

    @Test
    @DisplayName("excluir() Throws CarrinhoDeCompraNaoEncontradaException when carrinhoDeCompra not found by id")
    void givenUnregisteredId_whenExcluir_thenThrowsCarrinhoDeCompraNaoEncontradaException() {
        // given
        buscarPeloIdTestException();
        // when
        assertThatThrownBy(() ->  underTest.excluir(1L))
                .isInstanceOf(CarrinhoDeCompraNaoEncontradoException.class)
                .hasMessageContaining(CARRINHO_NOT_FOUND);
        // then
        verify(carrinhoCompraRepositoryMock, never()).delete(any(CarrinhoDeCompra.class));
    }

}