package com.viniciusvieira.backend.integration.venda;


import com.viniciusvieira.backend.api.representation.model.request.venda.CategoriaRequest;
import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
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
class CategoriaControllerIT {
    @LocalServerPort
    private int port;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final Categoria categoria = CategoriaCreator.createCategoria();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/categorias";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("busucarTodos() return list categorias and status OK")
    void givenCategoriaURI_wheBuscarTodos_thenReturnListCategoriasAndStatusOK() {
        Categoria categoriaInserted = getCategoriaInserted();
        given()
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(1))
                .body("[0].nome", Matchers.equalTo(categoriaInserted.getNome()));
    }

    @Test
    @DisplayName("buscarPeloId() return categoria and status OK")
    void givenId_whenBuscarPeloId__thenReturnCategoriaAndStatusOK() {
        Categoria categoriaInserted = getCategoriaInserted();
        given()
                .pathParam("id", categoriaInserted.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(categoriaInserted.getNome()));
    }

    @Test
    @DisplayName("buscarPeloId() return status NOT_FOUND when categoria not found")
    void givenUnregisteredId_whenBuscarPeloId__thenStatusNOT_FOUND() {
        given()
                .pathParam("id",99)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Categoria não cadastrada"))
                .log().body();
    }

    @Test
    @DisplayName("inserir() insert categoria and return status CREATED")
    void givenCategoriaRequest_whenInserir_thenStatusCREATED() {
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        given()
                .body(categoriaRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", Matchers.equalTo(categoriaRequest.getNome()));
    }

    @Test
    @DisplayName("inserir() return status CONFLICT when categoria already registered")
    void givenCategoriaRequestAlreadyRegistered_whenInserir_thenStatusCONFLICT() {
        getCategoriaInserted();
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        given()
                .body(categoriaRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status BAD_REQUEST when categoriaRequest have invalid fields")
    void givenInvalidCategoriaRequest_whenInserir_thenStatusBAD_REQUEST() {
        CategoriaRequest invalidCategoriaRequest = CategoriaCreator.createInvalidCategoriaRequest();
        given()
                .body(invalidCategoriaRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @DisplayName("alterar() update categoria and return status OK")
    void givenCategoriaRequest_whenAlterar_thenStatusOK() {
        getCategoriaInserted();
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        categoriaRequest.setNome("Samsung");

        given()
                .body(categoriaRequest)
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(categoriaRequest.getNome()));
    }

    @Test
    @DisplayName("alterar() return status CONFLICT when categoria already registered")
    void givenCategoriaRequestAlreadyRegistered_whenAlterar_thenStatusCONFLICT() {
        getCategoriaInserted();
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        given()
                .body(categoriaRequest)
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
    @DisplayName("alterar() return status BAD_REQUEST when categoriaRequest have invalid fields")
    void givenInvalidCategoriaRequest_whenAlterar_thenStatusBAD_REQUEST() {
        getCategoriaInserted();
        CategoriaRequest invalidCategoriaRequest = CategoriaCreator.createInvalidCategoriaRequest();
        given()
                .body(invalidCategoriaRequest)
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
    @DisplayName("excluir() remove categoria")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        getCategoriaInserted();
        given()
                .pathParam("id", categoria.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("excluir() Throws CategoriaNaoEncontradaException when categoria not found")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", categoria.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Categoria não cadastrada"))
                .log().all();
    }

    private Categoria getCategoriaInserted(){
        return categoriaRepository.saveAndFlush(categoria);
    }

}