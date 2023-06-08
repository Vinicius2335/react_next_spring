package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.CarrinhoDeCompraMapper;
import com.viniciusvieira.backend.api.representation.model.request.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.exception.CarrinhoDeCompraNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.model.Cidade;
import com.viniciusvieira.backend.domain.model.Estado;
import com.viniciusvieira.backend.domain.model.Pessoa;
import com.viniciusvieira.backend.domain.repository.CarrinhoCompraRepository;
import com.viniciusvieira.backend.domain.repository.CidadeRepository;
import com.viniciusvieira.backend.domain.repository.EstadoRepository;
import com.viniciusvieira.backend.domain.repository.PessoaRepository;
import com.viniciusvieira.backend.util.CarrinhoDeCompraCreator;
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
@DisplayName("Teste Unitário para a classe CrudCarrinhoDeCompraService")
class CrudCarrinhoDeCompraServiceTest {
    @InjectMocks
    private CrudCarrinhoDeCompraService crudCarrinhoDeCompraService;

    @Mock
    private CarrinhoCompraRepository mockCarrinhoCompraRepository;
    @Mock
    private CarrinhoDeCompraMapper mockCarrinhoDeCompraMapper;
    @Mock
    private CidadeRepository mockCidadeRepository;
    @Mock
    private EstadoRepository mockEstadoRepository;
    @Mock
    private PessoaRepository mockPessoaRepository;

    private final CarrinhoDeCompra validCarrinhoDeCompra = CarrinhoDeCompraCreator.mockCarrinhoDeCompra();
    private final CarrinhoDeCompraResponse expectedCarrinhoDeCompra = CarrinhoDeCompraCreator.mockCarrinhoDeCompraResponse();
    private final CarrinhoDeCompraResponse expectedCarrinhoDeCompraUpdated = CarrinhoDeCompraCreator.mockCarrinhoDeCompraResponseUpdated();
    private final List<CarrinhoDeCompra> expectedListCarrinhoDeCompra = List.of(validCarrinhoDeCompra);

    @BeforeEach
    void setUp() {
        // EstadoRepository - saveAndFlush
        BDDMockito.when(mockEstadoRepository.saveAndFlush(any(Estado.class))).thenReturn(EstadoCreator.mockEstado());
        // CidadeRepository - saveAndFlush
        BDDMockito.when(mockCidadeRepository.saveAndFlush(any(Cidade.class))).thenReturn(CidadeCreator.mockCidade());
        // PessoaRepository - saveAndFlush
        BDDMockito.when(mockPessoaRepository.saveAndFlush(any(Pessoa.class))).thenReturn(PessoaCreator.mockPessoa());

        // CarrinhoDeCompraRepository
        // findAll
        BDDMockito.when(mockCarrinhoCompraRepository.findAll()).thenReturn(expectedListCarrinhoDeCompra);
        // findById
        BDDMockito.when(mockCarrinhoCompraRepository.findById(anyLong())).thenReturn(Optional.of(validCarrinhoDeCompra));
        // saveAndFlush
        BDDMockito.when(mockCarrinhoCompraRepository.saveAndFlush(any(CarrinhoDeCompra.class))).thenReturn(validCarrinhoDeCompra);
        // delete
        BDDMockito.doNothing().when(mockCarrinhoCompraRepository).delete(any(CarrinhoDeCompra.class));

        // CarrinhoDeCompraRepository
        // toCarrinhoDeCompraResponse
        BDDMockito.when(mockCarrinhoDeCompraMapper.toCarrinhoDeCompraResponse(any(CarrinhoDeCompra.class)))
                .thenReturn(expectedCarrinhoDeCompra);
        // toDomainCarrinhoDeCompra
        BDDMockito.when(mockCarrinhoDeCompraMapper.toDomainCarrinhoDeCompra(any(CarrinhoDeCompraRequest.class)))
                .thenReturn(validCarrinhoDeCompra);
    }

