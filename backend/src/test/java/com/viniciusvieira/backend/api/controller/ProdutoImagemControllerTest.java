package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.controller.imagem.ProdutoImagemController;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.exception.venda.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.service.CrudProdutoImagemService;
import com.viniciusvieira.backend.domain.service.ImageService;
import com.viniciusvieira.backend.domain.service.venda.CrudProdutoService;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoImagemControllerTest {
    @InjectMocks
    private ProdutoImagemController underTest;

    @Mock
    private ImageService mockImageService;
    @Mock
    private CrudProdutoService mockCrudProdutoService;
    @Mock
    private CrudProdutoImagemService mockCrudProdutoImagemService;

    private final ProdutoImagem produtoImagem = ProdutoImagemCreator.createProdutoImagem();
    private final ProdutoImagemResponse produtoImagemResponse = ProdutoImagemCreator.createProdutoImagemResponse(produtoImagem);
    private final Produto produto = ProdutoCreator.createProduto();

    @Test
    @DisplayName("buscarImagensPorProduto() return a list of produtoImage")
    void givenIdProduto_whenBuscarImagensPorProduto_thenShouldBeReturnedListProdutoImage() {
        // given
        buscarPorProdutoConfig();
        // when
        ResponseEntity<List<ProdutoImagem>> expected = underTest.buscarImagensPorProduto(1L);
        // then
        verify(mockCrudProdutoImagemService, times(1)).buscarPorProduto(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .hasSize(1)
                .contains(produtoImagem);
    }

    private void buscarPorProdutoConfig(){
        given(mockCrudProdutoImagemService.buscarPorProduto(anyLong())).willReturn(List.of(produtoImagem));
    }

    @Test
    @DisplayName("uploadFile() add new image file")
    void givenIdProdutoAndFile_whenUploadFile_thenShouldBeAddNewImage() throws IOException {
        // given
        uploadFileConfig();
        MockMultipartFile file = criarMockMultoPartFile();
        // when
        ResponseEntity<ProdutoImagemResponse> expected = underTest.uploadFile(1L, file);
        // then
        verify(mockImageService, times(1)).uploadEInseriNovaImagem(any(Produto.class), any(MultipartFile.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(produtoImagemResponse);
    }

    private void uploadFileConfig() throws IOException {
        given(mockCrudProdutoService.buscarPorId(anyLong())).willReturn(produto);
        given(mockImageService.uploadEInseriNovaImagem(any(Produto.class), any(MultipartFile.class)))
                .willReturn(produtoImagemResponse);
    }

    @Test
    @DisplayName("uploadFile() Throws ProdutoNaoEncontradoException when produto not found by id")
    void givenUnregisteredIdProduto_whenUploadFile_thenThrowsProdutoNaoEncontradoException() throws IOException {
        // given
        String errorMessage = "Produto não encontrado";
        doThrow(new ProdutoNaoEncontradoException(errorMessage)).when(mockCrudProdutoService).buscarPorId(anyLong());
        MockMultipartFile file = criarMockMultoPartFile();
        // when
        assertThatThrownBy(() -> underTest.uploadFile(1L, file))
                .isInstanceOf(ProdutoNaoEncontradoException.class)
                        .hasMessageContaining(errorMessage);
        // then
        verify(mockCrudProdutoService, times(1)).buscarPorId(anyLong());
        verify(mockImageService, never()).uploadEInseriNovaImagem(any(Produto.class), any(MultipartFile.class));
    }

    private MockMultipartFile criarMockMultoPartFile() {
        return new MockMultipartFile(
                produtoImagem.getNome(),
                produtoImagem.getNome(),
                MediaType.IMAGE_PNG_VALUE,
                "Imagem".getBytes()
        );
    }
}