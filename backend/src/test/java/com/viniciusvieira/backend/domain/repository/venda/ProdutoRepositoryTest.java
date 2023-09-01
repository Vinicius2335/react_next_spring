package com.viniciusvieira.backend.domain.repository.venda;

import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProdutoRepositoryTest {
    @Autowired
    private ProdutoRepository underTest;

    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final Produto produto1 = ProdutoCreator.createProduto();
    private final Produto produto2 = ProdutoCreator.createOtherProduto();

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        marcaRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("findAllProdutosByMarcaId() return list produtos")
    void whenFindAllProdutosByMarcaId_thenProdutosShoudlBeFound() {
        // given
        insertProdutos();
        // when
        List<Produto> expected = underTest.findAllProdutosByMarcaId(1L);
        // then
        assertThat(expected)
                .isNotNull()
                .hasSize(2)
                .contains(produto1, produto2);
    }

    @Test
    @DisplayName("findProdutosByMarcaId() return list produtos")
    void whenFindProdutosByMarcaId_thenProdutosShouldBeFound() {
        // given
        insertProdutos();
        // when
        List<Produto> expected = underTest.findAllProdutosByMarcaId(1L);
        // then
        assertThat(expected)
                .isNotNull()
                .hasSize(2)
                .contains(produto1, produto2);
    }

    @Test
    @DisplayName("findAllProdutosByCategoriaId() return list produtos")
    void whenFindAllProdutosByCategoriaId_thenProdutosShouldBeFound() {
        // given
        insertProdutos();
        // when
        List<Produto> expected = underTest.findAllProdutosByCategoriaId(1L);
        // then
        assertThat(expected)
                .isNotNull()
                .hasSize(2)
                .contains(produto1, produto2);
    }

    @Test
    @DisplayName("findProdutosByCategoriaId() return list produtos")
    void whenFindProdutosByCategoriaId_thenProdutosShouldBeFound() {
        // given
        insertProdutos();
        // when
        List<Produto> expected = underTest.findProdutosByCategoriaId(1L);
        // then
        assertThat(expected)
                .isNotNull()
                .hasSize(2)
                .contains(produto1, produto2);
    }

    @Test
    @DisplayName("findById() return produtos")
    void givenId_whenFindById_thenProdutosShouldBeFound() {
        // given
        insertProdutos();
        // when
        Produto expected = underTest.findById(1L).orElse(null);
        // then
        assertThat(expected)
                .isNotNull()
                .isEqualTo(produto1);
    }

    @Test
    @DisplayName("findById() return false when produto not found by id")
    void givenUnregisteredId_whenFindById_thenReturnFalse() {
        // given
        // when
        boolean expected = underTest.findById(1L).isPresent();
        // then
        assertThat(expected).isFalse();
    }

    @Test
    @DisplayName("saveAndFlush() insert produto")
    void givenProduto_whenSaveAndFlush_thenProdutoShouldBeInserted() {
        // given
        marcaRepository.saveAndFlush(MarcaCreator.createMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createCategoria());
        // when
        Produto expected = underTest.saveAndFlush(produto1);
        // then
        assertThat(expected)
                .isNotNull()
                .isEqualTo(produto1);
    }

    @Test
    @DisplayName("delete() insert produto")
    void givenProduto_whenDelete_thenProdutoShouldBeDeleted() {
        // given
        insertProdutos();
        // when
        underTest.delete(produto1);
        // then
        boolean expected = underTest.findById(1L).isPresent();
        assertThat(expected).isFalse();
    }

    @Test
    @DisplayName("deleteAll() delete all supplied products")
    void givenListProduto_whenDeleteAll_thenAllProdutoShouldBeDeleted() {
        // given
        insertProdutos();
        marcaRepository.saveAndFlush(MarcaCreator.createOtherMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createOtherCategoria());
        Produto produto3 = underTest.saveAndFlush(ProdutoCreator.createOtherProduto2());
        List<Produto> produtosToDelete = List.of(produto1, produto2);
        // when
        underTest.deleteAll(produtosToDelete);
        // then
        List<Produto> expected = underTest.findAll();
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .contains(produto3);
    }

    private void insertProdutos(){
        marcaRepository.saveAndFlush(MarcaCreator.createMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.createCategoria());
        underTest.saveAndFlush(produto1);
        underTest.saveAndFlush(produto2);
    }
}