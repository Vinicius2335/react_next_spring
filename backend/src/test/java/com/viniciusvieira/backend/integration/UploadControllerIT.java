package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.ProdutoImagem;
import com.viniciusvieira.backend.domain.repository.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.MarcaRepository;
import com.viniciusvieira.backend.domain.repository.ProdutoImagemRepository;
import com.viniciusvieira.backend.domain.repository.ProdutoRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Log4j2
@DisplayName("Teste de Integração para UploadController")
class UploadControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProdutoImagemRepository produtoImagemRepository;

    private final ProdutoImagem produtoImagemToSave = ProdutoImagemCreator.mockProdutoImagem();
    private static final String URL = "/api/imagens";
    private static final String PATH_DIRECTORY = "src/main/resources/static/image";

    private ProdutoImagem inserirNovoProdutoImagemNoBanco(){
        marcaRepository.saveAndFlush(MarcaCreator.mockMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());
        produtoRepository.saveAndFlush(ProdutoCreator.mockProduto());
        return produtoImagemRepository.saveAndFlush(produtoImagemToSave);
    }

    private void criarImagemParaTeste() throws IOException {
        File file = new File(PATH_DIRECTORY + "/" + produtoImagemToSave.getNome());
        file.createNewFile();
    }


    @Test
    @DisplayName("buscarTodos Return a list of produtoImagem When successful")
    void buscarTodos_ReturnListOfProdutoImagem_WhenSuccessful(){
        inserirNovoProdutoImagemNoBanco();
        ParameterizedTypeReference<List<ProdutoImagem>> typeReference = new ParameterizedTypeReference<>(){};

        ResponseEntity<List<ProdutoImagem>> response = testRestTemplate.exchange(
                URL,
                HttpMethod.GET,
                null,
                typeReference
        );

        log.info("------------------- Response Body -------------------");
        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(1, response.getBody().size())
        );
    }

    @Test
    @DisplayName("excluir Remove image When successful")
    void excluir_RemoveImagem_WhenSuccessful() throws IOException {
        criarImagemParaTeste();
        inserirNovoProdutoImagemNoBanco();

        ResponseEntity<Void> response = testRestTemplate.exchange(
                URL + "/1",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Throws ProdutoImagemNaoEncontradoException When produtoImagem not found")
    void excluir_ThrowsProdutoImagemNaoEncontradoException_WhenProdutoImagemNotFound() {
        ResponseEntity<ProdutoNaoEncontradoException> response = testRestTemplate.exchange(
                URL + "/1",
                HttpMethod.DELETE,
                null,
                ProdutoNaoEncontradoException.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(ProdutoNaoEncontradoException.class, response.getBody().getClass())
        );
    }
}