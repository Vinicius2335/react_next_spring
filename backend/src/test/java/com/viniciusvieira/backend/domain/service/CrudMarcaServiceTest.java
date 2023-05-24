package com.viniciusvieira.backend.domain.service;

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

    private final Marca expectedMarca = MarcaCreator.mockValidMarca();
    private final List<Marca> expectedListMarca = List.of(expectedMarca);

    @BeforeEach
    void setUp() {
        // findAll
        BDDMockito.when(mockMarcaRepository.findAll()).thenReturn(expectedListMarca);

        // findById
        BDDMockito.when(mockMarcaRepository.findById(anyLong())).thenReturn(Optional.of(expectedMarca));

        // saveAndFlush
        BDDMockito.when(mockMarcaRepository.saveAndFlush(any(Marca.class))).thenReturn(expectedMarca);

        // delete
        BDDMockito.doNothing().when(mockMarcaRepository).delete(any(Marca.class));
    }

    @Test
    @DisplayName("buscarTodos Return a list of Marca When successful")
    void buscarTodos_ReturnListMarca_WhenSuccessful() {
        List<Marca> marcas = crudMarcaService.buscarTodos();

        assertAll(
                () -> assertNotNull(marcas),
                () -> assertFalse(marcas.isEmpty()),
                () -> assertEquals(1, marcas.size()),
                () -> assertTrue(marcas.contains(expectedMarca))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return marca When successful")
    void buscarPeloId_ReturnMarca_WhenSuccessful() {
        Marca marca = crudMarcaService.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(marca),
                () -> assertEquals(expectedMarca, marca)
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
        Marca marcaParaInserir = MarcaCreator.mockValidMarca();
        Marca marcaInserida = crudMarcaService.inserir(marcaParaInserir);

        assertAll(
                () -> assertNotNull(marcaInserida),
                () -> assertEquals(expectedMarca, marcaInserida)
        );
    }

    @Test
    @DisplayName("alterar Update marca when successful")
    void alterar_UpdateMarca_WhenSuccessul() {
        Marca marcaParaAlterar = MarcaCreator.mockMarcaToUpdate(expectedMarca.getDataCriacao());
        Marca marcaAlterada = crudMarcaService.alterar(1L, marcaParaAlterar);
        marcaAlterada.setDataAtualizacao(marcaParaAlterar.getDataAtualizacao());

        assertAll(
                () -> assertNotNull(marcaAlterada),
                () -> assertEquals(marcaParaAlterar, marcaAlterada)
        );
    }

    @Test
    @DisplayName("alterar Throws MarcaNaoEncontradaException When marca not found")
    void alterar_ThrowsMarcaNaoEncontradaException_WhenMarcaNotFound() {
        BDDMockito.when(mockMarcaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Marca marcaParaAlterar = MarcaCreator.mockMarcaToUpdate(expectedMarca.getDataCriacao());

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