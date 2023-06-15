package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.MarcaResponse;
import com.viniciusvieira.backend.domain.exception.MarcaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Marca;
import com.viniciusvieira.backend.domain.repository.MarcaRepository;
import com.viniciusvieira.backend.util.MarcaCreator;
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
@DisplayName("Teste Integração para MarcaController")
class MarcaControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private MarcaRepository marcaRepository;

    private static final String URL = "/api/marcas";

    private Marca inserirNovaMarcaNoBanco() {
        Marca novaMarca = MarcaCreator.mockMarca();
        return marcaRepository.saveAndFlush(novaMarca);
    }

    @Test
    @DisplayName("buscarTodos Return statusCode 200 and list of marcas When successful")
    void buscarTodos_ReturnReturnStatusCode200AndListMarcas_WhenSuccessful() {
        Marca marcaSaved = inserirNovaMarcaNoBanco();
        ParameterizedTypeReference<List<Marca>> typeReference = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Marca>> response = testRestTemplate.exchange(
                URL,
                GET,
                null,
                typeReference
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(marcaSaved.getId(), response.getBody().get(0).getId()),
                () -> assertEquals(marcaSaved.getNome(), response.getBody().get(0).getNome())
        );
    }

    @Test
    @DisplayName("buscarPeloId Return statusCode 200 and marca When successful")
    void buscarPeloId_ReturnStatusCode200AndMarca_WhenSuccessful() {
        Marca novaMarca = inserirNovaMarcaNoBanco();
        ResponseEntity<Marca> response = testRestTemplate.exchange(URL + "/1", GET, null, Marca.class);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(novaMarca.getId(), response.getBody().getId()),
                () -> assertEquals(novaMarca.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("buscarPeloId Return statusCode 404 When marca not found")
    void buscarPeloId_ReturnStatusCode404_WhenMarcaNotFound() {
        inserirNovaMarcaNoBanco();
        ResponseEntity<MarcaNaoEncontradaException> response = testRestTemplate.exchange(
                URL + "/2",
                GET,
                null,
                MarcaNaoEncontradaException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(MarcaNaoEncontradaException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 201 and new marcaResponse When successful")
    void inserir_ReturnStatusCode201AndMarcaResponse_WhenSuccessful() {
        MarcaRequest novaMarca = MarcaCreator.mockMarcaRequestToSave();
        ResponseEntity<MarcaResponse> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaMarca),
                MarcaResponse.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(novaMarca.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When marca have invalid fields")
    void inserir_ReturnStatusCode400_WhenMarcaHaveInvalidFields() {
        MarcaRequest novaMarca = MarcaCreator.mockInvalidMarcaRequestToSave();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaMarca),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 200 and changed marcaResponse when successful")
    void alterar_ReturnStatusCode200AndChangedMarcaResponse_WhenSuccessful() {
        Marca novaMarca = inserirNovaMarcaNoBanco();
        Marca marcaParaAlterar = MarcaCreator.mockMarcaToUpdate(novaMarca.getDataCriacao());

        ResponseEntity<MarcaResponse> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(marcaParaAlterar),
                MarcaResponse.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(marcaParaAlterar.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 400 when marca have invalid fields")
    void alterar_ReturnStatusCode400_WhenMarcaHaveInvalidFields() {
        inserirNovaMarcaNoBanco();
        MarcaRequest marcaParaAlterar = MarcaCreator.mockInvalidMarcaRequestToSave();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(marcaParaAlterar),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return statusCode 404 When marca not found")
    void alterar_ReturnStatusCode404_WhenMarcaNotFound() {
        Marca novaMarca = inserirNovaMarcaNoBanco();
        Marca marcaParaAlterar = MarcaCreator.mockMarcaToUpdate(novaMarca.getDataCriacao());

        ResponseEntity<MarcaNaoEncontradaException> response = testRestTemplate.exchange(
                URL + "/99",
                PUT,
                new HttpEntity<>(marcaParaAlterar),
                MarcaNaoEncontradaException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(MarcaNaoEncontradaException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("excluir Return statusCode 204 when successful")
    void excluir_ReturnStatusCode204_WhenSuccessful() {
        inserirNovaMarcaNoBanco();
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
    @DisplayName("excluir Return statusCode 404 When marca not found")
    void excluir_ReturnStatusCode404_WhenMarcaNotFound() {
        inserirNovaMarcaNoBanco();
        ResponseEntity<MarcaNaoEncontradaException> response = testRestTemplate.exchange(
                URL + "/2",
                DELETE,
                null,
                MarcaNaoEncontradaException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(MarcaNaoEncontradaException.class, response.getBody().getClass())
        );
    }
}