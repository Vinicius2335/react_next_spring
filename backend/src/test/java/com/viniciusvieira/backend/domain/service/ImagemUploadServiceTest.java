package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@Log4j2
@DisplayName("Teste Unitário para ImagemUploadService")
class ImagemUploadServiceTest {
    @InjectMocks
    private ImagemUploadService imagemUploadService;

    @Mock
    private CrudProdutoImagemService mockCrudProdutoImagemService;

    @BeforeEach
    void setUp(){
        // inserir
        BDDMockito.when(mockCrudProdutoImagemService.inserir(any(Produto.class), anyString()))
                .thenReturn(ProdutoImagemCreator.mockProdutoImagemResponse());
        // alterar
        BDDMockito.when(mockCrudProdutoImagemService.alterar(anyLong(), anyString()))
                .thenReturn(ProdutoImagemCreator.mockProdutoImagemResponse());
    }

    private MockMultipartFile criarMockMultoPartFile(){
        return new MockMultipartFile(
                "file",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem".getBytes()
        );
    }

    @Test
    @DisplayName("uploadEInseriNovaImagem Upload and save image When successful")
    void uploadEInseriNovaImagem_UploadAndSaveImage_WhenSuccessful() throws IOException {
        Produto produto = ProdutoCreator.mockProduto();
        MockMultipartFile file = criarMockMultoPartFile();

        ProdutoImagemResponse produtoImagemResponse = imagemUploadService.uploadEInseriNovaImagem(produto, file);

        log.info(produtoImagemResponse);

        assertAll(
                () -> assertNotNull(produtoImagemResponse),
                () -> assertTrue(produtoImagemResponse.getNome().endsWith("image.png"))
        );
    }

    @Test
    @DisplayName("uploadEAlteraImagem Upload and update image When successful")
    void uploadEAlteraImagem_UploadAndUpdateImage_WhenSuccessful() throws IOException {
        MockMultipartFile file = criarMockMultoPartFile();
        ProdutoImagemResponse produtoImagemResponse = imagemUploadService.uploadEAlteraImagem(1L, file);

        assertAll(
                () -> assertNotNull(produtoImagemResponse),
                () -> assertTrue(produtoImagemResponse.getNome().endsWith("image.png"))
        );
    }

    @Test
    void testeStringEndWith(){
        String exemplo = "sjdjsidjsodajimage.png";
        String exemplo2 = "sjdjsidjsodaj";

        assertAll(
                () -> assertTrue(exemplo.endsWith("image.png")),
                () -> assertFalse(exemplo2.endsWith("image.png"))
        );
    }
}