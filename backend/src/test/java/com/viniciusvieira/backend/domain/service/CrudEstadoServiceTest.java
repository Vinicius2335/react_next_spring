package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.usuario.EstadoMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.EstadoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.EstadoResponse;
import com.viniciusvieira.backend.domain.exception.EstadoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.usuario.Estado;
import com.viniciusvieira.backend.domain.repository.usuario.EstadoRepository;
import com.viniciusvieira.backend.domain.service.usuario.CrudEstadoService;
import com.viniciusvieira.backend.util.EstadoCreator;
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
@DisplayName("Teste Unitário para a classe CrudEstadoService")
class CrudEstadoServiceTest {
    @InjectMocks
    private CrudEstadoService crudEstadoService;

    @Mock
    private EstadoRepository mockEstadoRespository;
    @Mock
    private EstadoMapper mockEstadoMapper;

    private final Estado validEstado = EstadoCreator.mockEstado();
    private final EstadoResponse expectedEstado = EstadoCreator.mockEstadoResponse();
    private final EstadoResponse expectedEstadoUpdated = EstadoCreator.mockEstadoResponseUpdate();
    private final List<Estado> expectedListEstado = List.of(validEstado);

    @BeforeEach
    void setUp() {
        // findAll
        BDDMockito.when(mockEstadoRespository.findAll()).thenReturn(expectedListEstado);

        // findById
        BDDMockito.when(mockEstadoRespository.findById(anyLong())).thenReturn(Optional.of(validEstado));

        // saveAndFlush
        BDDMockito.when(mockEstadoRespository.saveAndFlush(any(Estado.class))).thenReturn(validEstado);

        // delete
        BDDMockito.doNothing().when(mockEstadoRespository).delete(any(Estado.class));

        // EstadoMapper - toDomainEstado
        BDDMockito.when(mockEstadoMapper.toDomainEstado(any(EstadoRequest.class))).thenReturn(validEstado);

        // toEstadoResponse
        BDDMockito.when(mockEstadoMapper.toEstadoResponse(any(Estado.class))).thenReturn(expectedEstado);
    }

    @Test
    @DisplayName("buscarTodos Return a list of Estado When successful")
    void buscarTodos_ReturnListEstado_WhenSuccessful() {
        List<Estado> estados = crudEstadoService.buscarTodos();
        crudEstadoService.buscarTodos();

        assertAll(
                () -> assertNotNull(estados),
                () -> assertFalse(estados.isEmpty()),
                () -> assertEquals(1, estados.size()),
                () -> assertTrue(estados.contains(validEstado))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return estado When successful")
    void buscarPeloId_ReturnEstado_WhenSuccessful() {
        Estado estado = crudEstadoService.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(estado),
                () -> assertEquals(validEstado, estado)
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws EstadoNaoEncontradoException When estado not found")
    void buscarPeloId_ThrowsEstadoNaoEncontradoException_WhenEstadoNotFound() {
        BDDMockito.when(mockEstadoRespository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EstadoNaoEncontradoException.class, () -> crudEstadoService.buscarPeloId(99L));
    }

    @Test
    @DisplayName("inserir Insert new estado When successful")
    void inserir_InsertNewEstado_WhenSuccessful() {
        EstadoRequest estadoParaInserir = EstadoCreator.mockEstadoRequestToSave();
        EstadoResponse estadoInserida = crudEstadoService.inserir(estadoParaInserir);

        assertAll(
                () -> assertNotNull(estadoInserida),
                () -> assertEquals(expectedEstado.getNome(), estadoInserida.getNome()),
                () -> assertEquals(expectedEstado.getSigla(), estadoInserida.getSigla())
        );
    }

    @Test
    @DisplayName("alterar Update estado when successful")
    void alterar_UpdateEstado_WhenSuccessul() {
        Estado update = EstadoCreator.mockEstadoToUpdate(validEstado.getDataCriacao());
        BDDMockito.when(mockEstadoRespository.saveAndFlush(any(Estado.class))).thenReturn(update);
        BDDMockito.when(mockEstadoMapper.toEstadoResponse(any(Estado.class))).thenReturn(expectedEstadoUpdated);

        EstadoRequest estadoParaAlterar = EstadoCreator.mockEstadoRequestToUpdate();
        EstadoResponse estadoAlterada = crudEstadoService.alterar(1L, estadoParaAlterar);

        assertAll(
                () -> assertNotNull(estadoAlterada),
                () -> assertEquals(expectedEstadoUpdated.getNome(), estadoAlterada.getNome()),
                () -> assertEquals(expectedEstadoUpdated.getSigla(), estadoAlterada.getSigla())
        );
    }

    @Test
    @DisplayName("alterar Throws EstadoNaoEncontradoException When estado not found")
    void alterar_ThrowsEstadoNaoEncontradoException_WhenEstadoNotFound() {
        BDDMockito.when(mockEstadoRespository.findById(anyLong())).thenReturn(Optional.empty());

        EstadoRequest estadoParaAlterar = EstadoCreator.mockEstadoRequestToUpdate();

        assertThrows(EstadoNaoEncontradoException.class, () -> crudEstadoService.alterar(99L, estadoParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove estado when successful")
    void excluir_RemoveEstado_WhenSuccessful() {
        assertDoesNotThrow(() -> crudEstadoService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws EstadoNaoEncontradoException When estado not found")
    void excluir_ThrowsEstadoNaoEncontradoException_WhenEstadoNotFound() {
        BDDMockito.when(mockEstadoRespository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EstadoNaoEncontradoException.class, () -> crudEstadoService.excluir(1L));
    }
}