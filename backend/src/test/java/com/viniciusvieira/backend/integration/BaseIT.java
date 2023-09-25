package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.AuthenticationRequest;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.AuthenticationResponse;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.util.EnderecoCreator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIT {
    @LocalServerPort
    protected int port;

    protected final String forbiddenMessage = "O usuário não tem permissão de acesso. ";

    @Autowired
    protected PessoaRepository pessoaRepository;
    @Autowired
    protected PermissaoRepository permissaoRepository;

    protected BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected AuthenticationResponse userLogin;
    protected AuthenticationResponse adminLogin;

    protected void startTest() {
        Pessoa user = inserirUser();
        userLogin = realizarLogin(user);

        Pessoa admin = inserirAdmin();
        adminLogin = realizarLogin(admin);
    }

    // USER CONFIGS
    private final Pessoa user = Pessoa.builder()
            .permissoes(new ArrayList<>())
            .cpf("302.218.730-07")
            .email("user@gmail.com")
            .nome("user")
            .endereco(EnderecoCreator.createEndereco())
            .senha(passwordEncoder.encode("user"))
            .codigoRecuperacaoSenha("teste")
            .dataEnvioCodigo(LocalDateTime.now())
            .build();

    protected final PessoaRequest userConflict = PessoaRequest.builder()
            .cpf("302.218.730-07")
            .email("user@gmail.com")
            .nome("user")
            .endereco(EnderecoCreator.createEnderecoRequest())
            .nomePermissao("USER")
            .build();

    private final Permissao permissaoUser = Permissao.builder()
            .nome("USER")
            .pessoas(new ArrayList<>())
            .build();

    protected Pessoa inserirUser(){
        user.adicionarPermissao(permissaoUser);
        return pessoaRepository.saveAndFlush(user);
    }

    protected Pessoa getUser(){
        return user;
    }

    // ADMIN CONFIGS
    private final Pessoa admin = Pessoa.builder()
            .permissoes(new ArrayList<>())
            .cpf("962.543.500-09")
            .email("admin@gmail.com")
            .nome("admin")
            .endereco(EnderecoCreator.createEndereco())
            .senha(passwordEncoder.encode("admin"))
            .codigoRecuperacaoSenha("teste")
            .dataEnvioCodigo(LocalDateTime.now())
            .build();

    private final Permissao permissaoAdmin = Permissao.builder()
            .nome("ADMIN")
            .pessoas(new ArrayList<>())
            .build();

    protected Pessoa inserirAdmin(){
        admin.adicionarPermissao(permissaoAdmin);
        return pessoaRepository.saveAndFlush(admin);
    }

    protected Pessoa getAdmin(){
        return admin;
    }

    // ---

    protected String setAuthorization(String token){
        return "Bearer " + token;
    }

    protected AuthenticationResponse realizarLogin(Pessoa pessoa){

        AuthenticationRequest userCredentials = AuthenticationRequest.builder()
                .email(pessoa.getEmail())
                .password(pessoa.getNome())
                .build();

        return given()
                    .body(userCredentials)
                    .contentType(JSON)
                    .accept(JSON)
                .when()
                    .post("/api/auth/login")
                .then()
                    .extract().response().as(AuthenticationResponse.class);
    }

}
