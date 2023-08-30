package com.viniciusvieira.backend.integration.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.MarcaRequest;
import com.viniciusvieira.backend.domain.model.venda.Marca;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.util.MarcaCreator;
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
class MarcaControllerIT {
    @LocalServerPort
    private int port;
    
    @Autowired
    private MarcaRepository marcaRepository;
    
    private final Marca marca = MarcaCreator.createMarca();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/marcas";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("busucarTodos() return list marcas and status OK")
    void givenMarcaURI_wheBuscarTodos_thenReturnListMarcasAndStatusOK() {
        Marca marcaInserted = getMarcaInserted();
        given()
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(1))
                .body("[0].nome", Matchers.equalTo(marcaInserted.getNome()));
    }

    @Test
    @DisplayName("buscarPeloId() return marca and status OK")
    void givenId_whenBuscarPeloId__thenReturnMarcaAndStatusOK() {
        Marca marcaInserted = getMarcaInserted();
        given()
                .pathParam("id", marcaInserted.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(marcaInserted.getNome()));
    }

    @Test
    @DisplayName("buscarPeloId() return status NOT_FOUND when marca not found")
    void givenUnregisteredId_whenBuscarPeloId__thenStatusNOT_FOUND() {
        given()
                .pathParam("id",99)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Marca não cadastrada"))
                .log().body();
    }

    @Test
    @DisplayName("inserir() insert marca and return status CREATED")
    void givenMarcaRequest_whenInserir_thenStatusCREATED() {
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        given()
                .body(marcaRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", Matchers.equalTo(marcaRequest.getNome()));
    }

    @Test
    @DisplayName("inserir() return status CONFLICT when marca already registered")
    void givenMarcaRequestAlreadyRegistered_whenInserir_thenStatusCONFLICT() {
        getMarcaInserted();
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        given()
                .body(marcaRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status BAD_REQUEST when marcaRequest have invalid fields")
    void givenInvalidMarcaRequest_whenInserir_thenStatusBAD_REQUEST() {
        MarcaRequest invalidMarcaRequest = MarcaCreator.createInvalidMarcaRequest();
        given()
                .body(invalidMarcaRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @DisplayName("alterar() update marca and return status OK")
    void givenMarcaRequest_whenAlterar_thenStatusOK() {
        getMarcaInserted();
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        marcaRequest.setNome("Samsung");

        given()
                .body(marcaRequest)
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(marcaRequest.getNome()));
    }

    @Test
    @DisplayName("alterar() return status CONFLICT when marca already registered")
    void givenMarcaRequestAlreadyRegistered_whenAlterar_thenStatusCONFLICT() {
        getMarcaInserted();
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        given()
                .body(marcaRequest)
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status BAD_REQUEST when marcaRequest have invalid fields")
    void givenInvalidMarcaRequest_whenAlterar_thenStatusBAD_REQUEST() {
        getMarcaInserted();
        MarcaRequest invalidMarcaRequest = MarcaCreator.createInvalidMarcaRequest();
        given()
                .body(invalidMarcaRequest)
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @DisplayName("excluir() remove marca")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        getMarcaInserted();
        given()
                .pathParam("id", marca.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("excluir() Throws MarcaNaoEncontradaException when marca not found")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", marca.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Marca não cadastrada"))
                .log().all();
    }

    private Marca getMarcaInserted(){
        return marcaRepository.saveAndFlush(marca);
    }
}