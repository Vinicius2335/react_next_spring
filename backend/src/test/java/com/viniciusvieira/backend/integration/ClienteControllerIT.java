package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.util.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Log4j2
@DisplayName("Teste de Integração para ClienteController")
class ClienteControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private PermissaoRepository permissaoRepository;

    private static final String URL = "/api/clientes";

    @Test
    @DisplayName("inserir Save pessoa cliente and return statusCode 201 When successful")
    // COMMENT - Tem que configurar as variaveis de ambiente para o envio de email para nao dar erro
    void inserir_SaveClienteAndReturn201_WhenSuccessful(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        permissaoRepository.saveAndFlush(PermissaoCreator.mockPermissaoCliente());

        ClienteRequest clienteRequest = ClienteCreator.mockClienteRequest();

        ResponseEntity<PessoaResponse> response = testRestTemplate.exchange(
                URL,
                HttpMethod.POST,
                new HttpEntity<>(clienteRequest),
                PessoaResponse.class
        );

        log.info("------- RESPONSE BODY ------");
        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(clienteRequest.getCpf(), response.getBody().getCpf())
         );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When cliente have invalid fields")
    void inserir_Return400_WhenClienteHaveInvalidFields(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        permissaoRepository.saveAndFlush(PermissaoCreator.mockPermissaoCliente());

        ClienteRequest clienteRequest = ClienteCreator.mockInvalidClienteRequest();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL,
                HttpMethod.POST,
                new HttpEntity<>(clienteRequest),
                Object.class
        );

        log.info("------- RESPONSE BODY ------");
        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When cliente cpf is in use")
    void inserir_Return400_WhenClienteCpfIsInUse(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        permissaoRepository.saveAndFlush(PermissaoCreator.mockPermissaoCliente());
        pessoaRepository.saveAndFlush(PessoaCreator.mockPessoa());

        ClienteRequest clienteRequest = ClienteCreator.mockClienteRequest();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL,
                HttpMethod.POST,
                new HttpEntity<>(clienteRequest),
                Object.class
        );

        log.info("------- RESPONSE BODY ------");
        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }
}