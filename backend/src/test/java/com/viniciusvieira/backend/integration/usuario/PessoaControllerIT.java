package com.viniciusvieira.backend.integration.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.integration.BaseIT;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

class PessoaControllerIT extends BaseIT {
    private final String basePath = "/api/pessoas";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //RestAssured.basePath = "/api/pessoas";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        startTest();

        permissaoRepository.saveAndFlush(PermissaoCreator.createPermissao());
    }

    @Test
    @DisplayName("buscarTodos() return list pessoas by ADMIN")
    void givenAdminAcess_whenBuscarTodos_thenReturnListPessoasAndStatusOK() {

        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(2))
                .log().all();
    }

    @Test
    @DisplayName("buscarTodos() return status UNAUTHORIZED  when any USER have expired token")
    void givenAdminAcess_whenBuscarTodos_thenReturntatusUNAUTHORIZED() throws InterruptedException {

        TimeUnit.SECONDS.sleep(30);

        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath)
        .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("message", Matchers.equalTo("Token expirado..."))
                .log().all();
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
    @DisplayName("buscarPermissoes() return list permissoes by ADMIN")
    void givenID_whenBuscarPermissoes_thenReturnListPermissoesAndStatusOK() {

        given()
                .pathParam("id", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}/permissoes")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.hasSize(1))
                .log().all();
    }

    @Test
    @DisplayName("buscarPermissoes() return status FORBIDDEN when USER dont have access")
    void givenIDWithUserRole_whenBuscarPermissoes_thenStatusFORBIDDEN() {

        given()
                .pathParam("id", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}/permissoes")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage))
                .log().all();
    }


    @Test
    @DisplayName("buscarPermissoes() return status NOT_FOUND when pessoa not found by ADMIN")
    void givenUnregisteredID_whenBuscarPermissoes_thenReturnStatusNOT_FOUND() {
        given()
                .pathParam("id", 99)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .get(basePath + "/{id}/permissoes")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este ID"))
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status CREATED by ADMIN")
    void givenPessoaRequest_whenInserir_thenReturnPessoaResponseAndStatusCREATED() {
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();

        given()
                .body(pessoaRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("cpf", Matchers.equalTo(pessoaRequest.getCpf()));
    }

    @Test
    @DisplayName("inserir() return status FORBIDDEN when USER dont have access")
    void givenPessoaRequestWithUserRole_whenInserir_thenStatusFORBIDDEN() {
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();

        given()
                .body(pessoaRequest)
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
    @DisplayName("inserir() return status BAD_REQUEST when pessoa request have invalid fields by ADMIN")
    void givenInvalidPessoaRequest_whenInserir_thenReturnPessoaAndStatusBAD_REQUEST() {
        PessoaRequest invalidPessoaRequest = PessoaCreator.createInvalidPessoaRequest();

        given()
                .body(invalidPessoaRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().body();
    }

    @Test
    @DisplayName("inserir() return status CONFLICT when cpf already exists by ADMIN")
    void givenPessoaRequestAlreadyRegistered_whenInserir_thenReturnStatusCONFLICT() {

        given()
                .body(userConflict)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath)
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("title", Matchers.equalTo("Já existe uma pessoa cadastrada com esse CPF"))
                .log().body();
    }

    @Test
    @DisplayName("alterar() return status OK when update pessoa by ADMIN")
    void givenPessoaRequest_whenAlterar_thenReturnPessoaResponseStatusOK() {
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        pessoaRequest.setCpf("011.173.038-45");

        given()
                .body(pessoaRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo(pessoaRequest.getNome()))
                .log().body();
    }

    @Test
    @DisplayName("alterar() return status FORBIDDEN when USER dont have access")
    void givenPessoaRequestWithUserRole_whenAlterar_thenStatusFORBIDDEN() {
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        pessoaRequest.setCpf("011.173.038-45");

        given()
                .body(pessoaRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage))
                .log().body();
    }

    @Test
    @DisplayName("alterar() return NOT_FOUND when pessoa not found by id by ADMIN")
    void givenUnregisteredId_whenAlterar_thenReturnStatusNOT_FOUND() {
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();

        given()
                .body(pessoaRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .pathParam("id", 99)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este ID"))
                .log().body();
    }

    @Test
    @DisplayName("alterar() return BAD_REQUEST when pessoaRequest have invalid fields by ADMIN")
    void givenInvalidPessoaRequest_whenAlterar_thenReturnStatusBAD_REQUEST() {
        PessoaRequest invalidPessoaRequest = PessoaCreator.createInvalidPessoaRequest();

        given()
                .body(invalidPessoaRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .pathParam("id", 1)
                .contentType(JSON)
                .accept(JSON)
                .log().all()
        .when()
                .put(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().body();
    }

    @Test
    @DisplayName("excluirPermissao() return status NO_CONTENT when the pessoa relationship is deleted by ADMIN")
    void givenIdPessoaAndIdPermissao_whenExcluirPermissao_thenReturnStatusNO_CONTENT() {

        given()
                .pathParam("idPessoa", 1)
                .pathParam("idPermissao", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{idPessoa}/permissoes/{idPermissao}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("excluirPermissao() return status FORBIDDEN when USER dont have access")
    void givenIdPessoaAndIdPermissaoWithUserRole_whenExcluirPermissao_thenReturnStatusFORBIDDEN() {

        given()
                .pathParam("idPessoa", 1)
                .pathParam("idPermissao", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{idPessoa}/permissoes/{idPermissao}")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage));
    }

    @Test
    @DisplayName("excluirPermissao() return status NOT_FOUND when pessoa not found by ADMIN")
    void givenUnregisteredIdPessoaAndIdPermissao_whenExcluirPermissao_thenReturnStatusNOT_FOUND() {

        given()
                .pathParam("idPessoa", 99)
                .pathParam("idPermissao", 1)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{idPessoa}/permissoes/{idPermissao}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este ID"))
                .log().all();
    }

    @Test
    @DisplayName("excluir() return status NO_CONTENT when pessoa is removed by ADMIN")
    void givenIdPessoa_whenExcluir_thenReturnStatusNO_CONTENT() {

        given()
                .pathParam("id", 1)
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
    void givenIdPessoaWithUserRole_whenExcluir_thenReturnStatusFORBIDDEN() {

        given()
                .pathParam("id", 1)
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
    @DisplayName("excluir() return status NOT_FOUND when pessoa not found by ADMIN")
    void givenUnregisteredIdPessoa_whenExcluir_thenReturnStatusNOT_FOUND() {

        given()
                .pathParam("id", 99)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .delete(basePath + "/{id}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este ID"));
    }
}