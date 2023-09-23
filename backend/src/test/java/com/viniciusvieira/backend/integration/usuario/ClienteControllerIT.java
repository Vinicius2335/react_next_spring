package com.viniciusvieira.backend.integration.usuario;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.integration.BaseIT;
import com.viniciusvieira.backend.util.ClienteCreator;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;


class ClienteControllerIT extends BaseIT {
    private final Pessoa cliente = PessoaCreator.createPessoa();
    private final ClienteRequest clienteRequest = ClienteCreator.createClienteRequest();

    private final String basepath = "/api/clientes";

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration
                    .aConfig().withUser("user@gmail.com", "password"));

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //RestAssured.basePath = "/api/clientes";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        permissaoRepository.saveAndFlush(PermissaoCreator.createPermissao());

        startTest();
    }

    @Test
    @DisplayName("inserir() inset cliente by ADMIN")
    void givenClienteRequest_whenInserir_thenClienteInsertedAndStatusCREATED() {
        given()
                .body(clienteRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basepath)
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("cpf", Matchers.equalTo(clienteRequest.getCpf()))
                .log().all();
    }

    @Test
    @DisplayName("inserir() return status FORBIDDEN  when USER dont have access")
    void givenClienteRequest_whenInserir_thenReturnStatusFORBIDDEN() {
        given()
                .body(clienteRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basepath)
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", Matchers.equalTo(forbiddenMessage))
                .log().all();
    }

    @Test
    @DisplayName("inserir() Throws CpfAlreadyExistsException when cpf already exists by ADMIN")
    void givenRegisteredClienteRequest_whenInserir_thenStatusCONFLICT() {
        insertCliente();
        given()
                .body(clienteRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basepath)
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("title", Matchers.equalTo("Já existe uma pessoa cadastrada com esse CPF"))
                .log().all();
    }

    @Test
    @DisplayName("inserir() return BAD_REQUEST when clienteRequest have invalid fields by ADMIN")
    void givenInvalidClienteRequest_whenInserir_thenStatusBAD_REQUEST() {
        ClienteRequest invalidClienteRequest = ClienteCreator.createInvalidClienteRequest();
        given()
                .body(invalidClienteRequest)
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(adminLogin.getAccessToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basepath)
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all();
    }

    private void insertCliente(){
        cliente.setCpf(clienteRequest.getCpf());
        pessoaRepository.saveAndFlush(cliente);
    }
}