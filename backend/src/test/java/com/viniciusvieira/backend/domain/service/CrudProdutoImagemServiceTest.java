package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.ProdutoImagemMapper;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.exception.ProdutoImagemNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoImagemRepository;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CrudProdutoImagemServiceTest {
    @InjectMocks
    private CrudProdutoImagemService underTest;

    @Mock
    private ProdutoImagemRepository produtoImagemRepositoryMock;
    @Mock
    private ProdutoImagemMapper produtoImagemMapperMock;

    private final Produto produto = ProdutoCreator.createProduto();
    private final ProdutoImagem produtoImagem = ProdutoImagemCreator.createProdutoImagem();
    private final ProdutoImagemResponse produtoImagemResponse = ProdutoImagemCreator.createProdutoImagemResponse();

    private MockMultipartFile criarMockMultoPartFile(){
        return new MockMultipartFile(
                "file",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem".getBytes()
        );
    }

    @Test
    @DisplayName("buscarTodos() return list produtoImagem")
    void whenBuscarTodos_thenListProdutoImagemShouldBeFound() {
        // given
        given(produtoImagemRepositoryMock.findAll()).willReturn(List.of(produtoImagem));
        // when
        List<ProdutoImagem> expected = underTest.buscarTodos();
        // then
        verify(produtoImagemRepositoryMock, times(1)).findAll();
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .contains(produtoImagem);
    }

    @Test
    @DisplayName("buscarPorId() return produtoImagem")
    void givenId_whenBuscarPorId_thenProdutoImagemShoudlBeFound() {
        // given
        findByIdConfig();
        // when
        ProdutoImagem expected = underTest.buscarPorId(1L);
        // then
        verify(produtoImagemRepositoryMock, times(1)).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .isEqualTo(produtoImagem);
    }

    private void findByIdConfig(){
        given(produtoImagemRepositoryMock.findById(anyLong())).willReturn(Optional.of(produtoImagem));
    }

    @Test
    @DisplayName("buscarPorId() throws ProdutoImagemNaoEncontradoException when produtoImagem not found by id")
    void givenUnregisteredId_whenBuscarPorId_thenThrowsProdutoImagemNaoEncontradoException() {
        // given
        findByIdEmptyConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPorId(1L))
                .isInstanceOf(ProdutoImagemNaoEncontradoException.class)
                        .hasMessageContaining("Imagem não encontrada");
        // then
        verify(produtoImagemRepositoryMock, times(1)).findById(anyLong());
    }

    private void findByIdEmptyConfig(){
        given(produtoImagemRepositoryMock.findById(anyLong())).willReturn(Optional.empty());
    }

    @Test
    @DisplayName("inserir() add new produtoImagem")
    void givenProdutoAndNomeImagem_whenInserir_thenProdutoImagemShouldBeInserted() {
        // given
        inserirConfig();
        // when
        ProdutoImagemResponse expected = underTest.inserir(produto, "nomeImagem");
        // then
        verify(produtoImagemRepositoryMock, times(1)).saveAndFlush(any(ProdutoImagem.class));
        assertThat(expected)
                .isNotNull()
                .isEqualTo(produtoImagemResponse);
    }

    private void inserirConfig(){
        given(produtoImagemRepositoryMock.saveAndFlush(any(ProdutoImagem.class))).willReturn(produtoImagem);
        given(produtoImagemMapperMock.toProdutoImagemResponse(any(ProdutoImagem.class)))
                .willReturn(produtoImagemResponse);
    }

    @Test
    @Disabled
    @DisplayName("alterar() update produtoImagem")
    void givenIdAndNomeImagem_whenAlterar_thenProdutoImagemShouldBeUpdated() {
    }

    @Test
    @Disabled
    @DisplayName("excluir")
    void excluir() {
    }
}