package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoImagemRepository;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
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

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;

import static com.viniciusvieira.backend.domain.service.CrudProdutoImagemService.PATH_DIRECTORY;
import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProdutoImagemControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProdutoImagemRepository produtoImagemRepository;

    private final Produto produto = ProdutoCreator.createProduto();
    private File image;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/produtos/{idProduto}/imagens";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        image = new File("src/test/resources/tmp/test.png");
    }

    @Test
    @DisplayName("buscarImagensPorProduto() return a list of produtoImagem")
    void givenIdProduto_whenBuscarImagensPorProduto_thenReturnListImagensAndStatusOK() throws IOException {
        createFileIfNotExists();
        insertProdutoImagem();

        given()
                .pathParam("idProduto", 1)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }

    @Test
    @DisplayName("uploadFile() upload image")
    void givenIdProdutoAndImage_whenUploadFile_thenStatusCREATED(){
        insertProdutos();

        given()
                .pathParam("idProduto", 1)
                .multiPart("file", image)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all();
    }

    @Test
    @DisplayName("uploadFile() return status NOT_FOUND when produto not found by id")
    void givenUnregisteredIdProduto_whenUploadFile_thenStatusNOT_FOUND(){
        given()
                .pathParam("idProduto", 1)
                .multiPart("file", image)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Produto não encontrado"))
                .log().all();
    }

    private Produto insertProdutos(){
        marcaRepository.saveAndFlush(MarcaCreator.createMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createCategoria());

        return produtoRepository.saveAndFlush(produto);
    }

    private void insertProdutoImagem(){
        Produto produtoInserted = insertProdutos();

        ProdutoImagem imagem = ProdutoImagem.builder()
                .imageCode("12345678")
                .nome("12345678-test.png")
                .produto(produtoInserted)
                .dataCriacao(OffsetDateTime.now())
                .dataAtualizacao(OffsetDateTime.now())
                .build();

         produtoImagemRepository.saveAndFlush(imagem);
    }

    private void createFileIfNotExists() throws IOException {
        File file = new File(PATH_DIRECTORY + "/12345678-test.png");
        if (!file.exists()){
            file.createNewFile();
        }
    }
}