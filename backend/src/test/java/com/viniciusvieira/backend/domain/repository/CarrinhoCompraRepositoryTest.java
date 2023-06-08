package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.CarrinhoDeCompra;
import com.viniciusvieira.backend.util.CarrinhoDeCompraCreator;
import com.viniciusvieira.backend.util.CidadeCreator;
import com.viniciusvieira.backend.util.EstadoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Teste Unitário para a interface CarrinhoCompraRepository")
class CarrinhoCompraRepositoryTest {
    @Autowired
    private CarrinhoCompraRepository carrinhoCompraRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    private final CarrinhoDeCompra expectedCarrinhoDeCompra = CarrinhoDeCompraCreator.mockCarrinhoDeCompra();

    public CarrinhoDeCompra inserirNovaCarrinhoDeCompraNoBanco(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        pessoaRepository.saveAndFlush(PessoaCreator.mockPessoa());
        return carrinhoCompraRepository.saveAndFlush(CarrinhoDeCompraCreator.mockCarrinhoDeCompra());
    }

    @Test
    @DisplayName("saveAndFlush Insert new carrinhoDeCompra when Successful")
    void saveAndFlush_InsertNewCarrinhoDeCompra_WhenSuccessful(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        pessoaRepository.saveAndFlush(PessoaCreator.mockPessoa());
        CarrinhoDeCompra carrinhoDeCompraSaved = carrinhoCompraRepository
                .saveAndFlush(CarrinhoDeCompraCreator.mockCarrinhoDeCompra());

        assertAll(
                () -> assertNotNull(carrinhoDeCompraSaved),
                () -> assertEquals(expectedCarrinhoDeCompra.getId(), carrinhoDeCompraSaved.getId()),
                () -> assertEquals(expectedCarrinhoDeCompra.getSituacao(), carrinhoDeCompraSaved.getSituacao()),
                () -> assertEquals(expectedCarrinhoDeCompra.getObservacao(), carrinhoDeCompraSaved.getObservacao())
        );
    }

    @Test
    @DisplayName("findAll Return list of carrinhoDeCompra When successful")
    void findAll_ReturnListCarrinhoDeCompra_WhenSuccessful(){
        CarrinhoDeCompra novaCarrinhoDeCompraInserida = inserirNovaCarrinhoDeCompraNoBanco();
        List<CarrinhoDeCompra> carrinhoDeCompras = carrinhoCompraRepository.findAll();

        assertAll(
                () -> assertNotNull(carrinhoDeCompras),
                () -> assertFalse(carrinhoDeCompras.isEmpty()),
                () -> assertEquals(1, carrinhoDeCompras.size()),
                () -> assertTrue(carrinhoDeCompras.contains(novaCarrinhoDeCompraInserida))
        );
    }

    @Test
    @DisplayName("findById Return a carrinhoDeCompra When successful")
    void findById_ReturnCarrinhoDeCompra_WhenSuccessful(){
        CarrinhoDeCompra novaCarrinhoDeCompraInserida = inserirNovaCarrinhoDeCompraNoBanco();
        CarrinhoDeCompra carrinhoDeCompraEncontrada = carrinhoCompraRepository
                .findById(novaCarrinhoDeCompraInserida.getId()).get();

        assertAll(
                () -> assertNotNull(carrinhoDeCompraEncontrada),
                () -> assertEquals(novaCarrinhoDeCompraInserida, carrinhoDeCompraEncontrada)
        );
    }

    @Test
    @DisplayName("saveAndFlush Update existing carrinhoDeCompra when successful")
    void saveAndFlush_UpdateExistingCarrinhoDeCompra_WhenSuccessful(){
        inserirNovaCarrinhoDeCompraNoBanco();
        CarrinhoDeCompra carrinhoDeCompraParaAtualizar = CarrinhoDeCompraCreator.mockCarrinhoDeCompraUpdate();

        CarrinhoDeCompra carrinhoDeCompraAtualizada = carrinhoCompraRepository.saveAndFlush(carrinhoDeCompraParaAtualizar);
        CarrinhoDeCompra carrinhoDeCompraEncontrada = carrinhoCompraRepository
                .findById(carrinhoDeCompraAtualizada.getId()).get();

        assertAll(
                () -> assertNotNull(carrinhoDeCompraAtualizada),
                () -> assertEquals(carrinhoDeCompraEncontrada, carrinhoDeCompraAtualizada)
        );
    }

    @Test
    @DisplayName("delete Remove carrinhoDeCompra When successful")
    void delete_RemoveCarrinhoDeCompra_WhenSuccessful(){
        CarrinhoDeCompra novaCarrinhoDeCompraInserida = inserirNovaCarrinhoDeCompraNoBanco();
        carrinhoCompraRepository.delete(novaCarrinhoDeCompraInserida);
        List<CarrinhoDeCompra> carrinhoDeCompras = carrinhoCompraRepository.findAll();

        assertTrue(carrinhoDeCompras.isEmpty());
    }
}