package com.viniciusvieira.backend.integration.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PessoaControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PermissaoRepository permissaoRepository;

    private final Pessoa pessoa = PessoaCreator.createPessoa();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/pessoas";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void givenURI_whenBuscarTodos_thenReturnListPessoasAndStatusOK() {
        Pessoa pessoaInserted = getPessoaInserted();

        given()
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(1))
                .body("[0].cpf", Matchers.equalTo(pessoaInserted.getCpf()));

    }

    @Test
    void givenPessoaRequest_whenInserir_thenReturnPessoaResponseAndStatusCREATED() {
        getPermissaoInserted();

        given()
                .body(PessoaCreator.createPessoaRequest())
                .contentType(JSON)
                .accept(JSON)
                .log().all()
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("cpf", Matchers.equalTo(pessoa.getCpf()));
    }

    @Test
    void givenInvalidPessoaRequest_whenInserir_thenReturnPessoaAndStatusCREATED() {
        getPermissaoInserted();
        PessoaRequest invalidPessoaRequest = PessoaCreator.createInvalidPessoaRequest();

        given()
                .body(invalidPessoaRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().body();
    }

    @Test
    void givenPessoaRequestWithCpfRegistered_whenInserir_thenReturnStatusCONFLICT() {
        getPessoaInserted();
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();

        given()
                .body(pessoaRequest)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("title", Matchers.equalTo("Já existe uma pessoa cadastrada com esse CPF"))
                .log().body();
    }

    @Test
    void givenPessoaRequest_whenAlterar_thenReturnPessoaResponseStatusOK() {
        Pessoa pessoaInserted = getPessoaInserted();
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();

        given()
                .body(pessoaRequest)
                .pathParam("id", pessoaInserted.getId())
                .contentType(JSON)
                .accept(JSON)
                .log().all()
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(pessoaRequest.getNome()))
                .log().body();
    }

    @Test
    void givenInvalidPessoaRequest_whenAlterar_thenReturnStatusBAD_REQUEST() {
        Pessoa pessoaInserted = getPessoaInserted();
        PessoaRequest pessoaRequest = PessoaCreator.createInvalidPessoaRequest();

        given()
                .body(pessoaRequest)
                .pathParam("id", pessoaInserted.getId())
                .contentType(JSON)
                .accept(JSON)
                .log().all()
        .when()
                .put("/{id}")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().body();
    }

    @Test
    void givenIdPessoaAndIdPermissao_whenExcluirPermissao_thenReturnStatusNO_CONTENT() {
        Pessoa pessoaInserted = getPessoaInserted();

        given()
                .pathParam("idPessoa", pessoaInserted.getId())
                .pathParam("idPermissao", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{idPessoa}/permissoes/{idPermissao}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void givenUnregisteredIdPessoaAndIdPermissao_whenExcluirPermissao_thenReturnStatusNOT_FOUND() {
        getPessoaInserted();

        given()
                .pathParam("idPessoa", 99)
                .pathParam("idPermissao", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{idPessoa}/permissoes/{idPermissao}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este ID"))
                .log().all();
    }

    @Test
    void givenIdPessoa_whenExcluir_thenReturnStatusNO_CONTENT() {
        Pessoa pessoaInserted = getPessoaInserted();

        given()
                .pathParam("id", pessoaInserted.getId())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void givenUnregisteredIdPessoa_whenExcluir_thenReturnStatusNOT_FOUND() {
        getPessoaInserted();

        given()
                .pathParam("id", 99)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este ID"));
    }

    private Pessoa getPessoaInserted(){
        Permissao permissaoInserted = getPermissaoInserted();
        pessoa.adicionarPermissao(permissaoInserted);
        return pessoaRepository.saveAndFlush(pessoa);
    }

    private Permissao getPermissaoInserted(){
        return permissaoRepository.saveAndFlush(PermissaoCreator.createPermissao());
    }
}