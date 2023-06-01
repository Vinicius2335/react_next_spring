package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoResponse;
import com.viniciusvieira.backend.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Categoria;
import com.viniciusvieira.backend.domain.model.Marca;
import com.viniciusvieira.backend.domain.model.Produto;
import com.viniciusvieira.backend.domain.repository.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.MarcaRepository;
import com.viniciusvieira.backend.domain.service.CrudProdutoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste Unitário para a classe ProdutoController")
class ProdutoControllerTest {
    @InjectMocks
    private ProdutoController produtoController;
    
    @Mock
    private CrudProdutoService mockCrudProdutoService;
    @Mock
    private MarcaRepository mockMarcaRepository;
    @Mock
    private CategoriaRepository mockCategoriaRepository;

    private final Produto validProduto = ProdutoCreator.mockProduto();
    private final ProdutoResponse expectedProduto = ProdutoCreator.mockProdutoResponse();
    private final ProdutoResponse expectedProdutoUpdated = ProdutoCreator.mockProdutoResponseUpdated();
    private final List<Produto> expectedListProdutos = List.of(validProduto);
    
    @BeforeEach
    void setUp(){
        // MarcaRepository
        // saveAndlFlush
        BDDMockito.when(mockMarcaRepository.saveAndFlush(any(Marca.class)))
                .thenReturn(MarcaCreator.mockMarca());

        // CategoriaRepository
        // saveAndlFlush
        BDDMockito.when(mockCategoriaRepository.saveAndFlush(any(Categoria.class)))
                .thenReturn(CategoriaCreator.mockCategoria());
        
        // CrudProdutoService
        // buscarTodos
        BDDMockito.when(mockCrudProdutoService.buscarTodos()).thenReturn(expectedListProdutos);
        // buscarPeloId
        BDDMockito.when(mockCrudProdutoService.buscarPorId(anyLong())).thenReturn(validProduto);
        // inserir
        BDDMockito.when(mockCrudProdutoService.inserir(any(ProdutoRequest.class))).thenReturn(expectedProduto);
        // alterar
        BDDMockito.when(mockCrudProdutoService.alterar(anyLong(), any(ProdutoRequest.class))).thenReturn(expectedProdutoUpdated);
        // excluir
        BDDMockito.doNothing().when(mockCrudProdutoService).excluir(anyLong());
    }

    @Test
    @DisplayName("buscarTodos Return list of produto When successful")
    void buscarTodos_ReturnListProduto_WhenSuccessful() {
        ResponseEntity<List<Produto>> response = produtoController.buscarTodos();

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedListProdutos, response.getBody())
        );
    }

    @Test
    @DisplayName("inserir Insert new produto When successful")
    void inserir_InsertNewProduto_WhenSuccessful() {
        ProdutoRequest produtoParaSalvar = ProdutoCreator.mockProdutoRequestToSave();
        ResponseEntity<ProdutoResponse> response = produtoController.inserir(produtoParaSalvar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(expectedProduto.getDescricaoCurta(), response.getBody().getDescricaoCurta()),
                () -> assertEquals(expectedProduto.getDescricaoDetalhada(), response.getBody().getDescricaoDetalhada()),
                () -> assertEquals(expectedProduto.getQuantidade(), response.getBody().getQuantidade()),
                () -> assertEquals(expectedProduto.getValorVenda(), response.getBody().getValorVenda())
        );
    }

    @Test
    @DisplayName("alterar Update produto when successful")
    void alterar_UpdateProduto_WhenSuccessul() {
        ProdutoRequest produtoParaAlterar = ProdutoCreator.mockProdutoRequestToUpdate();
        ResponseEntity<ProdutoResponse> response = produtoController.alterar(1L, produtoParaAlterar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedProdutoUpdated.getDescricaoCurta(), response.getBody().getDescricaoCurta()),
                () -> assertEquals(expectedProdutoUpdated.getDescricaoDetalhada(), response.getBody().getDescricaoDetalhada()),
                () -> assertEquals(expectedProdutoUpdated.getQuantidade(), response.getBody().getQuantidade()),
                () -> assertEquals(expectedProdutoUpdated.getValorVenda(), response.getBody().getValorVenda())
        );
    }

    @Test
    @DisplayName("alterar Throws ProdutoNaoEncontradoException When produto not found")
    void alterar_ThrowsProdutoNaoEncontradoException_WhenProdutoNotFound() {
        BDDMockito.when(mockCrudProdutoService.alterar(anyLong(), any(ProdutoRequest.class))).thenThrow(new ProdutoNaoEncontradoException());
        ProdutoRequest produtoParaAlterar = ProdutoCreator.mockProdutoRequestToUpdate();

        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoController.alterar(99L, produtoParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove produto when successful")
    void excluir_RemoveProduto_WhenSuccessful() {
        ResponseEntity<Void> response = produtoController.excluir(1L);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("excluir Throws ProdutoNaoEncontradoException When produto not found")
    void excluir_ThrowsProdutoNaoEncontradoException_WhenProdutoNotFound() {
        BDDMockito.doThrow(ProdutoNaoEncontradoException.class).when(mockCrudProdutoService).excluir(anyLong());

        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoController.excluir(1L));
    }
}