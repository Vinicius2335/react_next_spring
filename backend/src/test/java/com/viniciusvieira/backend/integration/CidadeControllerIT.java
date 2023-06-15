package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.CidadeRequest;
import com.viniciusvieira.backend.api.representation.model.response.CidadeResponse;
import com.viniciusvieira.backend.domain.exception.CidadeNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Cidade;
import com.viniciusvieira.backend.domain.repository.CidadeRepository;
import com.viniciusvieira.backend.domain.repository.EstadoRepository;
import com.viniciusvieira.backend.util.CidadeCreator;
import com.viniciusvieira.backend.util.EstadoCreator;
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

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DisplayName("Teste de integração para a classe CidadeController")
@Log4j2
class CidadeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    private final String url = "/api/cidades";
    private final CidadeResponse expectedCidade = CidadeCreator.mockCidadeResponse();
    private final CidadeResponse expectedCidadeUpdate = CidadeCreator.mockCidadeResponseUpdated();

    private Cidade inserirNovaCidade(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        return cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
    }

    @Test
    @DisplayName("buscarTodos Return statusCode 200 and list of cidades When successful")
    void buscarTodos_ReturnStatusCode200AndListCidades_WhenSuccessful() {
        Cidade cidadeSalva = inserirNovaCidade();
        ParameterizedTypeReference<List<Cidade>> typeReference = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Cidade>> response = testRestTemplate.exchange(
                url,
                GET,
                null,
                typeReference
        );

        log.info("Response -> {}", response.getBody());
        log.info("Cidade Salva -> {}", cidadeSalva);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(cidadeSalva.getId(), response.getBody().get(0).getId()),
                () -> assertEquals(cidadeSalva.getNome(), response.getBody().get(0).getNome()),
                () -> assertEquals(cidadeSalva.getEstado().getNome(), response.getBody().get(0).getEstado().getNome())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 201 and new cidadeResponse When successful")
    void inserir_InsertNewCidadeResponseAndReturn201_WhenSuccessful() {
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        CidadeRequest novaCidade = CidadeCreator.mockCidadeRequestToSave();

        ResponseEntity<CidadeResponse> response = testRestTemplate.exchange(
                url,
                POST,
                new HttpEntity<>(novaCidade),
                CidadeResponse.class
        );

        log.info("Cidade -> {}", response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(expectedCidade.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When cidade have invalid fields")
    void inserir_ReturnStatusCode400_WhenCidadeHaveInvalidFields() {
        CidadeRequest cidadeInvalida = CidadeCreator.mockInvalidCidadeRequestToSave();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                url,
                POST,
                new HttpEntity<>(cidadeInvalida),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 200 and cidadeResponse changed when successful")
    void alterar_ReturnStatusCode200AndChangedCidade_WhenSuccessful() {
        inserirNovaCidade();

        CidadeRequest cidadeParaAlterar = CidadeCreator.mockCidadeRequestToUpdate();
        ResponseEntity<CidadeResponse> response = testRestTemplate.exchange(
                url + "/1",
                PUT,
                new HttpEntity<>(cidadeParaAlterar),
                CidadeResponse.class
        );

        log.info(" - ");
        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedCidadeUpdate.getNome(), response.getBody().getNome()),
                () -> assertEquals(expectedCidadeUpdate.getNomeEstado(), response.getBody().getNomeEstado())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 400 when cidade have invalid fields")
    void alterar_ReturnStatusCode400_WhenCidadeHaveInvalidFields() {
        inserirNovaCidade();

        CidadeRequest cidadeInvalida = CidadeCreator.mockInvalidCidadeRequestToSave();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                url + "/1",
                PUT,
                new HttpEntity<>(cidadeInvalida),
                Object.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    // TUDO OK
    @Test
    @DisplayName("alterar Return statusCode 404 When cidade not found")
    void alterar_ReturnStatusCode404_WhenCidadeNotFound() {
        CidadeRequest cidadeParaAlterara = CidadeCreator.mockCidadeRequestToUpdate();
        ResponseEntity<CidadeNaoEncontradaException> response = testRestTemplate.exchange(
                url + "/99",
                PUT,
                new HttpEntity<>(cidadeParaAlterara),
                CidadeNaoEncontradaException.class
        );

        log.info("Response -> ", response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(CidadeNaoEncontradaException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("excluir Remove a cidade and return statusCode 204 when successful")
    void excluir_RemoveCidadeAndReturnStatusCode204_WhenSuccessful() {
        inserirNovaCidade();

        ResponseEntity<Void> response = testRestTemplate.exchange(
                url + "/1",
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
    @DisplayName("excluir Return statusCode 404 When cidade not found")
    void excluir_ReturnStatusCode404_WhenCidadeNotFound() {
        inserirNovaCidade();

        ResponseEntity<CidadeNaoEncontradaException> response = testRestTemplate.exchange(
                url + "999",
                DELETE,
                null,
                CidadeNaoEncontradaException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(CidadeNaoEncontradaException.class, response.getBody().getClass())
        );
    }
}