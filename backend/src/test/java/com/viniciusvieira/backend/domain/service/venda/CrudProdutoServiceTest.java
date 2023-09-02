package com.viniciusvieira.backend.domain.service.venda;

import com.viniciusvieira.backend.api.mapper.venda.ProdutoMapper;
import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.exception.venda.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoRepository;
import com.viniciusvieira.backend.util.ProdutoCreator;
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
class CrudProdutoServiceTest {
    @InjectMocks
    private CrudProdutoService underTest;

    @Mock
    private ProdutoRepository produtoRepositoryMock;
    @Mock
    private ProdutoMapper produtoMapperMock;

    private final Produto produto = ProdutoCreator.createProduto();
    private static final String PRODUTO_NOT_FOUND = "Produto não encontrado";

    @Test
    @DisplayName("buscarTodos() return list of produto")
    void whenBuscarTodos_thenProdutosShouldBeFound() {
        // given
        given(produtoRepositoryMock.findAll()).willReturn(List.of(produto));
        // when
        List<Produto> expected = underTest.buscarTodos();
        // then
        verify(produtoRepositoryMock, times(1)).findAll();
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .contains(produto);
    }

    @Test
    @DisplayName("buscarPorId() return produto by id")
    void givenId_whenBuscarPorId_thenProdutoShoudBeFound() {
        // given
        buscarPorIdConfig();
        // then
        Produto expected = underTest.buscarPorId(1L);
        // when
        verify(produtoRepositoryMock, times(1)).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .isEqualTo(produto);
    }

    private void buscarPorIdConfig() {
        given(produtoRepositoryMock.findById(anyLong())).willReturn(Optional.of(produto));
    }

    @Test
    @DisplayName("buscarPorId() Throws ProdutoNaoEncontradoException when produto not found by id")
    void givenUnregisteredId_whenBuscarPorId_thenThrowsProdutoNaoEncontradoException() {
        // given
        buscarPorIdTestException();
        // then
        assertThatThrownBy(() -> underTest.buscarPorId(1L))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                        .hasMessageContaining(PRODUTO_NOT_FOUND);
        // when
        verify(produtoRepositoryMock, times(1)).findById(anyLong());
    }

    private void buscarPorIdTestException() {
        given(produtoRepositoryMock.findById(anyLong())).willReturn(Optional.empty());
    }

    @Test
    @DisplayName("inserir() insert new produto")
    void givenProduto_whenInserir_thenProdutoShouldBeInserted() {
        // given
        inserirConfig();
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);
        // when
        ProdutoResponse expected = underTest.inserir(produtoRequest);
        // then
        verify(produtoRepositoryMock, times(1)).saveAndFlush(any(Produto.class));
        assertThat(expected).isNotNull();
        assertThat(expected.getDescricaoCurta()).isEqualTo(produtoRequest.getDescricaoCurta());
    }

    private void inserirConfig(){
        ProdutoResponse produtoResponse = ProdutoCreator.createProdutoResponse(produto);
        given(produtoMapperMock.toDomainProduto(any(ProdutoRequest.class))).willReturn(produto);
        given(produtoRepositoryMock.saveAndFlush(any(Produto.class))).willReturn(produto);
        given(produtoMapperMock.toProdutoResponse(any(Produto.class))).willReturn(produtoResponse);
    }

    @Test
    @DisplayName("alterar() update produto")
    void givenProduto_whenAlterar_thenProdutoShouldBeUpdated() {
        // given
        alterarConfig();
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);
        // when
        ProdutoResponse expected = underTest.alterar(1L, produtoRequest);
        // then
        verify(produtoRepositoryMock, times(1)).saveAndFlush(any(Produto.class));
        assertThat(expected).isNotNull();
        assertThat(expected.getDescricaoCurta()).isEqualTo(produtoRequest.getDescricaoCurta());
    }

    private void alterarConfig(){
        buscarPorIdConfig();
        inserirConfig();
    }

    @Test
    @DisplayName("alterar() Throws ProdutoNaoEncontradaException when produto not found by id")
    void givenProduto_whenAlterar_thenThrowsProdutoNaoEncontradaException() {
        // given
        buscarPorIdTestException();
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);
        // when
        assertThatThrownBy(() -> underTest.alterar(1L, produtoRequest))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                        .hasMessageContaining(PRODUTO_NOT_FOUND);
        // then
        verify(produtoRepositoryMock, never()).saveAndFlush(any(Produto.class));
    }

    @Test
    @DisplayName("excluir() remove produto")
    void givenId_whenExcluir_thenProdutoShouldBeRemoved() {
        // given
        buscarPorIdConfig();
        // when
        underTest.excluir(1L);
        // then
        verify(produtoRepositoryMock, times(1)).delete(any(Produto.class));
    }

    @Test
    @DisplayName("excluir() Throws ProdutoNaoEncontradaException when produto not found by id")
    void givenUnregisteredId_whenExcluir_thenThrowsProdutoNaoEncontradaException() {
        // given
        buscarPorIdTestException();
        // when
       assertThatThrownBy(() ->  underTest.excluir(1L))
               .isInstanceOf(ProdutoNaoEncontradoException.class)
                       .hasMessageContaining(PRODUTO_NOT_FOUND);
        // then
        verify(produtoRepositoryMock, never()).delete(any(Produto.class));
    }

    @Test
    @DisplayName("excluirTodosProdutosRelacionadosMarcaId() delete all produtos related to marcaId")
    void givenMarcaId_whenExcluirTodosProdutosRelacionadosMarcaId_thenProdutosShouldBeRemoved() {
        // given
        given(produtoRepositoryMock.findAllProdutosByMarcaId(anyLong())).willReturn(List.of(produto));
        // when
        underTest.excluirTodosProdutosRelacionadosMarcaId(1L);
        // then
        verify(produtoRepositoryMock, times(1)).deleteAll(anyIterable());
    }

    @Test
    @DisplayName("excluirTodosProdutosRelacionadosCategoriaId() delete all produtos related to categoriaId")
    void givenCategoriaId_whenExcluirTodosProdutosRelacionadosCategoriaId_thenProdutosShouldBeRemoved() {
        // given
        given(produtoRepositoryMock.findAllProdutosByCategoriaId(anyLong())).willReturn(List.of(produto));
        // when
        underTest.excluirTodosProdutosRelacionadosCategoriaId(1L);
        // then
        verify(produtoRepositoryMock, times(1)).deleteAll(anyIterable());
    }
}