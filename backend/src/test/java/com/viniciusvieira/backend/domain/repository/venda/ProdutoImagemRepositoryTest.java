package com.viniciusvieira.backend.domain.repository.venda;

import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.domain.model.venda.Marca;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProdutoImagemRepositoryTest {
    @Autowired
    private ProdutoImagemRepository underTest;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private MarcaRepository marcaRepository;

    private final ProdutoImagem produtoImagem = ProdutoImagemCreator.createProdutoImagem();
    private final Produto produto = ProdutoCreator.createProduto();
    private final Categoria categoria = CategoriaCreator.createCategoria();
    private final Marca marca = MarcaCreator.createMarca();

    @Test
    @DisplayName("saveAndFlush() insert new produtoImagem")
    void givenProdutoImagem_whenSaveAndFlush_thenProdutoImagemShouldBeInserted(){
        // given
        requirementsForCreatingAProdutoImagem();
        // when
        ProdutoImagem expected = underTest.saveAndFlush(produtoImagem);
        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getNome()).isEqualTo(produtoImagem.getNome());

    }

    @Test
    @DisplayName("findAll() return list produtoImagem")
    void whenFindAll_TheReturnListProdutoImagem(){
        // given
        ProdutoImagem produtoImagemInserted = getProdutoImagemInserted();
        // when
        List<ProdutoImagem> expected = underTest.findAll();
        // then
        assertThat(expected)
                .hasSize(1)
                .contains(produtoImagemInserted);
    }

    @Test
    @DisplayName("findById() return produtoImagem")
    void givenId_whenFindById_thenProdutoImagemShouldBeFound(){
        // given
        ProdutoImagem produtoImagemInserted = getProdutoImagemInserted();
        // when
        ProdutoImagem expected = underTest.findById(produtoImagemInserted.getId()).orElse(null);
        // then
        assertThat(expected)
                .isNotNull()
                .isEqualTo(produtoImagemInserted);
    }

    @Test
    @DisplayName("findById() return empty optional produtoImagem when not found")
    void givenUnregisteredId_whenFindById_thenReturnEmptyOptional(){
        // given
        // when
        Optional<ProdutoImagem> expected = underTest.findById(1L);
        // then
        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("delete() remove produtoImagem")
    void givenId_whenDelete_thenProdutoImagemShouldBeRemoved(){
        // given
        ProdutoImagem produtoImagemInserted = getProdutoImagemInserted();
        // when
        underTest.delete(produtoImagemInserted);
        // then
        boolean expected = underTest.findById(produtoImagemInserted.getId()).isPresent();
        assertThat(expected).isFalse();
    }

    private ProdutoImagem getProdutoImagemInserted(){
        requirementsForCreatingAProdutoImagem();
        return underTest.saveAndFlush(produtoImagem);
    }

    private void requirementsForCreatingAProdutoImagem() {
        marcaRepository.saveAndFlush(marca);
        categoriaRepository.saveAndFlush(categoria);
        produtoRepository.saveAndFlush(produto);
    }


}