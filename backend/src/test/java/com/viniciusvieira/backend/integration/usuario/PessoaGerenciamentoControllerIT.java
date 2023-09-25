package com.viniciusvieira.backend.integration.usuario;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.integration.BaseIT;
import com.viniciusvieira.backend.util.PessoaCreator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

class PessoaGerenciamentoControllerIT extends BaseIT {
    private final String basePath = "/api/gerenciamento";

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration
                    .aConfig().withUser("user@gmail.com", "password"));

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //RestAssured.basePath = "/api/gerenciamento";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        startTest();
    }

    @Test
    @DisplayName("recuperarCodigoViaEmail() sending email by USER")
    void givenEmailByUser_whenRecuperarCodigoViaEmail_thenStatusNO_CONTENT() {
        given()
                .queryParam("email", userLogin.getUser().getEmail())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/solicitar-codigo")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("recuperarCodigoViaEmail() sending email by ADMIN")
    void givenEmailByAdmin_whenRecuperarCodigoViaEmail_thenStatusNO_CONTENT() {
        given()
                .queryParam("email", adminLogin.getUser().getEmail())
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/solicitar-codigo")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("recuperarCodigoViaEmail() return status NOT_FOUND when pessoa not found by email by USER")
    void givenUnregisteredEmail_whenRecuperarCodigoViaEmail_thenStatusNOT_FOUND() {
        given()
                .queryParam("email", "notfound@gmail.com")
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/solicitar-codigo")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este EMAIL"))
                .log().all();
    }

    @Test
    @DisplayName("alterarSenha() update pessoa by USER")
    void givenPessoaGerenciamentoRequestByUser_whenAlterarSenha_thenStatusNO_CONTENT() {
        Pessoa user = getUser();
        PessoaGerenciamentoRequest request = PessoaCreator.createPessoaGerenciamentoRequest(user);

        given()
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/alterar-senha")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("alterarSenha() update pessoa by ADMIN")
    void givenPessoaGerenciamentoRequestByAdmin_whenAlterarSenha_thenStatusNO_CONTENT() {
        Pessoa admin = getAdmin();
        PessoaGerenciamentoRequest request = PessoaCreator.createPessoaGerenciamentoRequest(admin);

        given()
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/alterar-senha")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("alterarSenha() return NOT_FOUND when pessoa not found by emailAndCodigo by USER")
    void givenUnregisteredPessoaGerenciamentoRequest_whenAlterarSenha_thenStatusNOT_FOUND() {
        Pessoa user = getUser();
        PessoaGerenciamentoRequest request = PessoaCreator.createPessoaGerenciamentoRequest(user);
        request.setCodigoParaRecuperarSenha("senhaErrada");

        given()
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/alterar-senha")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este EMAIL e CODIGO"))
                .log().all();
    }

    @Test
    @DisplayName("alterarSenha() return BAD_REQUEST when dataEnvioCodigo expired by USER")
    void givenPessoaGerenciamentoRequest_whenAlterarSenha_thenStatusBAD_REQUEST() {
        Pessoa user = getUser();
        user.setDataEnvioCodigo(LocalDateTime.now().minusHours(1));
        user.setCodigoRecuperacaoSenha("teste");
        Pessoa userEdited = pessoaRepository.saveAndFlush(user);
        PessoaGerenciamentoRequest request = PessoaCreator.createPessoaGerenciamentoRequest(userEdited);

        given()
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put(basePath + "/alterar-senha")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo("Tempo expirado, solicite um novo código"))
                .log().all();
    }
}