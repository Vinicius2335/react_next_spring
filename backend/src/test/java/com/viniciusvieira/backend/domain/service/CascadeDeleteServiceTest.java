package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.exception.venda.CategoriaNaoEncontradaException;
import com.viniciusvieira.backend.domain.exception.venda.MarcaNaoEncontradaException;
import com.viniciusvieira.backend.domain.service.venda.CrudCategoriaService;
import com.viniciusvieira.backend.domain.service.venda.CrudMarcaService;
import com.viniciusvieira.backend.domain.service.venda.CrudProdutoService;
import com.viniciusvieira.backend.util.CategoriaCreator;
import com.viniciusvieira.backend.util.MarcaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CascadeDeleteServiceTest {
    @InjectMocks
    private CascadeDeleteService underTest;

    @Mock
    private CrudMarcaService crudMarcaServiceMock;
    @Mock
    private CrudCategoriaService crudCategoriaServiceMock;
    @Mock
    private CrudProdutoService crudProdutoServiceMock;

    @Test
    @DisplayName("cascadeDeleteMarca() remove all produtos related to marcaId")
    void givenMarcaId_whenCascadeDeleteMarca_thenMarcaShouldBeRemoved() {
        // given
        given(crudMarcaServiceMock.buscarPeloId(anyLong())).willReturn(MarcaCreator.createMarca());
        doNothing().when(crudProdutoServiceMock).excluirTodosProdutosRelacionadosMarcaId(anyLong());
        doNothing().when(crudMarcaServiceMock).excluir(anyLong());
        // when
        underTest.cascadeDeleteMarca(1L);
        // then
        verify(crudMarcaServiceMock, times(1)).excluir(anyLong());
    }

    @Test
    @DisplayName("cascadeDeleteMarca() Throws MarcaNaoEncontradaException when marca not found by id")
    void givenUnregisteredMarcaId_whenCascadeDeleteMarca_thenMarcaShouldBeRemoved() {
        // given
        doThrow(new MarcaNaoEncontradaException("Marca não cadastrada")).when(crudMarcaServiceMock).buscarPeloId(anyLong());
        // when
        assertThatThrownBy(() -> underTest.cascadeDeleteMarca(1L))
                .isInstanceOf(MarcaNaoEncontradaException.class)
                        .hasMessageContaining("Marca não cadastrada");
        // then
        verify(crudMarcaServiceMock,never()).excluir(anyLong());
    }

    @Test
    @DisplayName("cascadeDeleteCategoria() remove all produtos related to categoriaId")
    void givenCategoriaId_whenCascadeDeleteCategoria_thenCategoriaShouldBeRemoved() {
        // given
        given(crudCategoriaServiceMock.buscarPeloId(anyLong())).willReturn(CategoriaCreator.createCategoria());
        doNothing().when(crudProdutoServiceMock).excluirTodosProdutosRelacionadosCategoriaId(anyLong());
        doNothing().when(crudCategoriaServiceMock).excluir(anyLong());
        // when
        underTest.cascadeDeleteCategoria(1L);
        // then
        verify(crudCategoriaServiceMock, times(1)).excluir(anyLong());
    }

    @Test
    @DisplayName("cascadeDeleteCategoria() throws CategoriaNaoEncontradoException when categoria not found by id")
    void givenUnregisteredId_whenCascadeDeleteCategoria_thenThrowsCategoriaNaoEncontradaException(){
        // given
        doThrow(new CategoriaNaoEncontradaException("Categoria não cadastrada"))
                .when(crudCategoriaServiceMock).buscarPeloId(anyLong());
        // when
        assertThatThrownBy(() -> underTest.cascadeDeleteCategoria(1L))
                .isInstanceOf(CategoriaNaoEncontradaException.class)
                .hasMessageContaining("Categoria não cadastrada");
        // then
        verify(crudCategoriaServiceMock,never()).excluir(anyLong());
    }
}