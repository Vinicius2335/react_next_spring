package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Teste Unitário para a interface ProdutoRepository")
class ProdutoRepositoryTest {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    private final Produto expectedProduto = ProdutoCreator.mockProduto();
    
    public Produto inserirNovoProdutoNoBanco(){
        marcaRepository.saveAndFlush(MarcaCreator.mockMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());
        return produtoRepository.saveAndFlush(ProdutoCreator.mockProduto());
    }

    @Test
    @DisplayName("saveAndFlush Insert new produto when Successful")
    void saveAndFlush_InsertNewProduto_WhenSuccessful(){
        marcaRepository.saveAndFlush(MarcaCreator.mockMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());
        Produto produtoSaved = produtoRepository.saveAndFlush(ProdutoCreator.mockProduto());

        assertAll(
                () -> assertNotNull(produtoSaved),
                () -> assertEquals(expectedProduto.getId(), produtoSaved.getId()),
                () -> assertEquals(expectedProduto.getQuantidade(), produtoSaved.getQuantidade()),
                () -> assertEquals(expectedProduto.getValorCusto(), produtoSaved.getValorCusto()),
                () -> assertEquals(expectedProduto.getValorVenda(), produtoSaved.getValorVenda())
        );
    }

    @Test
    @DisplayName("findAll Return list of produto When successful")
    void findAll_ReturnListProduto_WhenSuccessful(){
        Produto novaProdutoInserida = inserirNovoProdutoNoBanco();
        List<Produto> produtos = produtoRepository.findAll();

        assertAll(
                () -> assertNotNull(produtos),
                () -> assertFalse(produtos.isEmpty()),
                () -> assertEquals(1, produtos.size()),
                () -> assertTrue(produtos.contains(novaProdutoInserida))
        );
    }

    @Test
    @DisplayName("findById Return a produto When successful")
    void findById_ReturnProduto_WhenSuccessful(){
        Produto novaProdutoInserida = inserirNovoProdutoNoBanco();
        Produto produtoEncontrada = produtoRepository.findById(novaProdutoInserida.getId()).get();

        assertAll(
                () -> assertNotNull(produtoEncontrada),
                () -> assertEquals(novaProdutoInserida, produtoEncontrada)
        );
    }

    @Test
    @DisplayName("saveAndFlush Update existing produto when successful")
    void saveAndFlush_UpdateExistingProduto_WhenSuccessful(){
        inserirNovoProdutoNoBanco();
        Produto produtoParaAtualizar = ProdutoCreator.mockProdutoToUpdate();

        Produto produtoAtualizada = produtoRepository.saveAndFlush(produtoParaAtualizar);
        Produto produtoEncontrada = produtoRepository.findById(produtoAtualizada.getId()).get();

        assertAll(
                () -> assertNotNull(produtoAtualizada),
                () -> assertEquals(produtoEncontrada, produtoAtualizada)
        );
    }

    @Test
    @DisplayName("delete Remove produto When successful")
    void delete_RemoveProduto_WhenSuccessful(){
        Produto novaProdutoInserida = inserirNovoProdutoNoBanco();
        produtoRepository.delete(novaProdutoInserida);
        List<Produto> produtos = produtoRepository.findAll();

        assertTrue(produtos.isEmpty());
    }
}