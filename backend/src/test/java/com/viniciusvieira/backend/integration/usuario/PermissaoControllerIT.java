package com.viniciusvieira.backend.integration.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PermissaoRequest;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;


@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PermissaoControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private PermissaoRepository permissaoRepository;

    private final Permissao permissao = PermissaoCreator.createPermissao();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/permissoes";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("busucarTodos() return list permissoes and status OK")
    void givenPermissoesURI_wheBuscarTodos_thenReturnListPermissoesAndStatusOK() {
        Permissao permissaoInserted = getPermissaoInserted();
        given()
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(1))
                .body("[0].nome", Matchers.equalTo(permissaoInserted.getNome()));
    }

    @Test
    @DisplayName("buscarPeloId() return permissao and status OK")
    void givenId_whenBuscarPeloId__thenReturnPermissaoAndStatusOK() {
        Permissao permissaoInserted = getPermissaoInserted();
        given()
                .pathParam("id", permissaoInserted.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(permissaoInserted.getNome()));
    }

    @Test
    @DisplayName("buscarPeloId() return status NOT_FOUND when permissao not found")
    void givenUnregisteredId_whenBuscarPeloId__thenStatusNOT_FOUND() {
        given()
                .pathParam("id",99)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Permissão não cadastrada"))
                .log().body();
    }

    @Test
    @DisplayName("inserir() insert permissao and return status CREATED")
    void givenPermissaoRequest_whenInserir_thenStatusCREATED() {
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        given()
                .body(permissaoRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", Matchers.equalTo(permissaoRequest.getNome()));
    }

    @Test
    @DisplayName("inserir() return status CONFLICT when permissao already registered")
    void givenPermissaoRequestAlreadyRegistered_whenInserir_thenStatusCONFLICT() {
        getPermissaoInserted();
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        given()
                .body(permissaoRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status BAD_REQUEST when permissaoRequest have invalid fields")
    void givenInvalidPermissaoRequest_whenInserir_thenStatusBAD_REQUEST() {
        PermissaoRequest invalidPermissaoRequest = PermissaoCreator.createInvalidPermissaoRequest();
        given()
                .body(invalidPermissaoRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @DisplayName("alterar() update permissao and return status OK")
    void givenPermissaoRequest_whenAlterar_thenStatusOK() {
        getPermissaoInserted();
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        permissaoRequest.setNome("FUNCIONARIO");

        given()
                .body(permissaoRequest)
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(permissaoRequest.getNome()));
    }

    @Test
    @DisplayName("excluir() remove permissao")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        getPermissaoInserted();
        given()
                .pathParam("id", permissao.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("excluir() Throws PermissaoNaoEncontradaException when permissao not found")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", permissao.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Permissão não cadastrada"))
                .log().all();
    }

    private Permissao getPermissaoInserted(){
        return permissaoRepository.saveAndFlush(permissao);
    }
}