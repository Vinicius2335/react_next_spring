package com.viniciusvieira.backend.integration.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoRepository;
import com.viniciusvieira.backend.integration.BaseIT;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
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

class ProdutoControllerIT extends BaseIT {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final Produto produto = ProdutoCreator.createProduto();
    private final String basePath = "/api/produtos";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //RestAssured.basePath = "/api/produtos";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        startTest();
    }

    @Test
    @DisplayName("buscarTodos() return list of produtos by ADMIN")
    void givenURI_whenBuscarTodos_thenStatusOK() {
        insertProdutos();
        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].descricaoCurta", Matchers.equalTo(produto.getDescricaoCurta()));
    }

    @Test
    @DisplayName("buscarTodos() return status FORBIDDEN  when USER dont have access")
    void givenURIWithUserRole_whenBuscarTodos_thenReturnStatusFORBIDDEN() {
        insertProdutos();
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
    @DisplayName("buscarPorId() return produto by ADMIN")
    void givenId_whenBuscarPorId_thenStatusOK() {
        insertProdutos();
        given()
                .pathParam("id", produto.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("descricaoCurta", Matchers.equalTo(produto.getDescricaoCurta()));
    }


    @Test
    @DisplayName("buscarPorId() return status NOT_FOUND when produto not found by id BY ADMIN")
    void givenUnregisteredId_whenBuscarPorId_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", produto.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Produto não encontrado"))
                .log().all();
    }

    @Test
    @DisplayName("buscarPorId() return status FORBIDDEN  when USER dont have access")
    void givenIdWithUserRole_whenbuscarPorId_thenReturnStatusFORBIDDEN() {
        insertProdutos();
        given()
                .pathParam("id", produto.getId())
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
    @DisplayName("inserir() insert new produto by ADMIN")
    void givenProdutoRequest_whenInserir_thenStatusCREATED() {
        marcaRepository.saveAndFlush(MarcaCreator.createMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createCategoria());
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);

        given()
                .body(produtoRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("descricaoCurta", Matchers.equalTo(produtoRequest.getDescricaoCurta()))
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status FORBIDDEN  when USER dont have access")
    void givenProdutoRequestWithUserRole_whenInserir_thenReturnStatusFORBIDDEN() {
        marcaRepository.saveAndFlush(MarcaCreator.createMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createCategoria());
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);

        given()
                .body(produtoRequest)
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
    @DisplayName("inserir() return status BAD_REQUEST when produtos have invalid fields by ADMIN")
    void givenInvalidProdutoRequest_whenInserir_thenStatusBAD_REQUEST() {
        marcaRepository.saveAndFlush(MarcaCreator.createMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createCategoria());
        ProdutoRequest invalidProdutoRequest = ProdutoCreator.createInvalidProdutoRequest();

        given()
                .body(invalidProdutoRequest)
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
    @DisplayName("alterar() update produto by ADMIN")
    void givenProdutoRequest_whenAlterar_thenStatusOK() {
        insertProdutos();
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);

        given()
                .body(produtoRequest)
                .pathParam("id", produto.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("descricaoCurta", Matchers.equalTo(produtoRequest.getDescricaoCurta()))
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status FORBIDDEN when USER dont have access")
    void givenProdutoRequestWithUserRole_whenAlterar_thenReturnStatusFORBIDDEN() {
        insertProdutos();
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);

        given()
                .body(produtoRequest)
                .pathParam("id", produto.getId())
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
    @DisplayName("alterar() return status NOT_FOUND when produto not found by id and ADMIN")
    void givenUnregisteredId_whenAlterar_thenStatusNOT_FOUND() {
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);

        given()
                .body(produtoRequest)
                .pathParam("id", produto.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Produto não encontrado"))
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status BAD_REQUEST when produto request have invalid fields by ADMIN")
    void givenInvalidProdutoRequest_whenAlterar_thenStatusBAD_REQUEST() {
        ProdutoRequest invalidProdutoRequest = ProdutoCreator.createInvalidProdutoRequest();

        given()
                .body(invalidProdutoRequest)
                .pathParam("id", produto.getId())
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
    @DisplayName("excluir() remove produto by ADMIN")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        insertProdutos();

        given()
                .pathParam("id", produto.getId())
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
    void givenIdWithUserRole_whenExcluir_thenStatusFORBIDDEN() {
        insertProdutos();

        given()
                .pathParam("id", produto.getId())
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
    @DisplayName("excluir() return status NOT_FOUND when produto not found by id by ADMIN")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {

        given()
                .pathParam("id", produto.getId())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Produto não encontrado"))
                .log().all();
    }

    private void insertProdutos(){
        marcaRepository.saveAndFlush(MarcaCreator.createMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createCategoria());

        produtoRepository.saveAndFlush(produto);
    }
}