package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static com.viniciusvieira.backend.domain.service.CrudProdutoImagemService.PATH_DIRECTORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    @InjectMocks
    private ImageService underTest;

    @Mock
    private CrudProdutoImagemService crudProdutoImagemServiceMock;

    private final ProdutoImagem produtoImagem = ProdutoImagemCreator.createProdutoImagem();
    private final ProdutoImagemResponse produtoImagemResponse = ProdutoImagemCreator.createProdutoImagemResponse(produtoImagem);
    private final Produto produto = ProdutoCreator.createProduto();

    @Test
    void whenCriarMockArquivo_thenCreateMockFile() {
        MockMultipartFile mockMultipartFile = criarMockMultoPartFile();
        assertThat(mockMultipartFile.getName()).isEqualTo(produtoImagem.getNome());
    }

    @Test
    @DisplayName("dowloadImage() return image for dowload")
    void givenImageCode_whenDowloadImage_thenShouldBeReturnedImage() throws IOException {
        // given
        criarImagemTeste();
        // when
        Resource expected = underTest.dowloadImage(produtoImagem.getImageCode());
        System.out.println(expected);
        // then
        assertThat(expected).isNotNull();
    }

    @Test
    @DisplayName("dowloadImage() Throws NegocioException when imageCode not found")
    void givenImageCode_whenDowloadImage_thenThrowsNegocioException() {
        // given
        String imageCode = produtoImagem.getImageCode();
        String errorMessage = String.format("ImageCode: '%s'  não está registrado no banco de dados.", imageCode);
        // when
        assertThatThrownBy(() -> underTest.dowloadImage(imageCode))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining(errorMessage);
    }

    @Test
    @DisplayName("uploadEInseriNovaImagem() add new image")
    void givenProdutoAndImage_whenUploadEInseriNovaImagem_thenShouldBeInsertedNewImage() throws IOException {
        // given
        inserirConfig();
        MockMultipartFile file = criarMockMultoPartFile();
        // when
        ProdutoImagemResponse expected = underTest.uploadEInseriNovaImagem(produto, file);
        // then
        verify(crudProdutoImagemServiceMock, times(1))
                .inserir(any(Produto.class), anyString(), anyString());
        assertThat(expected).isNotNull();
        assertThat(expected.getNome()).isEqualTo(produtoImagem.getNome());
        assertThat(expected.getImageCode()).isEqualTo(produtoImagem.getImageCode());

        deletarImagemTest();
    }

    private void inserirConfig() {
        given(crudProdutoImagemServiceMock.inserir(any(Produto.class), anyString(),anyString()))
                .willReturn(produtoImagemResponse);
    }

    private void criarImagemTeste() throws IOException {
        String fileName = produtoImagem.getImageCode() + "-" + produtoImagem.getNome();
        File file = new File(PATH_DIRECTORY + "/" + fileName);
        file.createNewFile();
    }

    private void deletarImagemTest() {
        Path directory = Path.of(PATH_DIRECTORY);

        try (Stream<Path> files = Files.list(directory)) {
            files.forEach(file -> {
                if(file.getFileName().toString().endsWith(produtoImagem.getNome())){
                    File fileFounded = file.toFile();
                    fileFounded.delete();
                    return;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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