    @Test
    @DisplayName("buscarTodos Return a list of CarrinhoDeCompra When successful")
    void buscarTodos_ReturnListCarrinhoDeCompra_WhenSuccessful() {
        List<CarrinhoDeCompra> carrinhoDeCompras = crudCarrinhoDeCompraService.buscarTodos();

        assertAll(
                () -> assertNotNull(carrinhoDeCompras),
                () -> assertFalse(carrinhoDeCompras.isEmpty()),
                () -> assertEquals(1, carrinhoDeCompras.size()),
                () -> assertTrue(carrinhoDeCompras.contains(validCarrinhoDeCompra))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return carrinhoDeCompra When successful")
    void buscarPeloId_ReturnCarrinhoDeCompra_WhenSuccessful() {
        CarrinhoDeCompra carrinhoDeCompra = crudCarrinhoDeCompraService.buscarPeloId(1L);

        assertAll(
                () -> assertNotNull(carrinhoDeCompra),
                () -> assertEquals(validCarrinhoDeCompra, carrinhoDeCompra)
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws CarrinhoDeCompraNaoEncontradoException When carrinhoDeCompra not found")
    void buscarPeloId_ThrowsCarrinhoDeCompraNaoEncontradaException_WhenCarrinhoDeCompraNotFound() {
        BDDMockito.when(mockCarrinhoCompraRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CarrinhoDeCompraNaoEncontradoException.class, () -> crudCarrinhoDeCompraService.buscarPeloId(99L));
    }

    @Test
    @DisplayName("inserir Insert new carrinhoDeCompra When successful")
    void inserir_InsertNewCarrinhoDeCompra_WhenSuccessful() {
        CarrinhoDeCompraRequest carrinhoDeCompraParaInserir = CarrinhoDeCompraCreator.mockCarrinhoDeCompraRequest();
        CarrinhoDeCompraResponse carrinhoDeCompraInserida = crudCarrinhoDeCompraService.inserir(carrinhoDeCompraParaInserir);

        assertAll(
                () -> assertNotNull(carrinhoDeCompraInserida),
                () -> assertEquals(expectedCarrinhoDeCompra.getSituacao(), carrinhoDeCompraInserida.getSituacao()),
                () -> assertEquals(expectedCarrinhoDeCompra.getObservacao(), carrinhoDeCompraInserida.getObservacao())
        );
    }

    @Test
    @DisplayName("alterar Update carrinhoDeCompra when successful")
    void alterar_UpdateCarrinhoDeCompra_WhenSuccessul() {
        CarrinhoDeCompra updateCarrinhoDeCompra = CarrinhoDeCompraCreator.mockCarrinhoDeCompraUpdate();
        BDDMockito.when(mockCarrinhoCompraRepository.saveAndFlush(any(CarrinhoDeCompra.class))).thenReturn(updateCarrinhoDeCompra);
        BDDMockito.when(mockCarrinhoDeCompraMapper.toCarrinhoDeCompraResponse(any(CarrinhoDeCompra.class))).thenReturn(expectedCarrinhoDeCompraUpdated);

        CarrinhoDeCompraRequest carrinhoDeCompraParaAlterar = CarrinhoDeCompraCreator.mockCarrinhoDeCompraRequestToUpdate();
        CarrinhoDeCompraResponse carrinhoDeCompraAlterada = crudCarrinhoDeCompraService.alterar(1L, carrinhoDeCompraParaAlterar);

        assertAll(
                () -> assertNotNull(carrinhoDeCompraAlterada),
                () -> assertEquals(expectedCarrinhoDeCompraUpdated.getObservacao(), carrinhoDeCompraAlterada.getObservacao()),
                () -> assertEquals(expectedCarrinhoDeCompraUpdated.getSituacao(), carrinhoDeCompraAlterada.getSituacao())
        );
    }

    @Test
    @DisplayName("alterar Throws CarrinhoDeCompraNaoEncontradoException When carrinhoDeCompra not found")
    void alterar_ThrowsCarrinhoDeCompraNaoEncontradaException_WhenCarrinhoDeCompraNotFound() {
        BDDMockito.when(mockCarrinhoCompraRepository.findById(anyLong())).thenReturn(Optional.empty());

        CarrinhoDeCompraRequest carrinhoDeCompraParaAlterar = CarrinhoDeCompraCreator.mockCarrinhoDeCompraRequestToUpdate();

        assertThrows(CarrinhoDeCompraNaoEncontradoException.class,
                () -> crudCarrinhoDeCompraService.alterar(99L, carrinhoDeCompraParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove carrinhoDeCompra when successful")
    void excluir_RemoveCarrinhoDeCompra_WhenSuccessful() {
        assertDoesNotThrow(() -> crudCarrinhoDeCompraService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws CarrinhoDeCompraNaoEncontradoException When carrinhoDeCompra not found")
    void excluir_ThrowsCarrinhoDeCompraNaoEncontradaException_WhenCarrinhoDeCompraNotFound() {
        BDDMockito.when(mockCarrinhoCompraRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CarrinhoDeCompraNaoEncontradoException.class, () -> crudCarrinhoDeCompraService.excluir(1L));
    }
}