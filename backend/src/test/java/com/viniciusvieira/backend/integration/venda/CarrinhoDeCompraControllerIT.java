package com.viniciusvieira.backend.integration.venda;


import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.domain.repository.venda.CarrinhoCompraRepository;
import com.viniciusvieira.backend.util.CarrinhoDeCompraCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
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
class CarrinhoDeCompraControllerIT {
    @LocalServerPort
    private int port;
    
    @Autowired
    private CarrinhoCompraRepository carrinhoCompraRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    
    private final CarrinhoDeCompra carrinhoDeCompra = CarrinhoDeCompraCreator.createCarrinhoDeCompra();
    private static final String CARRINHO_NOT_FOUND = "CarrinhoDeCompra não cadastrado";


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/carrinhos";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("buscarTodos() return list of carrinhos")
    void givenURI_whenBuscarTodos_thenStatusOK() {
        insertCarrinhoDeCompra();
        given()
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].situacao", Matchers.equalTo(carrinhoDeCompra.getSituacao()));
    }

    @Test
    @DisplayName("buscarPorId() return carrinhoDeCompra")
    void givenId_whenBuscarPorId_thenStatusOK() {
        insertCarrinhoDeCompra();
        given()
                .pathParam("id", carrinhoDeCompra.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("situacao", Matchers.equalTo(carrinhoDeCompra.getSituacao()));
    }

    @Test
    @DisplayName("buscarPorId() return status NOT_FOUND when carrinhoDeCompra not found by id")
    void givenUnregisteredId_whenBuscarPorId_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", carrinhoDeCompra.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(CARRINHO_NOT_FOUND))
                .log().all();
    }

    @Test
    @DisplayName("inserir() insert new carrinhoDeCompra")
    void givenCarrinhoDeCompraRequest_whenInserir_thenStatusCREATED() {
        pessoaRepository.saveAndFlush(PessoaCreator.createPessoa());
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();

        given()
                .body(carrinhoDeCompraRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("situacao", Matchers.equalTo(carrinhoDeCompraRequest.getSituacao()))
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status BAD_REQUEST when carrinhos have invalid fields")
    void givenInvalidCarrinhoDeCompraRequest_whenInserir_thenStatusBAD_REQUEST() {
        pessoaRepository.saveAndFlush(PessoaCreator.createPessoa());
        CarrinhoDeCompraRequest invalidCarrinhoDeCompraRequest = CarrinhoDeCompraCreator.createInvalidCarrinhoDeCompraRequest();

        given()
                .body(invalidCarrinhoDeCompraRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @DisplayName("alterar() update carrinhoDeCompra")
    void givenCarrinhoDeCompraRequest_whenAlterar_thenStatusOK() {
        insertCarrinhoDeCompra();
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();

        given()
                .body(carrinhoDeCompraRequest)
                .pathParam("id", carrinhoDeCompra.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("situacao", Matchers.equalTo(carrinhoDeCompraRequest.getSituacao()))
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status NOT_FOUND when carrinhoDeCompra not found by id")
    void givenUnregisteredId_whenAlterar_thenStatusNOT_FOUND() {
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();

        given()
                .body(carrinhoDeCompraRequest)
                .pathParam("id", carrinhoDeCompra.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(CARRINHO_NOT_FOUND))
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status BAD_REQUEST when carrinhoDeCompra request have invalid fields")
    void givenInvalidCarrinhoDeCompraRequest_whenAlterar_thenStatusBAD_REQUEST() {
        CarrinhoDeCompraRequest invalidCarrinhoDeCompraRequest = CarrinhoDeCompraCreator.createInvalidCarrinhoDeCompraRequest();

        given()
                .body(invalidCarrinhoDeCompraRequest)
                .pathParam("id", carrinhoDeCompra.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @DisplayName("excluir() remove carrinhoDeCompra")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        insertCarrinhoDeCompra();

        given()
                .pathParam("id", carrinhoDeCompra.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("excluir() return status NOT_FOUND when carrinhoDeCompra not found by id")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {

        given()
                .pathParam("id", carrinhoDeCompra.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(CARRINHO_NOT_FOUND))
                .log().all();
    }

    private void insertCarrinhoDeCompra(){
        pessoaRepository.saveAndFlush(PessoaCreator.createPessoa());
        
        carrinhoCompraRepository.saveAndFlush(CarrinhoDeCompraCreator.createCarrinhoDeCompra());
    }
}