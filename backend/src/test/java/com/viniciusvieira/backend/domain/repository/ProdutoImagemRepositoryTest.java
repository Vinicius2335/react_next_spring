package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoImagemRepository;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@Log4j2
@DisplayName("Teste Unitário para ProdutoImagemRepository")
class ProdutoImagemRepositoryTest {
    @Autowired
    private ProdutoImagemRepository produtoImagemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private final ProdutoImagem produtoImagemToSave = ProdutoImagemCreator.mockProdutoImagem();

    public ProdutoImagem inserirNovoProdutoImagemNoBanco(){
        marcaRepository.saveAndFlush(MarcaCreator.mockMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());
        produtoRepository.saveAndFlush(ProdutoCreator.mockProduto());
        return produtoImagemRepository.saveAndFlush(produtoImagemToSave);
    }

    @Test
    @DisplayName("saveAndFlush Insert new produtoImagem when Successful")
    void saveAndFlush_InsertNewProdutoImagem_WhenSuccessful(){
        marcaRepository.saveAndFlush(MarcaCreator.mockMarca());
        categoriaRepository.saveAndFlush(CategoriaCreator.mockCategoria());
        produtoRepository.saveAndFlush(ProdutoCreator.mockProduto());

        ProdutoImagem produtoImagemSalvo = produtoImagemRepository.saveAndFlush(produtoImagemToSave);

        assertAll(
                () -> assertNotNull(produtoImagemSalvo),
                () -> assertEquals(produtoImagemToSave.getId(), produtoImagemSalvo.getId()),
                () -> assertEquals(produtoImagemToSave.getNome(), produtoImagemSalvo.getNome()),
                () -> assertEquals(produtoImagemToSave.getProduto().getDescricaoCurta(),
                        produtoImagemSalvo.getProduto().getDescricaoCurta())
        );
    }

    @Test
    @DisplayName("findAll Return list of produtoImagem When successful")
    void findAll_ReturnListProdutoImagem_WhenSuccessful(){
        ProdutoImagem novoProdutoImagemInserida = inserirNovoProdutoImagemNoBanco();
        List<ProdutoImagem> imagems = produtoImagemRepository.findAll();

        assertAll(
                () -> assertNotNull(imagems),
                () -> assertFalse(imagems.isEmpty()),
                () -> assertEquals(1, imagems.size()),
                () -> assertTrue(imagems.contains(novoProdutoImagemInserida))
        );
    }

    @Test
    @DisplayName("findById Return a produtoImagem When successful")
    void findById_ReturnProdutoImagem_WhenSuccessful(){
        ProdutoImagem produtoImagemInserido = inserirNovoProdutoImagemNoBanco();
        Optional<ProdutoImagem> produtoEncontrado = produtoImagemRepository.findById(produtoImagemInserido.getId());

        assertAll(
                () -> assertNotNull(produtoEncontrado),
                () -> assertFalse(produtoEncontrado.isEmpty()),
                () -> assertEquals(produtoImagemToSave.getNome(), produtoEncontrado.get().getNome())
        );
    }

    @Test
    @DisplayName("delete Remove produtoImagem When successful")
    void delete_RemoveProdutoImagem_WhenSuccessful(){
        ProdutoImagem produtoImagemInserido = inserirNovoProdutoImagemNoBanco();
        produtoImagemRepository.delete(produtoImagemInserido);

        List<ProdutoImagem> imagems = produtoImagemRepository.findAll();

        assertTrue(imagems.isEmpty());
    }
}