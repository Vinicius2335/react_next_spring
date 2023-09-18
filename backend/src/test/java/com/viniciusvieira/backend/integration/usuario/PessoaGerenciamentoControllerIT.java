package com.viniciusvieira.backend.integration.usuario;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;


@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PessoaGerenciamentoControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PermissaoRepository permissaoRepository;

    private final Pessoa pessoa = PessoaCreator.createPessoa();

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration
                    .aConfig().withUser("user@gmail.com", "password"));

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/gerenciamento";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("recuperarCodigoViaEmail() sending email")
    void givenEmail_whenRecuperarCodigoViaEmail_thenStatusNO_CONTENT() {
        Pessoa pessoaInserted = getPessoaInserted();

        given()
                .queryParam("email", pessoaInserted.getEmail())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/solicitar-codigo")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("recuperarCodigoViaEmail() return status NOT_FOUND when pessoa not found by email")
    void givenUnregisteredEmail_whenRecuperarCodigoViaEmail_thenStatusNOT_FOUND() {
        given()
                .queryParam("email", pessoa.getEmail())
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/solicitar-codigo")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este EMAIL"))
                .log().all();
    }

    @Test
    @DisplayName("alterarSenha() update pessoa")
    void givenPessoaGerenciamentoRequest_whenAlterarSenha_thenStatusNO_CONTENT() {
        pessoa.setDataEnvioCodigo(LocalDateTime.now());
        pessoa.setCodigoRecuperacaoSenha("teste");
        Pessoa pessoaInserted = getPessoaInserted();
        PessoaGerenciamentoRequest request = PessoaCreator.createPessoaGerenciamentoRequest(pessoaInserted);

        given()
                .body(request)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/alterar-senha")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("alterarSenha() return NOT_FOUND when pessoa not found by emailAndCodigo")
    void givenUnregisteredPessoaGerenciamentoRequest_whenAlterarSenha_thenStatusNOT_FOUND() {
        pessoa.setDataEnvioCodigo(LocalDateTime.now());
        pessoa.setCodigoRecuperacaoSenha("teste");
        Pessoa pessoaInserted = getPessoaInserted();
        PessoaGerenciamentoRequest request = PessoaCreator.createPessoaGerenciamentoRequest(pessoaInserted);
        request.setCodigoParaRecuperarSenha("senhaErrada");

        given()
                .body(request)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/alterar-senha")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo("Não existe nenhuma pessoa cadastrada com este EMAIL e CODIGO"))
                .log().all();
    }

    @Test
    @DisplayName("alterarSenha() return BAD_REQUEST when dataEnvioCodigo expired")
    void givenPessoaGerenciamentoRequest_whenAlterarSenha_thenStatusBAD_REQUEST() {
        LocalDateTime dateExpired = LocalDateTime.now().minusMinutes(30);
        pessoa.setDataEnvioCodigo(dateExpired);
        pessoa.setCodigoRecuperacaoSenha("teste");
        Pessoa pessoaInserted = getPessoaInserted();
        PessoaGerenciamentoRequest request = PessoaCreator.createPessoaGerenciamentoRequest(pessoaInserted);

        given()
                .body(request)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .put("/alterar-senha")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo("Tempo expirado, solicite um novo código"))
                .log().all();
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