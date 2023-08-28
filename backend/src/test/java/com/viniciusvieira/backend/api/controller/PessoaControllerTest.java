package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.controller.usuario.PessoaController;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import com.viniciusvieira.backend.util.CidadeCreator;
import com.viniciusvieira.backend.util.EstadoCreator;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste Unitário para a classe PessoaController")
class PessoaControllerTest {
    @InjectMocks
    private PessoaController pessoaController;
    @Mock
    private CrudPessoaService mockCrudPessoaService;
    @Mock
    private EstadoRepository mockEstadoRepository;
    @Mock
    private CidadeRepository mockCidadeRepository;

    private final Pessoa validPessoa = PessoaCreator.mockPessoa();
    private final PessoaResponse expectedPessoa = PessoaCreator.mockPessoaResponse();
    private final List<Pessoa> expectedListPessoas = List.of(validPessoa);
    private final PessoaResponse expectedPessoaUpdated = PessoaCreator.mockPessoaResponseUpdate();

    @BeforeEach
    void setUp() {
        validPessoa.adicionarPermissao(PermissaoCreator.mockPermissao());
        // EstadoRepository - saveAndFlush
        BDDMockito.when(mockEstadoRepository.saveAndFlush(any(Estado.class))).thenReturn(EstadoCreator.mockEstado());
        // CidadeRepository - saveAndFlush
        BDDMockito.when(mockCidadeRepository.saveAndFlush(any(Cidade.class))).thenReturn(CidadeCreator.mockCidade());

        // CrudPessoaService
        // buscarTodos
        BDDMockito.when(mockCrudPessoaService.buscarTodos()).thenReturn(expectedListPessoas);
        // buscarPeloId
        BDDMockito.when(mockCrudPessoaService.buscarPorId(anyLong())).thenReturn(validPessoa);
        // inserir
        BDDMockito.when(mockCrudPessoaService.inserir(any(PessoaRequest.class))).thenReturn(expectedPessoa);
        // alterar
        BDDMockito.when(mockCrudPessoaService.alterar(anyLong(), any(PessoaRequest.class))).thenReturn(expectedPessoaUpdated);
        // excluir
        BDDMockito.doNothing().when(mockCrudPessoaService).excluir(anyLong());
    }

    @Test
    @DisplayName("buscarTodos Return list of pessoa When successful")
    void buscarTodos_ReturnListPessoa_WhenSuccessful() {
        ResponseEntity<List<Pessoa>> response = pessoaController.buscarTodos();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedListPessoas, response.getBody())
        );
    }

    @Test
    @DisplayName("inserir Insert new pessoa When successful")
    void inserir_InsertNewPessoa_WhenSuccessful() {
        PessoaRequest pessoaParaSalvar = PessoaCreator.mockPessoaRequestToSave();
        ResponseEntity<PessoaResponse> response = pessoaController.inserir(pessoaParaSalvar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(expectedPessoa.getNome(), response.getBody().getNome()),
                () -> assertEquals(expectedPessoa.getCpf(), response.getBody().getCpf())
        );
    }

    @Test
    @DisplayName("inserir Thorws NegocioException When CPF is in use")
    void inserir_ThorwsNegocioException_WhenCpfIsInUse() {
        BDDMockito.when(mockCrudPessoaService.inserir(any(PessoaRequest.class))).thenThrow(NegocioException.class);
        PessoaRequest pessoaParaSalvar = PessoaCreator.mockPessoaRequestToSave();

        assertThrows(NegocioException.class, () -> pessoaController.inserir(pessoaParaSalvar));
    }

    @Test
    @DisplayName("alterar Update pessoa when successful")
    void alterar_UpdatePessoa_WhenSuccessul() {
        PessoaRequest pessoaParaAlterar = PessoaCreator.mockPessoaRequestToUpdate();
        ResponseEntity<PessoaResponse> response = pessoaController.alterar(1L, pessoaParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedPessoaUpdated.getNome(), response.getBody().getNome()),
                () -> assertEquals(expectedPessoaUpdated.getCpf(), response.getBody().getCpf())
        );
    }

    @Test
    @DisplayName("alterar Throws PessoaNaoEncontradoException When pessoa not found")
    void alterar_ThrowsPessoaNaoEncontradoException_WhenPessoaNotFound() {
        BDDMockito.when(mockCrudPessoaService.alterar(anyLong(), any(PessoaRequest.class))).thenThrow(new PessoaNaoEncontradaException());
        PessoaRequest pessoaParaAlterar = PessoaCreator.mockPessoaRequestToUpdate();

        assertThrows(PessoaNaoEncontradaException.class, () -> pessoaController.alterar(99L, pessoaParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove pessoa when successful")
    void excluir_RemovePessoa_WhenSuccessful() {
        ResponseEntity<Void> response = pessoaController.excluir(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Throws PessoaNaoEncontradoException When pessoa not found")
    void excluir_ThrowsPessoaNaoEncontradoException_WhenPessoaNotFound() {
        BDDMockito.doThrow(PessoaNaoEncontradaException.class).when(mockCrudPessoaService).excluir(anyLong());

        assertThrows(PessoaNaoEncontradaException.class, () -> pessoaController.excluir(1L));
    }
}