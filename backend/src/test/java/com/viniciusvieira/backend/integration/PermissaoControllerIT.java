package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PermissaoResponse;
import com.viniciusvieira.backend.domain.exception.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
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
@DisplayName("Teste Integração para PermissaoController")
class PermissaoControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private PermissaoRepository permissaoRepository;

    private static final String URL = "/api/permissoes";

    private Permissao inserirNovaPermissaoNoBanco() {
        Permissao novaPermissao = PermissaoCreator.mockPermissao();
        return permissaoRepository.saveAndFlush(novaPermissao);
    }

    @Test
    @DisplayName("buscarTodos Return statusCode 200 and list of permissaos When successful")
    void buscarTodos_ReturnReturnStatusCode200AndListPermissaos_WhenSuccessful() {
        Permissao permissaoSaved = inserirNovaPermissaoNoBanco();
        ParameterizedTypeReference<List<Permissao>> typeReference = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Permissao>> response = testRestTemplate.exchange(
                URL,
                GET,
                null,
                typeReference
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(permissaoSaved.getId(), response.getBody().get(0).getId()),
                () -> assertEquals(permissaoSaved.getNome(), response.getBody().get(0).getNome())
        );
    }

    @Test
    @DisplayName("buscarPeloId Return statusCode 200 and permissao When successful")
    void buscarPeloId_ReturnStatusCode200AndPermissao_WhenSuccessful() {
        Permissao novaPermissao = inserirNovaPermissaoNoBanco();
        ResponseEntity<Permissao> response = testRestTemplate.exchange(URL + "/1", GET, null, Permissao.class);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(novaPermissao.getId(), response.getBody().getId()),
                () -> assertEquals(novaPermissao.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("buscarPeloId Return statusCode 404 When permissao not found")
    void buscarPeloId_ReturnStatusCode404_WhenPermissaoNotFound() {
        inserirNovaPermissaoNoBanco();
        ResponseEntity<PermissaoNaoEncontradaException> response = testRestTemplate.exchange(
                URL + "/2",
                GET,
                null,
                PermissaoNaoEncontradaException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(PermissaoNaoEncontradaException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 201 and new permissaoResponse When successful")
    void inserir_ReturnStatusCode201AndPermissaoResponse_WhenSuccessful() {
        PermissaoRequest novaPermissao = PermissaoCreator.mockPermissaoRequest();
        ResponseEntity<PermissaoResponse> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaPermissao),
                PermissaoResponse.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(novaPermissao.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When permissao have invalid fields")
    void inserir_ReturnStatusCode400_WhenPermissaoHaveInvalidFields() {
        PermissaoRequest novaPermissao = PermissaoCreator.mockInvalidPermissaoRequest();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaPermissao),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 200 and changed permissaoResponse when successful")
    void alterar_ReturnStatusCode200AndChangedPermissaoResponse_WhenSuccessful() {
        inserirNovaPermissaoNoBanco();
        Permissao permissaoParaAlterar = PermissaoCreator.mockPermissaoUpdated();

        ResponseEntity<PermissaoResponse> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(permissaoParaAlterar),
                PermissaoResponse.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(permissaoParaAlterar.getNome(), response.getBody().getNome())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 400 when permissao have invalid fields")
    void alterar_ReturnStatusCode400_WhenPermissaoHaveInvalidFields() {
        inserirNovaPermissaoNoBanco();
        PermissaoRequest permissaoParaAlterar = PermissaoCreator.mockInvalidPermissaoRequest();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(permissaoParaAlterar),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return statusCode 404 When permissao not found")
    void alterar_ReturnStatusCode404_WhenPermissaoNotFound() {
        Permissao novaPermissao = inserirNovaPermissaoNoBanco();
        Permissao permissaoParaAlterar = PermissaoCreator.mockPermissaoUpdated();

        ResponseEntity<PermissaoNaoEncontradaException> response = testRestTemplate.exchange(
                URL + "/99",
                PUT,
                new HttpEntity<>(permissaoParaAlterar),
                PermissaoNaoEncontradaException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(PermissaoNaoEncontradaException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("excluir Return statusCode 204 when successful")
    void excluir_ReturnStatusCode204_WhenSuccessful() {
        inserirNovaPermissaoNoBanco();
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
    @DisplayName("excluir Return statusCode 404 When permissao not found")
    void excluir_ReturnStatusCode404_WhenPermissaoNotFound() {
        inserirNovaPermissaoNoBanco();
        ResponseEntity<PermissaoNaoEncontradaException> response = testRestTemplate.exchange(
                URL + "/2",
                DELETE,
                null,
                PermissaoNaoEncontradaException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(PermissaoNaoEncontradaException.class, response.getBody().getClass())
        );
    }
}