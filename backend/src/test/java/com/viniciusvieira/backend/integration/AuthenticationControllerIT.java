package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.AuthenticationRequest;
import com.viniciusvieira.backend.api.representation.model.response.AuthenticationResponse;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

class AuthenticationControllerIT extends BaseIT{
    private final String basePath = "/api/auth";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        //RestAssured.basePath = "/api/auth";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("authenticate() return user informations and access/refresh token")
    void givenCredentials_whenAuthenticate_thenStatus0K() {
        Pessoa user = inserirUser();

        AuthenticationRequest userCredentials = AuthenticationRequest.builder()
                .email(user.getEmail())
                .password(user.getNome())
                .build();

        given()
                .body(userCredentials)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath + "/login")
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }

    @Test
    @DisplayName("authenticate() return Status UNAUTHORIZED when bad credentials")
    void givenInvalidCredentials_whenAuthenticate_thenStatusUNAUTHORIZED() {
        Pessoa user = inserirUser();

        AuthenticationRequest userCredentials = AuthenticationRequest.builder()
                .email(user.getEmail())
                .password("jdsiajdiksa")
                .build();

        given()
                .body(userCredentials)
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath + "/login")
        .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("message", Matchers.equalTo("Credenciais incorretas."))
                .log().all();
    }

    @Test
    @DisplayName("refreshToken() return new valid accessToken")
    void givenRefreshToken_whenRefreshToken_thenStatusOK() {
        Pessoa user = inserirUser();
        AuthenticationResponse userLogin = realizarLogin(user);

        given()
                .header(HttpHeaders.AUTHORIZATION, setAuthorization(userLogin.getRefreshToken()))
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath + "/refresh-token")
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }

    @Test
    @DisplayName("refreshToken() return status UNAUTHORIZED when refresh token is invalid")
    void givenInvalidRefreshToken_whenRefreshToken_thenStatusUNAUTHORIZED() {

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer akdmsad")
                .contentType(JSON)
                .accept(JSON)
        .when()
                .post(basePath + "/refresh-token")
        .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("message", Matchers.equalTo("JWT token inválido..."))
                .log().all();
    }

}