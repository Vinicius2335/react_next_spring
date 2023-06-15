package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.EstadoRequest;
import com.viniciusvieira.backend.api.representation.model.response.EstadoResponse;
import com.viniciusvieira.backend.domain.exception.EstadoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Estado;
import com.viniciusvieira.backend.domain.repository.EstadoRepository;
import com.viniciusvieira.backend.util.EstadoCreator;
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

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DisplayName("Teste Integração para EstadoController")
class EstadoControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private EstadoRepository estadoRepository;

    private static final String URL = "/api/estados";

    private Estado inserirNovaEstadoNoBanco() {
        Estado novaEstado = EstadoCreator.mockEstado();
        return estadoRepository.saveAndFlush(novaEstado);
    }

    @Test
    @DisplayName("buscarTodos Return statusCode 200 and list of estados When successful")
    void buscarTodos_ReturnReturnStatusCode200AndListEstados_WhenSuccessful() {
        Estado estadoSaved = inserirNovaEstadoNoBanco();
        ParameterizedTypeReference<List<Estado>> typeReference = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Estado>> response = testRestTemplate.exchange(
                URL,
                GET,
                null,
                typeReference
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(estadoSaved.getId(), response.getBody().get(0).getId()),
                () -> assertEquals(estadoSaved.getNome(), response.getBody().get(0).getNome()),
                () -> assertEquals(estadoSaved.getSigla(), response.getBody().get(0).getSigla())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 201 and new estadoResponse When successful")
    void inserir_ReturnStatusCode201AndEstadoResponse_WhenSuccessful() {
        EstadoRequest novaEstado = EstadoCreator.mockEstadoRequestToSave();
        ResponseEntity<EstadoResponse> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaEstado),
                EstadoResponse.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(novaEstado.getNome(), response.getBody().getNome()),
                () -> assertEquals(novaEstado.getSigla(), response.getBody().getSigla())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When estado have invalid fields")
    void inserir_ReturnStatusCode400_WhenEstadoHaveInvalidFields() {
        EstadoRequest novaEstado = EstadoCreator.mockInvalidEstadoRequest();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaEstado),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 200 and estadoResponse changed When successful")
    void alterar_ReturnStatusCode200AndChangedEstadoResponse_WhenSuccessful() {
        Estado novaEstado = inserirNovaEstadoNoBanco();
        Estado estadoParaAlterar = EstadoCreator.mockEstadoToUpdate(novaEstado.getDataCriacao());

        ResponseEntity<EstadoResponse> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(estadoParaAlterar),
                EstadoResponse.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(estadoParaAlterar.getNome(), response.getBody().getNome()),
                () -> assertEquals(estadoParaAlterar.getSigla(), response.getBody().getSigla())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 400 When estado have invalid fields")
    void alterar_ReturnStatusCode400_WhenEstadoHaveInvalidFields() {
        inserirNovaEstadoNoBanco();
        EstadoRequest estadoParaAlterar = EstadoCreator.mockInvalidEstadoRequest();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(estadoParaAlterar),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return statusCode 404 When estado not found")
    void alterar_ReturnStatusCode404_WhenEstadoNotFound() {
        Estado novaEstado = inserirNovaEstadoNoBanco();
        Estado estadoParaAlterar = EstadoCreator.mockEstadoToUpdate(novaEstado.getDataCriacao());

        ResponseEntity<EstadoNaoEncontradoException> response = testRestTemplate.exchange(
                URL + "/99",
                PUT,
                new HttpEntity<>(estadoParaAlterar),
                EstadoNaoEncontradoException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(EstadoNaoEncontradoException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("excluir Return statusCode 204 when successful")
    void excluir_ReturnStatusCode204_WhenSuccessful() {
        inserirNovaEstadoNoBanco();
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
    @DisplayName("excluir Return statusCode 404 When estado not found")
    void excluir_ReturnStatusCode404_WhenEstadoNotFound() {
        inserirNovaEstadoNoBanco();
        ResponseEntity<EstadoNaoEncontradoException> response = testRestTemplate.exchange(
                URL + "/2",
                DELETE,
                null,
                EstadoNaoEncontradoException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(EstadoNaoEncontradoException.class, response.getBody().getClass())
        );
    }
}