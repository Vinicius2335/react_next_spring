package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.MarcaMapper;
import com.viniciusvieira.backend.api.representation.model.request.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.MarcaResponse;
import com.viniciusvieira.backend.domain.exception.MarcaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Marca;
import com.viniciusvieira.backend.domain.repository.MarcaRepository;
import com.viniciusvieira.backend.util.MarcaCreator;
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
@DisplayName("Teste Unitário para a classe CrudMarcaService")
class CrudMarcaServiceTest {

    @InjectMocks
    private CrudMarcaService crudMarcaService;

    @Mock
    private MarcaRepository mockMarcaRepository;
    @Mock
    private MarcaMapper mockMarcaMapper;

    private final Marca validMarca = MarcaCreator.mockMarca();
    private final MarcaResponse expectedMarca = MarcaCreator.mockMarcaResponse();
    private final MarcaResponse expectedMarcaUpdated = MarcaCreator.mockMarcaResponseUpdate();
    private final List<Marca> expectedListMarca = List.of(validMarca);

    @BeforeEach
    void setUp() {
        // findAll
        BDDMockito.when(mockMarcaRepository.findAll()).thenReturn(expectedListMarca);

        // findById
        BDDMockito.when(mockMarcaRepository.findById(anyLong())).thenReturn(Optional.of(validMarca));

        // saveAndFlush
        BDDMockito.when(mockMarcaRepository.saveAndFlush(any(Marca.class))).thenReturn(validMarca);

        // delete
        BDDMockito.doNothing().when(mockMarcaRepository).delete(any(Marca.class));

        // MarcaMappepr - toDomainMarca
        BDDMockito.when(mockMarcaMapper.toDomainMarca(any(MarcaRequest.class))).thenReturn(validMarca);

        // toMarcaResponse
        BDDMockito.when(mockMarcaMapper.toMarcaResponse(any(Marca.class))).thenReturn(expectedMarca);
    }

    @Test
    @DisplayName("buscarTodos Return a list of Marca When successful")
    void buscarTodos_ReturnListMarca_WhenSuccessful() {
        List<Marca> marcas = crudMarcaService.buscarTodos();

        assertAll(
                () -> assertNotNull(marcas),
                () -> assertFalse(marcas.isEmpty()),
                () -> assertEquals(1, marcas.size()),
                () -> assertTrue(marcas.contains(validMarca))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return marca When successful")
    void buscarPeloId_ReturnMarca_WhenSuccessful() {
        Marca marca = crudMarcaService.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(marca),
                () -> assertEquals(validMarca, marca)
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws MarcaNaoEncontradaException When marca not found")
    void buscarPeloId_ThrowsMarcaNaoEncontradaException_WhenMarcaNotFound() {
        BDDMockito.when(mockMarcaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MarcaNaoEncontradaException.class, () -> crudMarcaService.buscarPeloId(99L));
    }

    @Test
    @DisplayName("inserir Insert new marca When successful")
    void inserir_InsertNewMarca_WhenSuccessful() {
        MarcaRequest marcaParaInserir = MarcaCreator.mockMarcaRequestToSave();
        MarcaResponse marcaInserida = crudMarcaService.inserir(marcaParaInserir);

        assertAll(
                () -> assertNotNull(marcaInserida),
                () -> assertEquals(expectedMarca.getNome(), marcaInserida.getNome())
        );
    }

    @Test
    @DisplayName("alterar Update marca when successful")
    void alterar_UpdateMarca_WhenSuccessul() {
        Marca updateMarca = MarcaCreator.mockMarcaToUpdate(validMarca.getDataCriacao());
        BDDMockito.when(mockMarcaRepository.saveAndFlush(any(Marca.class))).thenReturn(updateMarca);
        BDDMockito.when(mockMarcaMapper.toMarcaResponse(any(Marca.class))).thenReturn(expectedMarcaUpdated);

        MarcaRequest marcaParaAlterar = MarcaCreator.mockMarcaRequestToUpdate();
        MarcaResponse marcaAlterada = crudMarcaService.alterar(1L, marcaParaAlterar);

        assertAll(
                () -> assertNotNull(marcaAlterada),
                () -> assertEquals(expectedMarcaUpdated.getNome(), marcaAlterada.getNome())
        );
    }

    @Test
    @DisplayName("alterar Throws MarcaNaoEncontradaException When marca not found")
    void alterar_ThrowsMarcaNaoEncontradaException_WhenMarcaNotFound() {
        BDDMockito.when(mockMarcaRepository.findById(anyLong())).thenReturn(Optional.empty());

        MarcaRequest marcaParaAlterar = MarcaCreator.mockMarcaRequestToUpdate();

        assertThrows(MarcaNaoEncontradaException.class, () -> crudMarcaService.alterar(99L, marcaParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove marca when successful")
    void excluir_RemoveMarca_WhenSuccessful() {
        assertDoesNotThrow(() -> crudMarcaService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws MarcaNaoEncontradaException When marca not found")
    void excluir_ThrowsMarcaNaoEncontradaException_WhenMarcaNotFound() {
        BDDMockito.when(mockMarcaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MarcaNaoEncontradaException.class, () -> crudMarcaService.excluir(1L));
    }
}