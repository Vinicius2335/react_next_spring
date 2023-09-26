package com.viniciusvieira.backend.integration.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.MarcaRequest;
import com.viniciusvieira.backend.domain.model.venda.Marca;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.integration.BaseIT;
import com.viniciusvieira.backend.util.MarcaCreator;
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

class MarcaControllerIT extends BaseIT {
    @Autowired
    private MarcaRepository marcaRepository;
    
    private final Marca marca = MarcaCreator.createMarca();
    private final String basePath = "/api/marcas";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //RestAssured.basePath = "/api/marcas";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        startTest();
    }

    @Test
    @DisplayName("buscarTodos() return list marcas and status OK by ADMIN")
    void givenMarcaURI_wheBuscarTodos_thenReturnListMarcasAndStatusOK() {
        Marca marcaInserted = getMarcaInserted();
        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(1))
                .body("[0].nome", Matchers.equalTo(marcaInserted.getNome()));
    }

    @Test
    @DisplayName("buscarTodos() return status FORBIDDEN when USER dont have access")
    void givenURIWithUserRole_whenBuscarTodos_thenStatusFORBIDDEN() {

        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage))
                .log().all();
    }


    @Test
    @DisplayName("buscarPeloId() return marca and status OK by ADMIN")
    void givenId_whenBuscarPeloId__thenReturnMarcaAndStatusOK() {
        Marca marcaInserted = getMarcaInserted();
        given()
                .pathParam("id", marcaInserted.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(marcaInserted.getNome()));
    }

    @Test
    @DisplayName("buscarPeloId() return status FORBIDDEN when USER dont have access")
    void givenIDWithUserRole_whenBuscarPeloId_thenStatusFORBIDDEN() {

        given()
                .pathParam("id", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage))
                .log().all();
    }

    @Test
    @DisplayName("buscarPeloId() return status NOT_FOUND when marca not found by ADMIN")
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
                .body("title", Matchers.equalTo("Marca não cadastrada"))
                .log().body();
    }

    @Test
    @DisplayName("inserir() insert marca and return status CREATED by ADMIN")
    void givenMarcaRequest_whenInserir_thenStatusCREATED() {
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        given()
                .body(marcaRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", Matchers.equalTo(marcaRequest.getNome()));
    }

    @Test
    @DisplayName("inserir() return status FORBIDDEN when USER dont have access")
    void givenMarcaRequestWithUserRole_whenInserir_thenStatusFORBIDDEN() {
        Marca marcaRequest = MarcaCreator.createMarca();

        given()
                .body(marcaRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage))
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status CONFLICT when marca already registered by ADMIN")
    void givenMarcaRequestAlreadyRegistered_whenInserir_thenStatusCONFLICT() {
        getMarcaInserted();
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        given()
                .body(marcaRequest)
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
    @DisplayName("inserir() return status BAD_REQUEST when marcaRequest have invalid fields by ADMIN")
    void givenInvalidMarcaRequest_whenInserir_thenStatusBAD_REQUEST() {
        MarcaRequest invalidMarcaRequest = MarcaCreator.createInvalidMarcaRequest();
        given()
                .body(invalidMarcaRequest)
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
    @DisplayName("alterar() update marca and return status OK by ADMIN")
    void givenMarcaRequest_whenAlterar_thenStatusOK() {
        getMarcaInserted();
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        marcaRequest.setNome("Samsung");

        given()
                .body(marcaRequest)
                .pathParam("id", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(marcaRequest.getNome()));
    }

    @Test
    @DisplayName("alterar() return status FORBIDDEN when USER dont have access")
    void givenMarcaRequestWithUserRole_whenAlterar_thenReturnStatusFORBIDDEN() {
        getMarcaInserted();
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        marcaRequest.setNome("Samsung");

        given()
                .body(marcaRequest)
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
    @DisplayName("alterar() return status CONFLICT when marca already registered by ADIMN")
    void givenMarcaRequestAlreadyRegistered_whenAlterar_thenStatusCONFLICT() {
        getMarcaInserted();
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();

        given()
                .body(marcaRequest)
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
    @DisplayName("alterar() return status BAD_REQUEST when marcaRequest have invalid fields by ADMIN")
    void givenInvalidMarcaRequest_whenAlterar_thenStatusBAD_REQUEST() {
        getMarcaInserted();
        MarcaRequest invalidMarcaRequest = MarcaCreator.createInvalidMarcaRequest();
        given()
                .body(invalidMarcaRequest)
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
    @DisplayName("excluir() remove marca BY ADMIN")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        getMarcaInserted();
        given()
                .pathParam("id", marca.getId())
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
        getMarcaInserted();
        given()
                .pathParam("id", marca.getId())
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
    @DisplayName("excluir() Throws MarcaNaoEncontradaException when marca not found by ADMIN")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", marca.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Marca não cadastrada"))
                .log().all();
    }

    private Marca getMarcaInserted(){
        return marcaRepository.saveAndFlush(marca);
    }
}