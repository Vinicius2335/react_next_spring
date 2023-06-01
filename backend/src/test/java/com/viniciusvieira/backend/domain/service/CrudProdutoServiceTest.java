package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.ProdutoMapper;
import com.viniciusvieira.backend.api.representation.model.request.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoResponse;
import com.viniciusvieira.backend.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Categoria;
import com.viniciusvieira.backend.domain.model.Marca;
import com.viniciusvieira.backend.domain.model.Produto;
import com.viniciusvieira.backend.domain.repository.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.MarcaRepository;
import com.viniciusvieira.backend.domain.repository.ProdutoRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import com.viniciusvieira.backend.util.ProdutoCreator;
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
@DisplayName("Teste Unitário para a classe CrudProdutoService")
class CrudProdutoServiceTest {
    @InjectMocks
    private CrudProdutoService crudProdutoService;
    
    @Mock
    private ProdutoRepository mockProdutoRepository;
    @Mock
    private ProdutoMapper mockProdutoMapper;
    @Mock
    private MarcaRepository mockMarcaRepository;
    @Mock
    private CategoriaRepository mockCategoriaRepository;
    
    private final Produto validProduto = ProdutoCreator.mockProduto();
    private final ProdutoResponse expectedProduto = ProdutoCreator.mockProdutoResponse();
    private final ProdutoResponse expectedProdutoUpdated = ProdutoCreator.mockProdutoResponseUpdated();
    private final List<Produto> expectedListProduto = List.of(validProduto);
    
    @BeforeEach
    void setUp(){
        // ProdutoRepository
        // findAll
        BDDMockito.when(mockProdutoRepository.findAll()).thenReturn(expectedListProduto);
        // findById
        BDDMockito.when(mockProdutoRepository.findById(anyLong())).thenReturn(Optional.of(validProduto));
        // saveAndFlush
        BDDMockito.when(mockProdutoRepository.saveAndFlush(any(Produto.class))).thenReturn(validProduto);
        // delete
        BDDMockito.doNothing().when(mockProdutoRepository).delete(any(Produto.class));
        // findAllProdutosByMarcaId
        BDDMockito.when(mockProdutoRepository.findAllProdutosByMarcaId(anyLong())).thenReturn(expectedListProduto);
        // findAllProdutosByCategoriaId
        BDDMockito.when(mockProdutoRepository.findAllProdutosByCategoriaId(anyLong())).thenReturn(expectedListProduto);
        
        // ProdutoMapper
        // toDomainProduto
        BDDMockito.when(mockProdutoMapper.toDomainProduto(any(ProdutoRequest.class)))
                        .thenReturn(validProduto);
        // toProdutoResponse
        BDDMockito.when(mockProdutoMapper.toProdutoResponse(any(Produto.class)))
                        .thenReturn(expectedProduto);
        
        // MarcaRepository
        // saveAndlFlush
        BDDMockito.when(mockMarcaRepository.saveAndFlush(any(Marca.class)))
                .thenReturn(MarcaCreator.mockMarca());
        
        // CategoriaRepository
        // saveAndlFlush
        BDDMockito.when(mockCategoriaRepository.saveAndFlush(any(Categoria.class)))
                .thenReturn(CategoriaCreator.mockCategoria());
    }

    @Test
    @DisplayName("buscarTodos Return a list of Produto When successful")
    void buscarTodos_ReturnListProduto_WhenSuccessful() {
        List<Produto> produtos = crudProdutoService.buscarTodos();

        assertAll(
                () -> assertNotNull(produtos),
                () -> assertFalse(produtos.isEmpty()),
                () -> assertEquals(1, produtos.size()),
                () -> assertTrue(produtos.contains(validProduto))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return produto When successful")
    void buscarPeloId_ReturnProduto_WhenSuccessful() {
        Produto produto = crudProdutoService.buscarPorId(1L);

        assertAll(
                () -> assertNotNull(produto),
                () -> assertEquals(validProduto, produto)
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws ProdutoNaoEncontradaException When produto not found")
    void buscarPeloId_ThrowsProdutoNaoEncontradaException_WhenProdutoNotFound() {
        BDDMockito.when(mockProdutoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> crudProdutoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("inserir Insert new produto When successful")
    void inserir_InsertNewProduto_WhenSuccessful() {
        ProdutoRequest produtoParaInserir = ProdutoCreator.mockProdutoRequestToSave();
        ProdutoResponse produtoInserida = crudProdutoService.inserir(produtoParaInserir);

        assertAll(
                () -> assertNotNull(produtoInserida),
                () -> assertEquals(expectedProduto.getDescricaoCurta(), produtoInserida.getDescricaoCurta()),
                () -> assertEquals(expectedProduto.getDescricaoDetalhada(), produtoInserida.getDescricaoDetalhada()),
                () -> assertEquals(expectedProduto.getQuantidade(), produtoInserida.getQuantidade())
        );
    }

    @Test
    @DisplayName("alterar Update produto when successful")
    void alterar_UpdateProduto_WhenSuccessul() {
        Produto updateProduto = ProdutoCreator.mockProdutoToUpdate();
        BDDMockito.when(mockProdutoRepository.saveAndFlush(any(Produto.class))).thenReturn(updateProduto);
        BDDMockito.when(mockProdutoMapper.toProdutoResponse(any(Produto.class))).thenReturn(expectedProdutoUpdated);

        ProdutoRequest produtoParaAlterar = ProdutoCreator.mockProdutoRequestToUpdate();
        ProdutoResponse produtoAlterada = crudProdutoService.alterar(1L, produtoParaAlterar);

        assertAll(
                () -> assertNotNull(produtoAlterada),
                () -> assertEquals(expectedProdutoUpdated.getDescricaoCurta(), produtoAlterada.getDescricaoCurta()),
                () -> assertEquals(expectedProdutoUpdated.getDescricaoDetalhada(), produtoAlterada.getDescricaoDetalhada()),
                () -> assertEquals(expectedProdutoUpdated.getQuantidade(), produtoAlterada.getQuantidade())
        );
    }

    @Test
    @DisplayName("alterar Throws ProdutoNaoEncontradaException When produto not found")
    void alterar_ThrowsProdutoNaoEncontradaException_WhenProdutoNotFound() {
        BDDMockito.when(mockProdutoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ProdutoRequest produtoParaAlterar = ProdutoCreator.mockProdutoRequestToUpdate();

        assertThrows(ProdutoNaoEncontradoException.class, () -> crudProdutoService.alterar(99L, produtoParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove produto when successful")
    void excluir_RemoveProduto_WhenSuccessful() {
        assertDoesNotThrow(() -> crudProdutoService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws ProdutoNaoEncontradaException When produto not found")
    void excluir_ThrowsProdutoNaoEncontradaException_WhenProdutoNotFound() {
        BDDMockito.when(mockProdutoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> crudProdutoService.excluir(1L));
    }

    @Test
    @DisplayName("excluirTodosProdutosRelacionadosMarcaId When successuful")
    void excluirTodosProdutosRelacionadosMarcaId_WhenSuccessuful(){
        assertDoesNotThrow(() -> crudProdutoService.excluirTodosProdutosRelacionadosMarcaId(1L));
    }

    @Test
    @DisplayName("excluirTodosProdutosRelacionadosCategoriaId When successful")
    void excluirTodosProdutosRelacionadosCategoriaId_WhenSuccessful(){
        assertDoesNotThrow(() -> crudProdutoService.excluirTodosProdutosRelacionadosCategoriaId(1L));
    }
}