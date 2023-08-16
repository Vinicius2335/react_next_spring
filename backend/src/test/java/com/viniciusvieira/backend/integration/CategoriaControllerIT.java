package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.venda.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CategoriaResponse;
import com.viniciusvieira.backend.domain.exception.CategoriaNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;

@Log4j2
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DisplayName("Teste Integração para CategoriaController")
class CategoriaControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private static final String URL = "/api/categorias";

    private Categoria inserirNovaCategoriaNoBanco() {
        Categoria novaCategoria = CategoriaCreator.mockCategoria();
        return categoriaRepository.saveAndFlush(novaCategoria);
    }

    @Test
    @DisplayName("buscarTodos Return statusCode 200 and list of categorias When successful")
    void buscarTodos_ReturnStatusCode200AndListCategorias_WhenSuccessful() {
        Categoria categoriaSaved = inserirNovaCategoriaNoBanco();
        ParameterizedTypeReference<List<Categoria>> typeReference = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Categoria>> response = testRestTemplate.exchange(
                URL,
                GET,
                null,
                typeReference
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(categoriaSaved.getId(), response.getBody().get(0).getId()),
                () -> assertEquals(categoriaSaved.getNome(), response.getBody().get(0).getNome())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 201 and new categoriaResponse When successful")
    void inserir_InsertNewCategoriaResponseAndReturn201_WhenSuccessful() {
        CategoriaRequest novaCategoria = CategoriaCreator.mockCategoriaRequest();
        ResponseEntity<CategoriaResponse> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaCategoria),
                CategoriaResponse.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(novaCategoria.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When category have invalid fields")
    void inserir_ReturnStatusCode400_WhenCategoryHaveInvalidFields() {
        CategoriaRequest novaCategoria = CategoriaCreator.mockInvalidCategoriaRequest();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaCategoria),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 200 and categoriaResponse changed when successful")
    void alterar_ReturnStatusCode200AndChangedCategoria_WhenSuccessful() {
        inserirNovaCategoriaNoBanco();
        CategoriaRequest categoriaParaAlterar = CategoriaCreator.mockCategoriaRequestToUpdate();

        ResponseEntity<CategoriaResponse> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(categoriaParaAlterar),
                CategoriaResponse.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(categoriaParaAlterar.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 400 when categoria have invalid fields")
    void alterar_ReturnStatusCode400_WhenCategoriaHaveInvalidFields() {
        inserirNovaCategoriaNoBanco();
        CategoriaRequest categoriaParaAlterar = CategoriaCreator.mockInvalidCategoriaRequest();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(categoriaParaAlterar),
                Object.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return statusCode 404 When categoria not found")
    void alterar_ReturnStatusCode404_WhenCategoriaNotFound() {
        Categoria novaCategoria = inserirNovaCategoriaNoBanco();
        Categoria categoriaParaAlterar = CategoriaCreator.mockCategoriaToUpdated(novaCategoria.getDataCriacao());

        ResponseEntity<CategoriaNaoEncontradoException> response = testRestTemplate.exchange(
                URL + "/99",
                PUT,
                new HttpEntity<>(categoriaParaAlterar),
                CategoriaNaoEncontradoException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(CategoriaNaoEncontradoException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("excluir Return statusCode 204 when successful")
    void excluir_ReturnStatusCode204_WhenSuccessful() {
        inserirNovaCategoriaNoBanco();
        ResponseEntity<Void> response = testRestTemplate.exchange(
                URL + "/1",
                DELETE,
                null,
                Void.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Return statusCode 404 When categoria not found")
    void excluir_ReturnStatusCode404_WhenCategoriaNotFound() {
        inserirNovaCategoriaNoBanco();
        ResponseEntity<CategoriaNaoEncontradoException> response = testRestTemplate.exchange(
                URL + "/2",
                DELETE,
                null,
                CategoriaNaoEncontradoException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(CategoriaNaoEncontradoException.class, response.getBody().getClass())
        );
    }
}