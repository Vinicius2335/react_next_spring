package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.ProdutoImagemMapper;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.ProdutoImagemNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoImagemRepository;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.viniciusvieira.backend.domain.service.CrudProdutoImagemService.PATH_DIRECTORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    private final ProdutoImagemResponse produtoImagemResponse = ProdutoImagemCreator.createProdutoImagemResponse(produtoImagem);

    @AfterEach
    void tearDown() {
        deletarImagemTest();
    }

    @Test
    @DisplayName("buscarPorProduto() return a list of produtoImage")
    void givenIdProduto_whenBuscarPorProduto_thenShouldBeListOfProdutoImage() throws IOException {
        // given
        findByProdutoIdConfig();
        criarImagemTeste();
        // when
        List<ProdutoImagem> expected = underTest.buscarPorProduto(1L);
        // then
        verify(produtoImagemRepositoryMock, times(1)).findByProdutoId((anyLong()));
        assertThat(expected)
                .hasSize(1)
                .contains(produtoImagem);
    }

    private void findByProdutoIdConfig(){
        given(produtoImagemRepositoryMock.findByProdutoId(anyLong())).willReturn(List.of(produtoImagem));
    }

    @Test
    @DisplayName("buscarPorProduto() NegocioException when produtoImage file not found")
    void givenIdProduto_whenBuscarPorProduto_thenNegocioException() {
        // given
        String errorMessage = "Erro ao tentar buscar imagem pelo id do produto: ";
        findByProdutoIdConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPorProduto(1L))
                .isInstanceOf(NegocioException.class)
                        .hasMessageContaining(errorMessage);
        // then
        verify(produtoImagemRepositoryMock, times(1)).findByProdutoId((anyLong()));
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
        String imageCode = RandomStringUtils.randomAlphabetic(8);
        // when
        ProdutoImagemResponse expected = underTest.inserir(produto, "nomeImagem", imageCode);
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
    @DisplayName("excluir() remove produtoImagem from database and file")
    void givenId_whenExcluir_thenProdutoImagemShouldBeRemovedFromDatabaseAndFile() throws IOException {
        // given
        findByIdConfig();
        criarImagemTeste();
        // when
        underTest.excluir(1L);
        // then
        verify(produtoImagemRepositoryMock, times(1)).delete(any(ProdutoImagem.class));
    }

    @Test
    @DisplayName("excluir() throws ProdutoImagemNaoEncontradoException when produtoImage not found by id")
    void givenUnregisteredId_whenExcluir_thenThrowsProdutoNaoEncontradoException() throws IOException {
        // given
        String errorMessage = "Imagem não encontrada";
        findByIdEmptyConfig();
        criarImagemTeste();
        // when
        assertThatThrownBy(()  -> underTest.excluir(1L))
                .isInstanceOf(ProdutoImagemNaoEncontradoException.class)
                        .hasMessageContaining(errorMessage);
        // then
        verify(produtoImagemRepositoryMock, never()).delete(any(ProdutoImagem.class));
    }

    @Test
    @DisplayName("excluir() throws NegocioException when produtoImage file not found")
    void givenId_whenExcluir_thenThrowsNegocioException(){
        // given
        String errorMessage = "Erro ao tentar excluir a imagem do produto";
        findByIdConfig();
        // when
        assertThatThrownBy(()  -> underTest.excluir(1L))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining(errorMessage);
        // then
        verify(produtoImagemRepositoryMock, never()).delete(any(ProdutoImagem.class));
    }

    private void criarImagemTeste() throws IOException {
        File file = new File(PATH_DIRECTORY + "/" +produtoImagem.getNome());
        file.createNewFile();
    }

    private void deletarImagemTest(){
        File file = new File(PATH_DIRECTORY + "/" + produtoImagem.getNome());
        if (file.exists()){
            file.delete();
        }
    }
}