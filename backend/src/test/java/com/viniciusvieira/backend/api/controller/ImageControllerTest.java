package com.viniciusvieira.backend.api.controller;


import com.viniciusvieira.backend.api.controller.imagem.ImagemController;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.ProdutoImagemNaoEncontradoException;
import com.viniciusvieira.backend.domain.service.CrudProdutoImagemService;
import com.viniciusvieira.backend.domain.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {
    @InjectMocks
    private ImagemController underTest;

    @Mock
    private CrudProdutoImagemService mockCrudProdutoImagemService;
    @Mock
    private ImageService mockImageService;

    @Test
    @DisplayName("excluir() remove image")
    void givenId_whenExcluir_thenShouldBeRemovedImage(){
        // given
        doNothing().when(mockCrudProdutoImagemService).excluir(anyLong());
        // when
        ResponseEntity<Void> expected = underTest.excluir(1L);
        // then
        verify(mockCrudProdutoImagemService, times(1)).excluir(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("excluir() Throws NegocioException when image file not found")
    void givenId_whenExcluir_thenThrowNegocioException(){
        // given
        String errorMessage = "Erro ao tentar excluir a imagem do produto";
        doThrow(new NegocioException(errorMessage)).when(mockCrudProdutoImagemService).excluir(anyLong());
        // when
        assertThatThrownBy(() -> underTest.excluir(1L))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining(errorMessage);
    }

    @Test
    @DisplayName("excluir() Throws ProdutoImagemNaoEncontradoException when image not found by id")
    void givenUnregisteredId_whenExcluir_thenThrowProdutoImagemNaoEncontradoException(){
        // given
        String errorMessage = "Imagem não encontrada";
        doThrow(new ProdutoImagemNaoEncontradoException(errorMessage)).when(mockCrudProdutoImagemService).excluir(anyLong());
        // when
        assertThatThrownBy(() -> underTest.excluir(1L))
                .isInstanceOf(ProdutoImagemNaoEncontradoException.class)
                .hasMessageContaining(errorMessage);
    }
}