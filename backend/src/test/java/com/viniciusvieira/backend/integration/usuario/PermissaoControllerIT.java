package com.viniciusvieira.backend.integration.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PermissaoRequest;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.integration.BaseIT;
import com.viniciusvieira.backend.util.PermissaoCreator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;


class PermissaoControllerIT extends BaseIT {
    private final String basePath = "/api/permissoes";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //RestAssured.basePath = "/api/permissoes";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        startTest();
    }

    @Test
    @DisplayName("busucarTodos() return list permissoes and status OK by ADMIN")
    void givenPermissoesURI_wheBuscarTodos_thenReturnListPermissoesAndStatusOK() {
        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(2));
    }

    @Test
    @DisplayName("busucarTodos() return status FORBIDDEN  when USER dont have access")
    void givenURIWithUserRole_wheBuscarTodos_thenReturnStatusUNAUTHORIZED() {
        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage));
    }

    @Test
    @DisplayName("buscarPeloId() return permissao and status OK by ADMIN")
    void givenId_whenBuscarPeloId__thenReturnPermissaoAndStatusOK() {
        given()
                .pathParam("id", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("buscarPeloId()  return status FORBIDDEN  when any USER have expired token")
    void givenIdWithUserRole_whenBuscarPeloId__thenReturnStatusFORBIDDEN() {
        given()
                .pathParam("id", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage));
    }

    @Test
    @DisplayName("buscarPeloId() return status NOT_FOUND when permissao not found by ADMIN")
    void givenUnregisteredId_whenBuscarPeloId__thenStatusNOT_FOUND() {
        given()
                .pathParam("id",99)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Permissão não cadastrada"))
                .log().body();
    }

    @Test
    @DisplayName("inserir() insert permissao and return status CREATED by ADMIN")
    void givenPermissaoRequest_whenInserir_thenStatusCREATED() {
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        given()
                .body(permissaoRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", Matchers.equalTo(permissaoRequest.getNome()));
    }

    @Test
    @DisplayName("inserir() return status FORBIDDEN  when USER dont have access")
    void givenPermissaoRequestWithUserRole_whenInserir_thenStatusFORBIDDEN() {
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        given()
                .body(permissaoRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage));
    }

    @Test
    @DisplayName("inserir() return status CONFLICT when permissao already registered by ADMIN")
    void givenPermissaoRequestAlreadyRegistered_whenInserir_thenStatusCONFLICT() {
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        permissaoRequest.setNome("USER");

        given()
                .body(permissaoRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status BAD_REQUEST when permissaoRequest have invalid fields by ADMIN")
    void givenInvalidPermissaoRequest_whenInserir_thenStatusBAD_REQUEST() {
        PermissaoRequest invalidPermissaoRequest = PermissaoCreator.createInvalidPermissaoRequest();
        given()
                .body(invalidPermissaoRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @DisplayName("alterar() update permissao and return status OK by ADMIN")
    void givenPermissaoRequest_whenAlterar_thenStatusOK() {
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        permissaoRequest.setNome("FUNCIONARIO");

        given()
                .body(permissaoRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(permissaoRequest.getNome()));
    }

    @Test
<<<<<<< HEAD
    @DisplayName("alterar() return status CONFLICT when permissao already registered")
    void givenPermissaoRequestAlreadyRegistered_whenAlterar_thenStatusCONFLICT() {
        getPermissaoInserted();

        given()
                .body(permissaoRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
>>>>>>> 86317af (tentando corrigir um erro de Git error broken link from tree)
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage));
    }

    @Test
    @DisplayName("alterar() return status CONFLICT when permissao already registered by ADMIN")
    void givenPermissaoRequestAlreadyRegistered_whenAlterar_thenStatusCONFLICT() {
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        permissaoRequest.setNome("USER");

        given()
                .body(permissaoRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status BAD_REQUEST when permissaoRequest have invalid fields by ADMIN")
    void givenInvalidPermissaoRequest_whenAlterar_thenStatusBAD_REQUEST() {
        PermissaoRequest invalidPermissaoRequest = PermissaoCreator.createInvalidPermissaoRequest();
        given()
                .body(invalidPermissaoRequest)
                .pathParam("id", 99)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @DisplayName("excluir() remove permissao by ADMIN")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        Permissao permissaoCriada = PermissaoCreator.createPermissao();
        permissaoCriada.setId(3L);
        Permissao permissaoInserida = permissaoRepository.saveAndFlush(permissaoCriada);

        given()
                .pathParam("id", permissaoInserida.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("excluir() return status FORBIDDEN when USER dont have access")
    void givenIdWithUserRole_whenExcluir_thenStatusFORBIDDEN() {
        given()
                .pathParam("id",1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage));
    }

    @Test
    @DisplayName("excluir() Throws PermissaoNaoEncontradaException when permissao not found by ADMIN")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", 99)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Permissão não cadastrada"))
                .log().all();
    }
}