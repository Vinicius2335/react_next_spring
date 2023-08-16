package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.ProdutoImagemMapper;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.exception.ProdutoImagemNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoImagemRepository;
import com.viniciusvieira.backend.util.ProdutoCreator;
import com.viniciusvieira.backend.util.ProdutoImagemCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
@Log4j2
@DisplayName("Teste Unitário para CrudProdutoImagemService")
class CrudProdutoImagemServiceTest {
    private static final String PATH_DIRECTORY = "src/main/resources/static/image";
    @InjectMocks
    private CrudProdutoImagemService crudProdutoImagemService;

    @Mock
    private ProdutoImagemRepository mockProdutoImagemRepository;
    @Mock
    private ProdutoImagemMapper mockProdutoImagemMapper;

    private final ProdutoImagem produtoImagemToSave = ProdutoImagemCreator.mockProdutoImagem();
    private final ProdutoImagemResponse expectedProdutoImagemResponse = ProdutoImagemCreator.mockProdutoImagemResponse();

    @BeforeEach
    void SetUp(){
        // ProdutoImagemRepository
        // saveAndFlush
        BDDMockito.when(mockProdutoImagemRepository.saveAndFlush(any(ProdutoImagem.class))).thenReturn(produtoImagemToSave);
        // findAll
        BDDMockito.when(mockProdutoImagemRepository.findAll()).thenReturn(List.of(produtoImagemToSave));
        // findById
        BDDMockito.when(mockProdutoImagemRepository.findById(anyLong())).thenReturn(Optional.of(produtoImagemToSave));
        // delete
        BDDMockito.doNothing().when(mockProdutoImagemRepository).delete(any(ProdutoImagem.class));

        // ProdutoImagemMapper
        // toProdutoImagemResponse
        BDDMockito.when(mockProdutoImagemMapper.toProdutoImagemResponse(any(ProdutoImagem.class)))
                .thenReturn(expectedProdutoImagemResponse);
    }

    @Test
    @DisplayName("inserir Save new produtoImagem When successful")
    void inserir_SaveNewProdutoImagem_WhenSuccessful(){
        Produto produto = ProdutoCreator.mockProduto();
        ProdutoImagemResponse imagemInserida = crudProdutoImagemService.inserir(produto, "teste.png");

        log.info(imagemInserida);

        assertAll(
                () -> assertNotNull(imagemInserida),
                () -> assertEquals(produtoImagemToSave.getNome(), imagemInserida.getNome())
        );
    }

    @Test
    @DisplayName("buscarTodos Return list of produtoImagem When successful")
    void buscarTodos_ReturnListProdutoImagem_WhenSuccessful(){
        List<ProdutoImagem> imagems = crudProdutoImagemService.buscarTodos();

        assertAll(
                () -> assertNotNull(imagems),
                () -> assertEquals(1, imagems.size()),
                () -> assertTrue(imagems.contains(produtoImagemToSave))
        );
    }

    @Test
    @DisplayName("buscarPorId Return ProdutoImagem When successful")
    void buscarPorId_ReturnProdutoImagem_WhenSuccessful(){
        ProdutoImagem imagemEncontrada = crudProdutoImagemService.buscarPorId(1L);

        assertAll(
                () -> assertNotNull(imagemEncontrada),
                () -> assertEquals(1L, imagemEncontrada.getId()),
                () -> assertEquals(produtoImagemToSave.getNome(), imagemEncontrada.getNome())
        );
    }

    @Test
    @DisplayName("buscarPorId Throws ProdutoImagemNaoEncontradoException When produtoImagem not found")
    void buscarPorId_ThorwsProdutoImagemNaoEncontradoException_WhenProdutoImagemNotFound(){
        BDDMockito.when(mockProdutoImagemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProdutoImagemNaoEncontradoException.class, () -> crudProdutoImagemService.buscarPorId(1L));
    }

    @Test
    @DisplayName("alterar Update imagem When successful")
    void alterar_UpdateImagem_WhenSuccessful() throws IOException {
        criarImagemTeste();
        assertDoesNotThrow(() -> crudProdutoImagemService.alterar(1L, "imagemRefatorada"));
    }

    @Test
    @DisplayName("alterar Throws ProdutoImagemNaoEncontradoException When produtoImagem not found")
    void alterar_ThorwsProdutoImagemNaoEncontradoException_WhenProdutoImagemNotFound() {
        BDDMockito.when(mockProdutoImagemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProdutoImagemNaoEncontradoException.class,
                () -> crudProdutoImagemService.alterar(99L, "teste.png"));
    }

    @Test
    @DisplayName("excluir Remove Image When Successful")
    void excluir_RemoveImage_WhenSuccessful() throws IOException {
        criarImagemTeste();
        assertDoesNotThrow(() -> crudProdutoImagemService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws ProdutoImagemNaoEncontradoException When produtoImagem not found")
    void excluir_ThorwsProdutoImagemNaoEncontradoException_WhenProdutoImagemNotFound() {
        BDDMockito.when(mockProdutoImagemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProdutoImagemNaoEncontradoException.class, () -> crudProdutoImagemService.excluir(99L));
    }

    private void criarImagemTeste() throws IOException {
        File file = new File(PATH_DIRECTORY + "/image.png");
        file.createNewFile();
    }
}