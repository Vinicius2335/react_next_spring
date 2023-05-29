package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.CidadeMapper;
import com.viniciusvieira.backend.api.representation.model.request.CidadeRequest;
import com.viniciusvieira.backend.api.representation.model.response.CidadeResponse;
import com.viniciusvieira.backend.domain.exception.CidadeNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Cidade;
import com.viniciusvieira.backend.domain.repository.CidadeRepository;
import com.viniciusvieira.backend.util.CidadeCreator;
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
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste Unitário para classe CrudServiceCidade")
class CrudCidadeServiceTest {
    @InjectMocks
    private CrudCidadeService crudCidadeService;
    @Mock
    private CidadeRepository mockCidadeRepository;
    @Mock
    private CidadeMapper mockCidadeMapper;

    private final Cidade validCidade = CidadeCreator.mockCidade();
    private final CidadeResponse expectedCidade = CidadeCreator.mockCidadeResponse();
    private final CidadeResponse expectedCidadeUpdated = CidadeCreator.mockCidadeResponseUpdated();
    private final List<Cidade> expectedListCidade = List.of(validCidade);

    @BeforeEach
    void setUp() {
        // Cidade Repository
        // findAll
        BDDMockito.when(mockCidadeRepository.findAll()).thenReturn(expectedListCidade);
        // findById
        BDDMockito.when(mockCidadeRepository.findById(anyLong())).thenReturn(Optional.of(validCidade));
        // saveAndFlush
        BDDMockito.when(mockCidadeRepository.saveAndFlush(any(Cidade.class))).thenReturn(validCidade);
        // delete
        BDDMockito.doNothing().when(mockCidadeRepository).delete(any(Cidade.class));

        // Cidade Mapper
        // toDomainCidade
        BDDMockito.when(mockCidadeMapper.toDomainCidade(any(CidadeRequest.class))).thenReturn(validCidade);
        // toCidadeResponse
        BDDMockito.when(mockCidadeMapper.toCidadeResponse(any(Cidade.class))).thenReturn(expectedCidade);

    }

    @Test
    @DisplayName("buscarTodos Return a list of Cidade When successful")
    void buscarTodos_ReturnListCidade_WhenSuccessful() {
        List<Cidade> cidades = crudCidadeService.buscarTodos();

        assertAll(
                () -> assertNotNull(cidades),
                () -> assertFalse(cidades.isEmpty()),
                () -> assertTrue(cidades.contains(validCidade))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return cidade When successful")
    void buscarPeloId_ReturnCidade_WhenSuccessful() {
        Cidade cidadeEncontrada = crudCidadeService.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(cidadeEncontrada),
                () -> assertEquals(expectedCidade.getNome(), cidadeEncontrada.getNome()),
                () -> assertEquals(expectedCidade.getNomeEstado(), cidadeEncontrada.getEstado().getNome())
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws CidadeNaoEncontradoException When cidade not found")
    void buscarPeloId_ThrowsCidadeNaoEncontradoException_WhenCidadeNotFound() {
        BDDMockito.when(mockCidadeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CidadeNaoEncontradaException.class, () -> crudCidadeService.buscarPeloId(99L));
    }

    @Test
    @DisplayName("inserir Insert new cidade When successful")
    void inserir_InsertNewCidade_WhenSuccessful() {
        CidadeResponse cidadeInserida = crudCidadeService.inserir(CidadeCreator.mockCidadeRequestToSave());

        assertAll(
                () -> assertNotNull(cidadeInserida),
                () -> assertEquals(expectedCidade.getNome(), cidadeInserida.getNome()),
                () -> assertEquals(expectedCidade.getNomeEstado(), cidadeInserida.getNomeEstado())
        );
    }

    @Test
    @DisplayName("alterar Update cidade when successful")
    void alterar_UpdateCidade_WhenSuccessul() {
        BDDMockito.when(mockCidadeRepository.saveAndFlush(any(Cidade.class))).thenReturn(CidadeCreator
                .mockCidateToUpdate(validCidade.getDataCriacao()));
        BDDMockito.when(mockCidadeMapper.toCidadeResponse(any(Cidade.class)))
                .thenReturn(expectedCidadeUpdated);

        CidadeResponse cidadeAlterada = crudCidadeService.alterar(1L, CidadeCreator.mockCidadeRequestToUpdate());

        assertAll(
                () -> assertNotNull(cidadeAlterada),
                () -> assertEquals(expectedCidadeUpdated.getNome(), cidadeAlterada.getNome()),
                () -> assertEquals(expectedCidadeUpdated.getNomeEstado(), cidadeAlterada.getNomeEstado())
        );
    }

    @Test
    @DisplayName("alterar Throws CidadeNaoEncontradoException When cidade not found")
    void alterar_ThrowsCidadeNaoEncontradoException_WhenCidadeNotFound() {
        BDDMockito.when(mockCidadeRepository.findById(anyLong())).thenReturn(Optional.empty());
        CidadeRequest cidadeParaAlterar = CidadeCreator.mockCidadeRequestToUpdate();

        assertThrows(CidadeNaoEncontradaException.class, () -> crudCidadeService.alterar(99L, cidadeParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove cidade when successful")
    void excluir_RemoveCidade_WhenSuccessful() {
        assertDoesNotThrow(() -> crudCidadeService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws CidadeNaoEncontradoException When cidade not found")
    void excluir_ThrowsCidadeNaoEncontradoException_WhenCidadeNotFound() {
        BDDMockito.when(mockCidadeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CidadeNaoEncontradaException.class, () -> crudCidadeService.excluir(99L));
    }
}