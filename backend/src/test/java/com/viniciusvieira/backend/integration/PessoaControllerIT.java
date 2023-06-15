package com.viniciusvieira.backend.integration;

import com.viniciusvieira.backend.api.representation.model.request.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Pessoa;
import com.viniciusvieira.backend.domain.repository.CidadeRepository;
import com.viniciusvieira.backend.domain.repository.EstadoRepository;
import com.viniciusvieira.backend.domain.repository.PermissaoRepository;
import com.viniciusvieira.backend.domain.repository.PessoaRepository;
import com.viniciusvieira.backend.util.CidadeCreator;
import com.viniciusvieira.backend.util.EstadoCreator;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
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
@DisplayName("Teste de Integração da classe PessoaController")
class PessoaControllerIT {
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

    private static final String URL = "/api/pessoas";

    public Pessoa inserirNovaPessoaNoBanco(){
        permissaoRepository.saveAndFlush(PermissaoCreator.mockPermissao());
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        return pessoaRepository.saveAndFlush(PessoaCreator.mockPessoa());
    }

    @Test
    @DisplayName("buscarTodos Return statusCode 200 and list of pessoas When successful")
    void buscarTodos_ReturnStatusCode200AndListPessoas_WhenSuccessful() {
        Pessoa pessoaSaved = inserirNovaPessoaNoBanco();

        ParameterizedTypeReference<List<Pessoa>> typeReference = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<Pessoa>> response = testRestTemplate.exchange(
                URL,
                GET,
                null,
                typeReference
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(pessoaSaved.getId(), response.getBody().get(0).getId()),
                () -> assertEquals(pessoaSaved.getNome(), response.getBody().get(0).getNome())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 201 and new pessoaResponse When successful")
    void inserir_InsertNewPessoaResponseAndReturn201_WhenSuccessful() {
        permissaoRepository.saveAndFlush(PermissaoCreator.mockPermissao());
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());

        PessoaRequest novaPessoa = PessoaCreator.mockPessoaRequestToSave();
        ResponseEntity<PessoaResponse> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaPessoa),
                PessoaResponse.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(novaPessoa.getNome(), response.getBody().getNome()),
                () -> assertEquals(novaPessoa.getCpf(), response.getBody().getCpf())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 and NegocioException When cpf is in use")
    void inserir_ReturnStatusCode400AndNegocioException_WhenCpfInUse(){
        inserirNovaPessoaNoBanco();

        PessoaRequest novaPessoa = PessoaCreator.mockPessoaRequestToSave();
        ResponseEntity<NegocioException> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaPessoa),
                NegocioException.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(NegocioException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("inserir Return statusCode 400 When category have invalid fields")
    void inserir_ReturnStatusCode400_WhenCategoryHaveInvalidFields() {
        PessoaRequest novaPessoa = PessoaCreator.mockInvalidPessoaRequest();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL,
                POST,
                new HttpEntity<>(novaPessoa),
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 200 and pessoaResponse changed when successful")
    void alterar_ReturnStatusCode200AndChangedPessoa_WhenSuccessful() {
        inserirNovaPessoaNoBanco();
        PessoaRequest pessoaParaAlterar = PessoaCreator.mockPessoaRequestToUpdate();

        ResponseEntity<PessoaResponse> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(pessoaParaAlterar),
                PessoaResponse.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(pessoaParaAlterar.getNome(), response.getBody().getNome()),
                () -> assertEquals(pessoaParaAlterar.getCpf(), response.getBody().getCpf())
        );
    }

    @Test
    @DisplayName("alterar Return StatusCode 400 when pessoa have invalid fields")
    void alterar_ReturnStatusCode400_WhenPessoaHaveInvalidFields() {
        inserirNovaPessoaNoBanco();
        PessoaRequest pessoaParaAlterar = PessoaCreator.mockInvalidPessoaRequest();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL + "/1",
                PUT,
                new HttpEntity<>(pessoaParaAlterar),
                Object.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterar Return statusCode 404 When pessoa not found")
    void alterar_ReturnStatusCode404_WhenPessoaNotFound() {
        inserirNovaPessoaNoBanco();
        PessoaRequest pessoaParaAlterar = PessoaCreator.mockPessoaRequestToUpdate();

        ResponseEntity<PessoaNaoEncontradaException> response = testRestTemplate.exchange(
                URL + "/99",
                PUT,
                new HttpEntity<>(pessoaParaAlterar),
                PessoaNaoEncontradaException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(PessoaNaoEncontradaException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("excluir Return statusCode 204 when successful")
    void excluir_ReturnStatusCode204_WhenSuccessful() {
        Pessoa pessoa = inserirNovaPessoaNoBanco();
        log.info("Pessoa Inserida -> {}", pessoa);

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
    @DisplayName("excluir Return statusCode 404 When pessoa not found")
    void excluir_ReturnStatusCode404_WhenPessoaNotFound() {
        inserirNovaPessoaNoBanco();
        ResponseEntity<PessoaNaoEncontradaException> response = testRestTemplate.exchange(
                URL + "/2",
                DELETE,
                null,
                PessoaNaoEncontradaException.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals(PessoaNaoEncontradaException.class, response.getBody().getClass())
        );
    }

    @Test
    @DisplayName("excluirPermissao Return statusCode 204 when successful")
    void excluirPermissao_ReturnStatusCode204_WhenSuccessful() {
        inserirNovaPessoaNoBanco();
        ResponseEntity<Void> response = testRestTemplate.exchange(
                URL + "/1/permissoes/1",
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
    @DisplayName("excluirPermissao Return statusCode 404 When pessoa not found")
    void excluirPermissao_ReturnStatusCode404_WhenPessoaNotFound() {
        inserirNovaPessoaNoBanco();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL + "/99/permissoes/1",
                DELETE,
                null,
                Object.class
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluirPermissao Return statusCode 404 When permissao not found")
    void excluirPermissao_ReturnStatusCode404_WhenPermissaoNotFound() {
        inserirNovaPessoaNoBanco();
        ResponseEntity<Object> response = testRestTemplate.exchange(
                URL + "/1/permissoes/90",
                DELETE,
                null,
                Object.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("Teste CascadeDelete excluindo um estado depois de inserir varios relacionamentos")
    void testeCascadeDelete_RemoveEstado() {
        // Estado -> Cidade -> Pessoa -> Pessoa_Permissao -> Permissao
        inserirNovaPessoaNoBanco();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                "/api/estados/1",
                DELETE,
                null,
                Object.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("Teste CascadeDelete excluindo um cidade depois de inserir varios relacionamentos")
    void testeCascadeDelete_RemoveCidade() {
        // Cidade -> Pessoa -> Pessoa_Permissao -> Permissao
        inserirNovaPessoaNoBanco();

        ResponseEntity<Object> response = testRestTemplate.exchange(
                "/api/cidades/1",
                DELETE,
                null,
                Object.class
        );

        log.info(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }
}