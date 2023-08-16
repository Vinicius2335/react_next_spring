package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.exception.CarrinhoDeCompraNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.repository.usuario.CidadeRepository;
import com.viniciusvieira.backend.domain.repository.usuario.EstadoRepository;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.domain.repository.venda.CarrinhoCompraRepository;
import com.viniciusvieira.backend.util.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;

@Log4j2
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DisplayName("Teste de Integração da classe CarrinhoDeCompraController")
class CarrinhoDeCompraControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private CarrinhoCompraRepository carrinhoDeCompraRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private PermissaoRepository permissaoRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    private static final String URL = "/api/carrinhos";

    public CarrinhoDeCompra inserirNovaCarrinhoDeCompraNoBanco(){
        permissaoRepository.saveAndFlush(PermissaoCreator.mockPermissao());
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        pessoaRepository.saveAndFlush(PessoaCreator.mockPessoa());
        
        return carrinhoDeCompraRepository.saveAndFlush(CarrinhoDeCompraCreator.mockCarrinhoDeCompra());
    }

    @Test
    @DisplayName("buscarTodos Return statusCode 200 and list of carrinhoDeCompras When successful")
    void buscarTodos_ReturnStatusCode200AndListCarrinhoDeCompras_WhenSuccessful() {
        CarrinhoDeCompra carrinhoDeCompraSaved = inserirNovaCarrinhoDeCompraNoBanco();

        ParameterizedTypeReference<List<CarrinhoDeCompra>> typeReference = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<CarrinhoDeCompra>> response = testRestTemplate.exchange(
                URL,
                GET,
                null,
                typeReference
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(carrinhoDeCompraSaved.getId(), response.getBody().get(0).getId()),
                () -> assertEquals(carrinhoDeCompraSaved.getObservacao(), response.getBody().get(0).getObservacao()),
                () -> assertEquals(carrinhoDeCompraSaved.getSituacao(), response.getBody().get(0).getSituacao())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 201 and new carrinhoDeCompraResponse When successful")
    void inserir_InsertNewCarrinhoDeCompraResponseAndReturn201_WhenSuccessful() {
        permissaoRepository.saveAndFlush(PermissaoCreator.mockPermissao());
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        pessoaRepository.saveAndFlush(PessoaCreator.mockPessoa());

        CarrinhoDeCompraRequest novaCarrinhoDeCompra = CarrinhoDeCompraCreator.mockCarrinhoDeCompraRequest();
        ResponseEntity<CarrinhoDeCompraResponse> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaCarrinhoDeCompra),
                CarrinhoDeCompraResponse.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(novaCarrinhoDeCompra.getObservacao(), response.getBody().getObservacao()),
                () -> assertEquals(novaCarrinhoDeCompra.getSituacao(), response.getBody().getSituacao())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When carrinhoDeCompraResponse have invalid fields")
    void inserir_ReturnStatusCode400_WhenCarrinhoDeCompraResponseHaveInvalidFields() {
        CarrinhoDeCompraRequest novaCarrinhoDeCompra = CarrinhoDeCompraCreator.mockInvalidCarrinhoDeCompraRequest();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaCarrinhoDeCompra),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 200 and carrinhoDeCompraResponse changed when successful")
    void alterar_ReturnStatusCode200AndChangedCarrinhoDeCompra_WhenSuccessful() {
        inserirNovaCarrinhoDeCompraNoBanco();
        CarrinhoDeCompraRequest carrinhoDeCompraParaAlterar = CarrinhoDeCompraCreator.mockCarrinhoDeCompraRequestToUpdate();

        ResponseEntity<CarrinhoDeCompraResponse> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(carrinhoDeCompraParaAlterar),
                CarrinhoDeCompraResponse.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(carrinhoDeCompraParaAlterar.getSituacao(), response.getBody().getSituacao()),
                () -> assertEquals(carrinhoDeCompraParaAlterar.getObservacao(), response.getBody().getSituacao())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 400 when carrinhoDeCompra have invalid fields")
    void alterar_ReturnStatusCode400_WhenCarrinhoDeCompraHaveInvalidFields() {
        inserirNovaCarrinhoDeCompraNoBanco();
        CarrinhoDeCompraRequest carrinhoDeCompraParaAlterar = CarrinhoDeCompraCreator.mockInvalidCarrinhoDeCompraRequest();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(carrinhoDeCompraParaAlterar),
                Object.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return statusCode 404 When carrinhoDeCompra not found")
    void alterar_ReturnStatusCode404_WhenCarrinhoDeCompraNotFound() {
        inserirNovaCarrinhoDeCompraNoBanco();
        CarrinhoDeCompraRequest carrinhoDeCompraParaAlterar = CarrinhoDeCompraCreator.mockCarrinhoDeCompraRequestToUpdate();

        ResponseEntity<CarrinhoDeCompraNaoEncontradoException> response = testRestTemplate.exchange(
                URL + "/99",
                PUT,
                new HttpEntity<>(carrinhoDeCompraParaAlterar),
                CarrinhoDeCompraNaoEncontradoException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(CarrinhoDeCompraNaoEncontradoException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("excluir Return statusCode 204 when successful")
    void excluir_ReturnStatusCode204_WhenSuccessful() {
        CarrinhoDeCompra carrinhoDeCompra = inserirNovaCarrinhoDeCompraNoBanco();
        log.info("CarrinhoDeCompra Inserida -> {}", carrinhoDeCompra);

        ResponseEntity<Void> response = testRestTemplate.exchange(
                URL + "/1",
                DELETE,
                null,
                Void.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Return statusCode 404 When carrinhoDeCompra not found")
    void excluir_ReturnStatusCode404_WhenCarrinhoDeCompraNotFound() {
        inserirNovaCarrinhoDeCompraNoBanco();
        ResponseEntity<CarrinhoDeCompraNaoEncontradoException> response = testRestTemplate.exchange(
                URL + "/2",
                DELETE,
                null,
                CarrinhoDeCompraNaoEncontradoException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(CarrinhoDeCompraNaoEncontradoException.class, response.getBody().getClass())
        );
    }

}