package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.domain.exception.ProdutoImagemNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.service.CrudProdutoImagemService;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import lombok.extern.log4j.Log4j2;
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
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
@Log4j2
@DisplayName("Teste Unitário para UploadController")
class UploadControllerTest {
    @InjectMocks
    private UploadController uploadController;

    @Mock
    private CrudProdutoImagemService mockCrudProdutoImagemService;

    private final ProdutoImagem expectedProdutoImagem = ProdutoImagemCreator.mockProdutoImagem();

    @BeforeEach
    void setUp(){
        // CrudProdutoImagemService
        // buscarTodos
        BDDMockito.when(mockCrudProdutoImagemService.buscarTodos()).thenReturn(List.of(expectedProdutoImagem));
        // excluir
        BDDMockito.doNothing().when(mockCrudProdutoImagemService).excluir(anyLong());
    }

    @Test
    @DisplayName("buscarTodos Return list of produtoImagem when successful")
    void buscarTodos_ReturnListProdutoImagem_WhenSuccessful(){
        ResponseEntity<List<ProdutoImagem>> response = uploadController.buscarTodos();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(1, response.getBody().size()),
                () -> assertEquals(expectedProdutoImagem.getNome(), response.getBody().get(0).getNome())
        );
    }

    @Test
    @DisplayName("excluir Remove imagem When successful")
    void excluir_RemoveImagem_WhenSuccessful(){
        ResponseEntity<Void> response = uploadController.excluir(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Thorws ProdutoImagemNaoEncontradoException When produtoImagem not found")
    void excluir_ThrowsProdutoImagemNaoEncontradoException_WhenProdutoImagemNotFound(){
        BDDMockito.doThrow(ProdutoImagemNaoEncontradoException.class).when(mockCrudProdutoImagemService).excluir(anyLong());

        assertThrows(ProdutoImagemNaoEncontradoException.class, () -> uploadController.excluir(99L));
    }
}