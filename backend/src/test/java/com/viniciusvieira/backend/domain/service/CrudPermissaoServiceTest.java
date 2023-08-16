package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.usuario.PermissaoMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PermissaoResponse;
import com.viniciusvieira.backend.domain.exception.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.domain.service.usuario.CrudPermissaoService;
import com.viniciusvieira.backend.util.PermissaoCreator;
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
@DisplayName("Teste Unitário para a classe CrudPermissaoService")
class CrudPermissaoServiceTest {
    @InjectMocks
    private CrudPermissaoService crudPermissaoService;
    
    @Mock
    private PermissaoRepository mockPermissaoRepository;
    @Mock
    private PermissaoMapper mockPermissaoMapper;

    private final Permissao validPermissao = PermissaoCreator.mockPermissao();
    private final PermissaoResponse expectedPermissao = PermissaoCreator.mockPermissaoResponse();
    private final PermissaoResponse expectedPermissaoUpdated = PermissaoCreator.mockPermissaoResponseUpdated();
    private final List<Permissao> expectedListPermissao = List.of(validPermissao);

    @BeforeEach
    void setUp() {
        // PermissaoRepository
        // findAll
        BDDMockito.when(mockPermissaoRepository.findAll()).thenReturn(expectedListPermissao);
        // findById
        BDDMockito.when(mockPermissaoRepository.findById(anyLong())).thenReturn(Optional.of(validPermissao));
        // saveAndFlush
        BDDMockito.when(mockPermissaoRepository.saveAndFlush(any(Permissao.class))).thenReturn(validPermissao);
        // delete
        BDDMockito.doNothing().when(mockPermissaoRepository).delete(any(Permissao.class));
        // findByNome
        BDDMockito.when(mockPermissaoRepository.findByNome(anyString())).thenReturn(Optional.of(validPermissao));
        
        // PermissaoMappepr
        // toDomainPermissao
        BDDMockito.when(mockPermissaoMapper.toDomainPermissao(any(PermissaoRequest.class))).thenReturn(validPermissao);
        // toPermissaoResponse
        BDDMockito.when(mockPermissaoMapper.toPermissaoResponse(any(Permissao.class))).thenReturn(expectedPermissao);
    }

    @Test
    @DisplayName("buscarTodos Return a list of Permissao When successful")
    void buscarTodos_ReturnListPermissao_WhenSuccessful() {
        List<Permissao> permissaos = crudPermissaoService.buscarTodos();

        assertAll(
                () -> assertNotNull(permissaos),
                () -> assertFalse(permissaos.isEmpty()),
                () -> assertEquals(1, permissaos.size()),
                () -> assertTrue(permissaos.contains(validPermissao))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return permissao When successful")
    void buscarPeloId_ReturnPermissao_WhenSuccessful() {
        Permissao permissao = crudPermissaoService.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(permissao),
                () -> assertEquals(validPermissao, permissao)
        );
    }

    @Test
    @DisplayName("buscarPeloNome Return permissao When successful")
    void buscarPeloNome_ReturnPermissao_WhenSuccessful() {
        Permissao permissao = crudPermissaoService.buscarPeloNome(validPermissao.getNome());

        assertAll(
                () -> assertNotNull(permissao),
                () -> assertEquals(validPermissao, permissao)
        );
    }

    @Test
    @DisplayName("buscarPeloNome Throws PermissaoNaoEncontradaException When permissao not found")
    void buscarPeloNome_ThrowsPermissaoNaoEncontradaException_WhenPermissaoNotFound() {
        BDDMockito.when(mockPermissaoRepository.findByNome(anyString())).thenReturn(Optional.empty());

        String nome = validPermissao.getNome();
        assertThrows(PermissaoNaoEncontradaException.class, () -> crudPermissaoService.buscarPeloNome(nome));
    }

    @Test
    @DisplayName("buscarPeloId Throws PermissaoNaoEncontradaException When permissao not found")
    void buscarPeloId_ThrowsPermissaoNaoEncontradaException_WhenPermissaoNotFound() {
        BDDMockito.when(mockPermissaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PermissaoNaoEncontradaException.class, () -> crudPermissaoService.buscarPeloId(99L));
    }

    @Test
    @DisplayName("inserir Insert new permissao When successful")
    void inserir_InsertNewPermissao_WhenSuccessful() {
        PermissaoRequest permissaoParaInserir = PermissaoCreator.mockPermissaoRequest();
        PermissaoResponse permissaoInserida = crudPermissaoService.inserir(permissaoParaInserir);

        assertAll(
                () -> assertNotNull(permissaoInserida),
                () -> assertEquals(expectedPermissao.getNome(), permissaoInserida.getNome())
        );
    }

    @Test
    @DisplayName("alterar Update permissao when successful")
    void alterar_UpdatePermissao_WhenSuccessul() {
        Permissao updatePermissao = PermissaoCreator.mockPermissaoUpdated();
        BDDMockito.when(mockPermissaoRepository.saveAndFlush(any(Permissao.class))).thenReturn(updatePermissao);
        BDDMockito.when(mockPermissaoMapper.toPermissaoResponse(any(Permissao.class))).thenReturn(expectedPermissaoUpdated);

        PermissaoRequest permissaoParaAlterar = PermissaoCreator.mockPermissaoRequestToUpdate();
        PermissaoResponse permissaoAlterada = crudPermissaoService.alterar(1L, permissaoParaAlterar);

        assertAll(
                () -> assertNotNull(permissaoAlterada),
                () -> assertEquals(expectedPermissaoUpdated.getNome(), permissaoAlterada.getNome())
        );
    }

    @Test
    @DisplayName("alterar Throws PermissaoNaoEncontradaException When permissao not found")
    void alterar_ThrowsPermissaoNaoEncontradaException_WhenPermissaoNotFound() {
        BDDMockito.when(mockPermissaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        PermissaoRequest permissaoParaAlterar = PermissaoCreator.mockPermissaoRequestToUpdate();

        assertThrows(PermissaoNaoEncontradaException.class, () -> crudPermissaoService.alterar(99L, permissaoParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove permissao when successful")
    void excluir_RemovePermissao_WhenSuccessful() {
        assertDoesNotThrow(() -> crudPermissaoService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws PermissaoNaoEncontradaException When permissao not found")
    void excluir_ThrowsPermissaoNaoEncontradaException_WhenPermissaoNotFound() {
        BDDMockito.when(mockPermissaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PermissaoNaoEncontradaException.class, () -> crudPermissaoService.excluir(1L));
    }
}