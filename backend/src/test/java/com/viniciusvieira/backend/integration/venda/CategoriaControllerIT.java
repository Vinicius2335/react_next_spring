package com.viniciusvieira.backend.integration.venda;


import com.viniciusvieira.backend.api.representation.model.request.venda.CategoriaRequest;
import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.integration.BaseIT;
import com.viniciusvieira.backend.util.CategoriaCreator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

class CategoriaControllerIT extends BaseIT {
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final Categoria categoria = CategoriaCreator.createCategoria();
    private final String basePath = "/api/categorias";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //RestAssured.basePath = "/api/categorias";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        startTest();
    }

    @Test
    @DisplayName("busucarTodos() return list categorias and status OK by ADMIN")
    void givenCategoriaURI_wheBuscarTodos_thenReturnListCategoriasAndStatusOK() {
        Categoria categoriaInserted = getCategoriaInserted();
        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(1))
                .body("[0].nome", Matchers.equalTo(categoriaInserted.getNome()));
    }

    @Test
    @DisplayName("busucarTodos() return status FORBIDDEN when USER dont have access")
    void givenCategoriaURIWithUserRole_wheBuscarTodos_thenReturnStatusFORBIDDEN() {
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
    @DisplayName("buscarPeloId() return categoria and status OK by ADMIN")
    void givenId_whenBuscarPeloId__thenReturnCategoriaAndStatusOK() {
        Categoria categoriaInserted = getCategoriaInserted();
        given()
                .pathParam("id", categoriaInserted.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(categoriaInserted.getNome()));
    }

    @Test
    @DisplayName("buscarPeloId() return status FORBIDDEN when USER dont have access")
    void givenIdWithUserRole_whenBuscarPeloId__thenReturnStatusFORBIDDEN() {
        Categoria categoriaInserted = getCategoriaInserted();
        given()
                .pathParam("id", categoriaInserted.getId())
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
    @DisplayName("buscarPeloId() return status NOT_FOUND when categoria not found by ADMIN")
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
                .body("title", Matchers.equalTo("Categoria não cadastrada"))
                .log().body();
    }

    @Test
    @DisplayName("inserir() insert categoria and return status CREATED by ADMIN")
    void givenCategoriaRequest_whenInserir_thenStatusCREATED() {
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        given()
                .body(categoriaRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", Matchers.equalTo(categoriaRequest.getNome()));
    }

    @Test
    @DisplayName("inserir() return status FORBIDDEN when USER dont have access")
    void givenCategoriaRequestWithUserRole_whenInserir_thenReturnStatusFORBIDDEN() {
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        given()
                .body(categoriaRequest)
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
    @DisplayName("inserir() return status CONFLICT when categoria already registered by ADMIN")
    void givenCategoriaRequestAlreadyRegistered_whenInserir_thenStatusCONFLICT() {
        getCategoriaInserted();
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        given()
                .body(categoriaRequest)
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
    @DisplayName("inserir() return status BAD_REQUEST when categoriaRequest have invalid fields by ADMIN")
    void givenInvalidCategoriaRequest_whenInserir_thenStatusBAD_REQUEST() {
        CategoriaRequest invalidCategoriaRequest = CategoriaCreator.createInvalidCategoriaRequest();
        given()
                .body(invalidCategoriaRequest)
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
    @DisplayName("alterar() update categoria and return status OK by ADMIN")
    void givenCategoriaRequest_whenAlterar_thenStatusOK() {
        getCategoriaInserted();
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        categoriaRequest.setNome("Samsung");

        given()
                .body(categoriaRequest)
                .pathParam("id", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(categoriaRequest.getNome()));
    }

    @Test
    @DisplayName("alterar() return status FORBIDDEN when USER dont have access")
    void givenCategoriaRequestWithUserRole_whenAlterar_thenReturnStatusFORBIDDEN() {
        getCategoriaInserted();
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        categoriaRequest.setNome("Samsung");

        given()
                .body(categoriaRequest)
                .pathParam("id", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage));
    }

    @Test
    @DisplayName("alterar() return status CONFLICT when categoria already registered by ADMIN")
    void givenCategoriaRequestAlreadyRegistered_whenAlterar_thenStatusCONFLICT() {
        getCategoriaInserted();
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        given()
                .body(categoriaRequest)
                .pathParam("id", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status BAD_REQUEST when categoriaRequest have invalid fields by ADMIN")
    void givenInvalidCategoriaRequest_whenAlterar_thenStatusBAD_REQUEST() {
        getCategoriaInserted();
        CategoriaRequest invalidCategoriaRequest = CategoriaCreator.createInvalidCategoriaRequest();
        given()
                .body(invalidCategoriaRequest)
                .pathParam("id", 1)
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
    @DisplayName("excluir() remove categoria by ADMIN")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        getCategoriaInserted();
        given()
                .pathParam("id", categoria.getId())
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
    void givenIdWithUserRole_whenExcluir_thenReturnStatusFORBIDDEN() {
        getCategoriaInserted();
        given()
                .pathParam("id", categoria.getId())
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
    @DisplayName("excluir() Throws CategoriaNaoEncontradaException when categoria not found by ADMIN")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", categoria.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Categoria não cadastrada"))
                .log().all();
    }

    private Categoria getCategoriaInserted(){
        return categoriaRepository.saveAndFlush(categoria);
    }

}