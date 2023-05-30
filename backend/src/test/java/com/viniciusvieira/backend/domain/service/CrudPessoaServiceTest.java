package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.PessoaMapper;
import com.viniciusvieira.backend.api.representation.model.request.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Cidade;
import com.viniciusvieira.backend.domain.model.Estado;
import com.viniciusvieira.backend.domain.model.Pessoa;
import com.viniciusvieira.backend.domain.repository.CidadeRepository;
import com.viniciusvieira.backend.domain.repository.EstadoRepository;
import com.viniciusvieira.backend.domain.repository.PessoaRepository;
import com.viniciusvieira.backend.util.CidadeCreator;
import com.viniciusvieira.backend.util.EstadoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste Unitário para a classe CrudPessoaService")
class CrudPessoaServiceTest {
    @InjectMocks
    private CrudPessoaService crudPessoaService;

    @Mock
    private PessoaRepository mockPessoaRepository;
    @Mock
    private PessoaMapper mockPessoaMapper;
    @Mock
    private CidadeRepository mockCidadeRepository;
    @Mock
    private EstadoRepository mockEstadoRepository;

    private final Pessoa validPessoa = PessoaCreator.mockPessoa();
    private final PessoaResponse expectedPessoa = PessoaCreator.mockPessoaResponse();
    private final PessoaResponse expectedPessoaUpdated = PessoaCreator.mockPessoaResponseUpdate();
    private final List<Pessoa> expectedListPessoa = List.of(validPessoa);

    @BeforeEach
    void setUp() {
        // EstadoRepository - saveAndFlush
        BDDMockito.when(mockEstadoRepository.saveAndFlush(any(Estado.class))).thenReturn(EstadoCreator.mockEstado());
        // CidadeRepository - saveAndFlush
        BDDMockito.when(mockCidadeRepository.saveAndFlush(any(Cidade.class))).thenReturn(CidadeCreator.mockCidade());

        // PessoaRepository
        // findAll
        BDDMockito.when(mockPessoaRepository.findAll()).thenReturn(expectedListPessoa);
        // findById
        BDDMockito.when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.of(validPessoa));
        // saveAndFlush
        BDDMockito.when(mockPessoaRepository.saveAndFlush(any(Pessoa.class))).thenReturn(validPessoa);
        // delete
        BDDMockito.doNothing().when(mockPessoaRepository).delete(any(Pessoa.class));

        // PessoaMapper
        // toDomainPessoa
        BDDMockito.when(mockPessoaMapper.toDomainPessoa(any(PessoaRequest.class))).thenReturn(validPessoa);
        // toPessoaResponse
        BDDMockito.when(mockPessoaMapper.toPessoaResponse(any(Pessoa.class))).thenReturn(expectedPessoa);

    }

    @Test
    @DisplayName("buscarTodos Return a list of Pessoa When successful")
    void buscarTodos_ReturnListPessoa_WhenSuccessful() {
        List<Pessoa> pessoas = crudPessoaService.buscarTodos();

        assertAll(
                () -> assertNotNull(pessoas),
                () -> assertFalse(pessoas.isEmpty()),
                () -> assertEquals(1, pessoas.size()),
                () -> assertTrue(pessoas.contains(validPessoa))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return pessoa When successful")
    void buscarPeloId_ReturnPessoa_WhenSuccessful() {
        Pessoa pessoa = crudPessoaService.buscarPorId(1L);

        assertAll(
                () -> assertNotNull(pessoa),
                () -> assertEquals(validPessoa, pessoa)
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws PessoaNaoEncontradaException When pessoa not found")
    void buscarPeloId_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound() {
        BDDMockito.when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PessoaNaoEncontradaException.class, () -> crudPessoaService.buscarPorId(99L));
    }

    @Test
    @DisplayName("inserir Insert new pessoa When successful")
    void inserir_InsertNewPessoa_WhenSuccessful() {
        PessoaRequest pessoaParaInserir = PessoaCreator.mockPessoaRequestToSave();
        PessoaResponse pessoaInserida = crudPessoaService.inserir(pessoaParaInserir);

        assertAll(
                () -> assertNotNull(pessoaInserida),
                () -> assertEquals(expectedPessoa.getNome(), pessoaInserida.getNome())
        );
    }

    @Test
    @DisplayName("alterar Update pessoa when successful")
    void alterar_UpdatePessoa_WhenSuccessul() {
        Pessoa updatePessoa = PessoaCreator.mockPessoaToUpdate(validPessoa.getDataCriacao());
        BDDMockito.when(mockPessoaRepository.saveAndFlush(any(Pessoa.class))).thenReturn(updatePessoa);
        BDDMockito.when(mockPessoaMapper.toPessoaResponse(any(Pessoa.class))).thenReturn(expectedPessoaUpdated);

        PessoaRequest pessoaParaAlterar = PessoaCreator.mockPessoaRequestToUpdate();
        PessoaResponse pessoaAlterada = crudPessoaService.alterar(1L, pessoaParaAlterar);

        assertAll(
                () -> assertNotNull(pessoaAlterada),
                () -> assertEquals(expectedPessoaUpdated.getNome(), pessoaAlterada.getNome())
        );
    }

    @Test
    @DisplayName("alterar Throws PessoaNaoEncontradaException When pessoa not found")
    void alterar_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound() {
        BDDMockito.when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.empty());

        PessoaRequest pessoaParaAlterar = PessoaCreator.mockPessoaRequestToUpdate();

        assertThrows(PessoaNaoEncontradaException.class, () -> crudPessoaService.alterar(99L, pessoaParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove pessoa when successful")
    void excluir_RemovePessoa_WhenSuccessful() {
        assertDoesNotThrow(() -> crudPessoaService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws PessoaNaoEncontradaException When pessoa not found")
    void excluir_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound() {
        BDDMockito.when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PessoaNaoEncontradaException.class, () -> crudPessoaService.excluir(1L));
    }

}