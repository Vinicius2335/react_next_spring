package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoImagemRepository;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

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
    @Autowired
    private ProdutoImagemRepository produtoImagemRepository;

    // para conseguir realizar o testar upload de imagem que tem o MultiPartFile
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String URL = "/api/produtos";
    private static final String PATH_DIRECTORY = "src/main/resources/static/image";

    private Produto inserirNovaProdutoNoBanco() {
        marcaRepository.saveAndFlush(MarcaCreator.mockMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());
        return produtoRepository.saveAndFlush(ProdutoCreator.mockProduto());
    }

    private void criarArquivoParaTeste() throws IOException {
        File file = new File(PATH_DIRECTORY + "/image.png");
        file.createNewFile();
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
    @DisplayName("inserir Return statusCode 201 and new produtoResponse When successful")
    void inserir_InsertNewProdutoResponseAndReturn201_WhenSuccessful() {
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
    @DisplayName("uploadFile Save image of produto and return statusCode 204 When successful")
    void uploadFile_SaveImageOfProdutoAndReturn204_WhenSuccessful() throws Exception {
        inserirNovaProdutoNoBanco();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "upload.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem para upload".getBytes()
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(multipart(URL + "/1/image/")
                .file(file))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("uploadFile return statusCode 404 When produto not found by id")
    void uploadFile_Return404_WhenProdutoNotFoundById() throws Exception {
        inserirNovaProdutoNoBanco();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "upload.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem para upload".getBytes()
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(multipart(URL + "/99/image/")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
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
    @DisplayName("alterarImagem Update imagem and return statusCode 200 When successful")
    void alterarImagem_UpdateImageAndReturn200_WhenSuccessful() throws Exception {
        inserirNovaProdutoNoBanco();
        criarArquivoParaTeste();
        produtoImagemRepository.saveAndFlush(ProdutoImagemCreator.mockProdutoImagem());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "teste.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem para teste".getBytes()
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        MockMultipartHttpServletRequestBuilder builder = multipart(URL + "/1/image/1/");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(
                builder.file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("alterarImagem Return statusCode 404 When produto not found")
    void alterarImagem_Return404_WhenProdutoNotFound() throws Exception {
        inserirNovaProdutoNoBanco();
        produtoImagemRepository.saveAndFlush(ProdutoImagemCreator.mockProdutoImagem());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "teste.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem para teste".getBytes()
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        MockMultipartHttpServletRequestBuilder builder = multipart(URL + "/99/image/1/");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(
                        builder.file(file))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("alterarImagem Return statusCode 404 When produtoImagem not found")
    void alterarImagem_Return404_WhenProdutoImagemNotFound() throws Exception {
        inserirNovaProdutoNoBanco();
        produtoImagemRepository.saveAndFlush(ProdutoImagemCreator.mockProdutoImagem());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "teste.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem para teste".getBytes()
        );

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        MockMultipartHttpServletRequestBuilder builder = multipart(URL + "/1/image/99/");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(
                        builder.file(file))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

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