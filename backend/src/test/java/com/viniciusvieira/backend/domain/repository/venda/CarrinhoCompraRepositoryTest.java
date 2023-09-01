package com.viniciusvieira.backend.domain.repository.venda;

import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.util.CarrinhoDeCompraCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CarrinhoCompraRepositoryTest {
    @Autowired
    private CarrinhoCompraRepository underTest;
    @Autowired
    private PessoaRepository pessoaRepository;

    private final CarrinhoDeCompra carrinhoDeCompra = CarrinhoDeCompraCreator.createCarrinhoDeCompra();
    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        pessoa = pessoaRepository.saveAndFlush(PessoaCreator.createPessoa());
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        pessoaRepository.deleteAll();
    }

    @Test
    @DisplayName("saveAndFlush() insert new carrinhoDeCompra")
    void givenCarrinhoDeCompra_whenSaveAndFlush_thenCarrinhoDeCompraShouldBeInserted(){
        // given
        // when
        CarrinhoDeCompra expected = underTest.saveAndFlush(carrinhoDeCompra);
        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getPessoa()).isEqualTo(pessoa);

    }

    @Test
    @DisplayName("findAll() return list carrinhoDeCompra")
    void whenFindAll_TheReturnListCarrinhoDeCompra(){
        // given
        CarrinhoDeCompra carrinhoDeCompraInserted = getCarrinhoDeCompraInserted();
        // when
        List<CarrinhoDeCompra> expected = underTest.findAll();
        // then
        assertThat(expected)
                .hasSize(1)
                .contains(carrinhoDeCompraInserted);
    }

    @Test
    @DisplayName("findById() return carrinhoDeCompra")
    void givenId_whenFindById_thenCarrinhoDeCompraShouldBeFound(){
        // given
        CarrinhoDeCompra carrinhoDeCompraInserted = getCarrinhoDeCompraInserted();
        // when
        CarrinhoDeCompra expected = underTest.findById(carrinhoDeCompraInserted.getId()).orElse(null);
        // then
        assertThat(expected)
                .isNotNull()
                .isEqualTo(carrinhoDeCompraInserted);
    }

    @Test
    @DisplayName("findById() return empty optional carrinhoDeCompra when not found")
    void givenUnregisteredId_whenFindById_thenReturnEmptyOptional(){
        // given
        // when
        Optional<CarrinhoDeCompra> expected = underTest.findById(1L);
        // then
        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("delete() remove carrinhoDeCompra")
    void givenId_whenDelete_thenCarrinhoDeCompraShouldBeRemoved(){
        // given
        CarrinhoDeCompra carrinhoDeCompraInserted = getCarrinhoDeCompraInserted();
        // when
        underTest.delete(carrinhoDeCompraInserted);
        // then
        boolean expected = underTest.findById(carrinhoDeCompraInserted.getId()).isPresent();
        assertThat(expected).isFalse();
    }

    private CarrinhoDeCompra getCarrinhoDeCompraInserted(){
        carrinhoDeCompra.setPessoa(pessoa);
        return underTest.saveAndFlush(carrinhoDeCompra);
    }

}