package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.controller.venda.CarrinhoDeCompraController;
import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.exception.CarrinhoDeCompraNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.model.usuario.Cidade;
import com.viniciusvieira.backend.domain.model.usuario.Estado;
import com.viniciusvieira.backend.domain.repository.venda.CarrinhoCompraRepository;
import com.viniciusvieira.backend.domain.repository.usuario.CidadeRepository;
import com.viniciusvieira.backend.domain.repository.usuario.EstadoRepository;
import com.viniciusvieira.backend.domain.service.venda.CrudCarrinhoDeCompraService;
import com.viniciusvieira.backend.util.CarrinhoDeCompraCreator;
import com.viniciusvieira.backend.util.CidadeCreator;
import com.viniciusvieira.backend.util.EstadoCreator;
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
@DisplayName("Teste Unitário para a classe CarrinhoDeCompraController")
class CarrinhoDeCompraControllerTest {
    @InjectMocks
    private CarrinhoDeCompraController carrinhoDeCompraController;

    @Mock
    private CrudCarrinhoDeCompraService mockCarrinhoDeCompraService;
    @Mock
    private CidadeRepository mockCidadeRepository;
    @Mock
    private EstadoRepository mockEstadoRepository;
    @Mock
    private CarrinhoCompraRepository mockCarrinhoDeCompraRepository;

    private final CarrinhoDeCompra validCarrinhoDeCompra = CarrinhoDeCompraCreator.mockCarrinhoDeCompra();
    private final CarrinhoDeCompraResponse expectedCarrinhoDeCompra = CarrinhoDeCompraCreator.mockCarrinhoDeCompraResponse();
    private final CarrinhoDeCompraResponse expectedCarrinhoDeCompraUpdated = CarrinhoDeCompraCreator.mockCarrinhoDeCompraResponseUpdated();
    private final List<CarrinhoDeCompra> expectedListCarrinhoDeCompras = List.of(validCarrinhoDeCompra);

    @BeforeEach
    void setUp() {
        // EstadoRepository - saveAndFlush
        BDDMockito.when(mockEstadoRepository.saveAndFlush(any(Estado.class))).thenReturn(EstadoCreator.mockEstado());
        // CidadeRepository - saveAndFlush
        BDDMockito.when(mockCidadeRepository.saveAndFlush(any(Cidade.class))).thenReturn(CidadeCreator.mockCidade());
        // CarrinhoDeCompraRepository - saveAndFlush
        BDDMockito.when(mockCarrinhoDeCompraRepository.saveAndFlush(any(CarrinhoDeCompra.class))).thenReturn(CarrinhoDeCompraCreator.mockCarrinhoDeCompra());

        // CrudCarrinhoDeCompraService
        // buscarTodos
        BDDMockito.when(mockCarrinhoDeCompraService.buscarTodos()).thenReturn(expectedListCarrinhoDeCompras);
        // buscarPeloId
        BDDMockito.when(mockCarrinhoDeCompraService.buscarPeloId(anyLong())).thenReturn(validCarrinhoDeCompra);
        // alterar
        BDDMockito.when(mockCarrinhoDeCompraService.alterar(anyLong(), any(CarrinhoDeCompraRequest.class)))
                .thenReturn(expectedCarrinhoDeCompraUpdated);
        // inserir
        BDDMockito.when(mockCarrinhoDeCompraService.inserir(any(CarrinhoDeCompraRequest.class))).thenReturn(expectedCarrinhoDeCompra);
        // excluir
        BDDMockito.doNothing().when(mockCarrinhoDeCompraService).excluir(anyLong());
    }

    @Test
    @DisplayName("buscarTodos Return list of carrinhoDeCompra When successful")
    void buscarTodos_ReturnListCarrinhoDeCompra_WhenSuccessful() {
        ResponseEntity<List<CarrinhoDeCompra>> response = carrinhoDeCompraController.buscarTodos();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedListCarrinhoDeCompras, response.getBody())
        );
    }

    @Test
    @DisplayName("inserir Insert new carrinhoDeCompra When successful")
    void inserir_InsertNewCarrinhoDeCompra_WhenSuccessful() {
        CarrinhoDeCompraRequest carrinhoDeCompraParaSalvar = CarrinhoDeCompraCreator.mockCarrinhoDeCompraRequest();
        ResponseEntity<CarrinhoDeCompraResponse> response = carrinhoDeCompraController.inserir(carrinhoDeCompraParaSalvar);

        System.out.println(response.getBody());

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(expectedCarrinhoDeCompra.getSituacao(), response.getBody().getSituacao()),
                () -> assertEquals(expectedCarrinhoDeCompra.getObservacao(), response.getBody().getObservacao())
        );
    }

    @Test
    @DisplayName("alterar Update carrinhoDeCompra when successful")
    void alterar_UpdateCarrinhoDeCompra_WhenSuccessul() {
        CarrinhoDeCompraRequest carrinhoDeCompraParaAlterar = CarrinhoDeCompraCreator.mockCarrinhoDeCompraRequestToUpdate();
        ResponseEntity<CarrinhoDeCompraResponse> response = carrinhoDeCompraController.alterar(1L, carrinhoDeCompraParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedCarrinhoDeCompraUpdated.getSituacao(), response.getBody().getSituacao()),
                () -> assertEquals(expectedCarrinhoDeCompraUpdated.getObservacao(), response.getBody().getObservacao())
        );
    }

    @Test
    @DisplayName("alterar Throws CarrinhoDeCompraNaoEncontradoException When carrinhoDeCompra not found")
    void alterar_ThrowsCarrinhoDeCompraNaoEncontradoException_WhenCarrinhoDeCompraNotFound() {
        BDDMockito.when(mockCarrinhoDeCompraService.alterar(anyLong(), any(CarrinhoDeCompraRequest.class))).thenThrow(new CarrinhoDeCompraNaoEncontradoException());
        CarrinhoDeCompraRequest carrinhoDeCompraParaAlterar = CarrinhoDeCompraCreator.mockCarrinhoDeCompraRequestToUpdate();

        assertThrows(CarrinhoDeCompraNaoEncontradoException.class, () -> carrinhoDeCompraController.alterar(99L, carrinhoDeCompraParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove carrinhoDeCompra when successful")
    void excluir_RemoveCarrinhoDeCompra_WhenSuccessful() {
        ResponseEntity<Void> response = carrinhoDeCompraController.excluir(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Throws CarrinhoDeCompraNaoEncontradoException When carrinhoDeCompra not found")
    void excluir_ThrowsCarrinhoDeCompraNaoEncontradoException_WhenCarrinhoDeCompraNotFound() {
        BDDMockito.doThrow(CarrinhoDeCompraNaoEncontradoException.class).when(mockCarrinhoDeCompraService).excluir(anyLong());

        assertThrows(CarrinhoDeCompraNaoEncontradoException.class, () -> carrinhoDeCompraController.excluir(1L));
    }
}