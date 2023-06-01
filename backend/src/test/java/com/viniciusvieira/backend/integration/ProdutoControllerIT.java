package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoResponse;
import com.viniciusvieira.backend.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Produto;
import com.viniciusvieira.backend.domain.repository.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.MarcaRepository;
import com.viniciusvieira.backend.domain.repository.ProdutoRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
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
@DisplayName("Teste de Integração da classe ProdutoController")
class ProdutoControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private static final String URL = "/api/produtos";

    public Produto inserirNovaProdutoNoBanco() {
        marcaRepository.saveAndFlush(MarcaCreator.mockMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());
        return produtoRepository.saveAndFlush(ProdutoCreator.mockProduto());
    }

    @Test
    @DisplayName("buscarTodos Return statusCode 200 and list of produtos When successful")
    void buscarTodos_ReturnStatusCode200AndListProdutos_WhenSuccessful() {
        Produto produtoSaved = inserirNovaProdutoNoBanco();

        ParameterizedTypeReference<List<Produto>> typeReference = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Produto>> response = testRestTemplate.exchange(
                URL,
                GET,
                null,
                typeReference
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(produtoSaved.getId(), response.getBody().get(0).getId()),
                () -> assertEquals(produtoSaved.getDescricaoCurta(), response.getBody().get(0).getDescricaoCurta()),
                () -> assertEquals(produtoSaved.getQuantidade(), response.getBody().get(0).getQuantidade()),
                () -> assertEquals(produtoSaved.getValorCusto(), response.getBody().get(0).getValorCusto())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 202 and new produtoResponse When successful")
    void inserir_InsertNewProdutoResponse_WhenSuccessful() {
        marcaRepository.saveAndFlush(MarcaCreator.mockMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());

        ProdutoRequest novoProduto = ProdutoCreator.mockProdutoRequestToSave();
        ResponseEntity<ProdutoResponse> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novoProduto),
                ProdutoResponse.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(novoProduto.getDescricaoCurta(), response.getBody().getDescricaoCurta()),
                () -> assertEquals(novoProduto.getQuantidade(), response.getBody().getQuantidade()),
                () -> assertEquals(novoProduto.getValorCusto(), response.getBody().getValorCusto())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When category have invalid fields")
    void inserir_ReturnStatusCode400_WhenCategoryHaveInvalidFields() {
        ProdutoRequest novaProduto = ProdutoCreator.mockInvalidProdutoRequest();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaProduto),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 200 and produtoResponse changed when successful")
    void alterar_ReturnStatusCode200AndChangedProduto_WhenSuccessful() {
        inserirNovaProdutoNoBanco();
        ProdutoRequest produtoParaAlterar = ProdutoCreator.mockProdutoRequestToUpdate();

        ResponseEntity<ProdutoResponse> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(produtoParaAlterar),
                ProdutoResponse.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(produtoParaAlterar.getDescricaoCurta(), response.getBody().getDescricaoCurta()),
                () -> assertEquals(produtoParaAlterar.getQuantidade(), response.getBody().getQuantidade()),
                () -> assertEquals(produtoParaAlterar.getValorCusto(), response.getBody().getValorCusto())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 400 when produto have invalid fields")
    void alterar_ReturnStatusCode400_WhenProdutoHaveInvalidFields() {
        inserirNovaProdutoNoBanco();
        ProdutoRequest produtoParaAlterar = ProdutoCreator.mockInvalidProdutoRequest();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(produtoParaAlterar),
                Object.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return statusCode 404 When produto not found")
    void alterar_ReturnStatusCode404_WhenProdutoNotFound() {
        inserirNovaProdutoNoBanco();
        Produto produtoParaAlterar = ProdutoCreator.mockProdutoToUpdate();

        ResponseEntity<ProdutoNaoEncontradoException> response = testRestTemplate.exchange(
                URL + "/99",
                PUT,
                new HttpEntity<>(produtoParaAlterar),
                ProdutoNaoEncontradoException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(ProdutoNaoEncontradoException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("excluir Return statusCode 204 when successful")
    void excluir_ReturnStatusCode204_WhenSuccessful() {
        inserirNovaProdutoNoBanco();
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
    @DisplayName("excluir Return statusCode 404 When produto not found")
    void excluir_ReturnStatusCode404_WhenProdutoNotFound() {
        inserirNovaProdutoNoBanco();
        ResponseEntity<ProdutoNaoEncontradoException> response = testRestTemplate.exchange(
                URL + "/2",
                DELETE,
                null,
                ProdutoNaoEncontradoException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(ProdutoNaoEncontradoException.class, response.getBody().getClass())
        );
    }
}