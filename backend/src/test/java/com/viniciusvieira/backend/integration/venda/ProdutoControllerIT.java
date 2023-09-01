package com.viniciusvieira.backend.integration.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
class ProdutoControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final Produto produto = ProdutoCreator.createProduto();


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/produtos";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("buscarTodos() return list of produtos")
    void givenURI_whenBuscarTodos_thenStatusOK() {
        insertProdutos();
        given()
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].descricaoCurta", Matchers.equalTo(produto.getDescricaoCurta()));
    }

    @Test
    @DisplayName("buscarPorId() return produto")
    void givenId_whenBuscarPorId_thenStatusOK() {
        insertProdutos();
        given()
                .pathParam("id", produto.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("descricaoCurta", Matchers.equalTo(produto.getDescricaoCurta()));
    }

    @Test
    @DisplayName("buscarPorId() return status NOT_FOUND when produto not found by id")
    void givenUnregisteredId_whenBuscarPorId_thenStatusNOT_FOUND() {
        given()
                .pathParam("id", produto.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Produto não encontrado"))
                .log().all();
    }

    @Test
    @DisplayName("inserir() insert new produto")
    void givenProdutoRequest_whenInserir_thenStatusCREATED() {
        marcaRepository.saveAndFlush(MarcaCreator.createMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createCategoria());
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);

        given()
                .body(produtoRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("descricaoCurta", Matchers.equalTo(produtoRequest.getDescricaoCurta()))
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status BAD_REQUEST when produtos have invalid fields")
    void givenInvalidProdutoRequest_whenInserir_thenStatusBAD_REQUEST() {
        marcaRepository.saveAndFlush(MarcaCreator.createMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createCategoria());
        ProdutoRequest invalidProdutoRequest = ProdutoCreator.createInvalidProdutoRequest();

        given()
                .body(invalidProdutoRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @Disabled
    @DisplayName("uploadFile()")
    void uploadFile() {
        // TEST - ImagemUploadService PRIMEIRO
    }

    @Test
    @DisplayName("alterar() update produto")
    void givenProdutoRequest_whenAlterar_thenStatusOK() {
        insertProdutos();
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);

        given()
                .body(produtoRequest)
                .pathParam("id", produto.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("descricaoCurta", Matchers.equalTo(produtoRequest.getDescricaoCurta()))
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status NOT_FOUND when produto not found by id")
    void givenUnregisteredId_whenAlterar_thenStatusNOT_FOUND() {
        ProdutoRequest produtoRequest = ProdutoCreator.createProdutoRequest(produto);

        given()
                .body(produtoRequest)
                .pathParam("id", produto.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Produto não encontrado"))
                .log().all();
    }

    @Test
    @DisplayName("alterar() return status BAD_REQUEST when produto request have invalid fields")
    void givenInvalidProdutoRequest_whenAlterar_thenStatusBAD_REQUEST() {
        ProdutoRequest invalidProdutoRequest = ProdutoCreator.createInvalidProdutoRequest();

        given()
                .body(invalidProdutoRequest)
                .pathParam("id", produto.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    @Test
    @Disabled
    @DisplayName("alterarImagem()")
    void alterarImagem() {
        // TEST - ImagemUploadService PRIMEIRO
    }

    @Test
    @DisplayName("excluir() remove produto")
    void givenId_whenExcluir_thenStatusNO_CONTENT() {
        insertProdutos();

        given()
                .pathParam("id", produto.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("excluir() return status NOT_FOUND when produto not found by id")
    void givenUnregisteredId_whenExcluir_thenStatusNOT_FOUND() {

        given()
                .pathParam("id", produto.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
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