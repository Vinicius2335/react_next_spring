package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.controller.venda.ProdutoController;
import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.exception.ProdutoImagemNaoEncontradoException;
import com.viniciusvieira.backend.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.domain.model.venda.Marca;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.domain.service.venda.CrudProdutoService;
import com.viniciusvieira.backend.domain.service.ImagemUploadService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Mock
    private ImagemUploadService mockImagemUploadService;

    private final Produto validProduto = ProdutoCreator.mockProduto();
    private final ProdutoResponse expectedProduto = ProdutoCreator.mockProdutoResponse();
    private final ProdutoResponse expectedProdutoUpdated = ProdutoCreator.mockProdutoResponseUpdated();
    private final List<Produto> expectedListProdutos = List.of(validProduto);
    
    @BeforeEach
    void setUp() throws IOException {
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

        // ImagemUploadService
        // uploadEInseriNovaImagem
        BDDMockito.when(mockImagemUploadService.uploadEInseriNovaImagem(any(Produto.class), any(MultipartFile.class)))
                .thenReturn(ProdutoCreator.mockProdutoImagemResponse());
        // uploadEAlteraImagem
        BDDMockito.when(mockImagemUploadService.uploadEAlteraImagem(anyLong(), any(MultipartFile.class)))
                .thenReturn(ProdutoCreator.mockProdutoImagemResponse());
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
    @DisplayName("uploadFile Upload image When successful")
    void uploadFile_UploadImage_WhenSuccessful() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "upload.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem".getBytes()
        );
        ResponseEntity<ProdutoImagemResponse> response = produtoController.uploadFile(1L, file);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("uploadFile Throws ProdutoNotFoundException When produto not found")
    void uploadFile_ThorwsProdutoNotFoundException_WhenProdutoNotFound() {
        BDDMockito.doThrow(ProdutoNaoEncontradoException.class).when(mockCrudProdutoService).buscarPorId(anyLong());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "upload.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem".getBytes()
        );

        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoController.uploadFile(99L, file));

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
    @DisplayName("alterarImagem Update image When successful")
    void alterarImagem_UpdateImage_WhenSuccessful() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "upload.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem para Upload".getBytes()
        );

        ResponseEntity<ProdutoImagemResponse> response = produtoController.alterarImagem(1L, 1L, file);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );
    }

    @Test
    @DisplayName("alterarImagem Throws ProdutoNaoEncontradoException When produto not found")
    void alterarImagem_ThorwsProdutoNaoEncontradoException_WhenProdutoNotFound() {
        BDDMockito.when(mockCrudProdutoService.buscarPorId(anyLong())).thenThrow(ProdutoNaoEncontradoException.class);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "upload.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem para Upload".getBytes()
        );

        assertThrows(ProdutoNaoEncontradoException.class,
                () -> produtoController.alterarImagem(99L, 1L, file));

    }

    @Test
    @DisplayName("alterarImagem Throws ProdutoImagemNaoEncontradoException When produtoImagem not found")
    void alterarImagem_ThorwsProdutoImagemNaoEncontradoException_WhenProdutoImagemNotFound() throws IOException {
        BDDMockito.when(mockImagemUploadService.uploadEAlteraImagem(anyLong(), any(MultipartFile.class)))
                .thenThrow(ProdutoImagemNaoEncontradoException.class);
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "upload.png",
                MediaType.IMAGE_PNG_VALUE,
                "Imagem para Upload".getBytes()
        );

        assertThrows(ProdutoImagemNaoEncontradoException.class,
                () -> produtoController.alterarImagem(1L, 99L, file));

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