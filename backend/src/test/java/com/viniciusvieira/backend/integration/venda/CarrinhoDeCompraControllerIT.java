package com.viniciusvieira.backend.integration.venda;


import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.repository.venda.CarrinhoCompraRepository;
import com.viniciusvieira.backend.integration.BaseIT;
import com.viniciusvieira.backend.util.CarrinhoDeCompraCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
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


class CarrinhoDeCompraControllerIT extends BaseIT {
    @Autowired
    private CarrinhoCompraRepository carrinhoCompraRepository;

    private final CarrinhoDeCompra carrinhoDeCompra = CarrinhoDeCompraCreator.createCarrinhoDeCompra();
    private static final String CARRINHO_NOT_FOUND = "CarrinhoDeCompra não cadastrado";
    private final String basePath = "/api/carrinhos";


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //RestAssured.basePath = "/api/carrinhos";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        startTest();
    }

    @Test
    @DisplayName("buscarTodos() return list of carrinhos by ADMIN")
    void givenURI_whenBuscarTodos_thenStatusOK() {
        insertCarrinhoDeCompra();
        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].situacao", Matchers.equalTo(carrinhoDeCompra.getSituacao()));
    }

    @Test
    @DisplayName("buscarTodos() return status FORBIDDEN when USER dont have access")
    void givenURIWithUserRole_whenBuscarTodos_thenReturnStatusFORBIDDEN() {
        insertCarrinhoDeCompra();
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
    @DisplayName("buscarPorId() return carrinhoDeCompra by ADMIN")
    void givenId_whenBuscarPorId_thenStatusOK() {
        insertCarrinhoDeCompra();
        given()
                .pathParam("id", carrinhoDeCompra.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("situacao", Matchers.equalTo(carrinhoDeCompra.getSituacao()));
    }

    @Test
    @DisplayName("buscarPorId() return status FORBIDDEN when USER dont have access")
    void givenIdWithUserRole_whenBuscarPorId_thenReturnStatusFORBIDDEN() {
        insertCarrinhoDeCompra();
        given()
                .pathParam("id", carrinhoDeCompra.getId())
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
    @DisplayName("buscarPorId() return status NOT_FOUND when carrinhoDeCompra not found by id with ADMIN role")
    void givenUnregisteredId_whenBuscarPorId_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", carrinhoDeCompra.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(CARRINHO_NOT_FOUND))
                .log().all();
    }

    @Test
    @DisplayName("inserir() insert new carrinhoDeCompra by ADMIN")
    void givenCarrinhoDeCompraRequest_whenInserir_thenStatusCREATED() {
        pessoaRepository.saveAndFlush(PessoaCreator.createPessoa());
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();

        given()
                .body(carrinhoDeCompraRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("situacao", Matchers.equalTo(carrinhoDeCompraRequest.getSituacao()))
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status FORBIDDEN when USER dont have access")
    void givenCarrinhoDeCompraRequestWithUserRole_whenInserir_thenReturnStatusFORBIDDEN() {
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();
        carrinhoDeCompraRequest.setPessoaId(getUser().getId());

        given()
                .body(carrinhoDeCompraRequest)
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
    @DisplayName("inserir() return status BAD_REQUEST when carrinhos have invalid fields by ADMIN")
    void givenInvalidCarrinhoDeCompraRequest_whenInserir_thenStatusBAD_REQUEST() {
        pessoaRepository.saveAndFlush(PessoaCreator.createPessoa());
        CarrinhoDeCompraRequest invalidCarrinhoDeCompraRequest = CarrinhoDeCompraCreator.createInvalidCarrinhoDeCompraRequest();

        given()
                .body(invalidCarrinhoDeCompraRequest)
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
    @DisplayName("alterar() update carrinhoDeCompra by ADMIN")
    void givenCarrinhoDeCompraRequest_whenAlterar_thenStatusOK() {
        insertCarrinhoDeCompra();
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();

        given()
                .body(carrinhoDeCompraRequest)
                .pathParam("id", carrinhoDeCompra.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("situacao", Matchers.equalTo(carrinhoDeCompraRequest.getSituacao()))
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status FORBIDDEN when USER dont have access")
    void givenCarrinhoDeCompraRequestWithUserRole_whenAlterar_thenReturnStatusFORBIDDEN() {
        insertCarrinhoDeCompra();
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();

        given()
                .body(carrinhoDeCompraRequest)
                .pathParam("id", carrinhoDeCompra.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage))
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status NOT_FOUND when carrinhoDeCompra not found by id with ADMIN role")
    void givenUnregisteredId_whenAlterar_thenStatusNOT_FOUND() {
        CarrinhoDeCompraRequest carrinhoDeCompraRequest = CarrinhoDeCompraCreator.createCarrinhoDeCompraRequest();

        given()
                .body(carrinhoDeCompraRequest)
                .pathParam("id", carrinhoDeCompra.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(CARRINHO_NOT_FOUND))
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status BAD_REQUEST when carrinhoDeCompra request have invalid fields by ADMIN")
    void givenInvalidCarrinhoDeCompraRequest_whenAlterar_thenStatusBAD_REQUEST() {
        CarrinhoDeCompraRequest invalidCarrinhoDeCompraRequest = CarrinhoDeCompraCreator.createInvalidCarrinhoDeCompraRequest();

        given()
                .body(invalidCarrinhoDeCompraRequest)
                .pathParam("id", carrinhoDeCompra.getId())
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
    @DisplayName("excluir() remove carrinhoDeCompra by ADMIN")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        insertCarrinhoDeCompra();

        given()
                .pathParam("id", carrinhoDeCompra.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("excluir() return status FORBIDDEN when USER dont have access")
    void givenIdWithUserRole_whenExcluir_thenReturnStatusFORBIDDEN() {
        insertCarrinhoDeCompra();

        given()
                .pathParam("id", carrinhoDeCompra.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage))
                .log().all();
    }

    @Test
    @DisplayName("excluir() return status NOT_FOUND when carrinhoDeCompra not found by id with ADMIN role")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {

        given()
                .pathParam("id", carrinhoDeCompra.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(CARRINHO_NOT_FOUND))
                .log().all();
    }

    private void insertCarrinhoDeCompra(){
        Pessoa pessoaInserted = getUser();

        CarrinhoDeCompra carrinhoDeCompra = CarrinhoDeCompraCreator.createCarrinhoDeCompra();
        carrinhoDeCompra.setPessoa(pessoaInserted);
        carrinhoCompraRepository.saveAndFlush(carrinhoDeCompra);
    }
}