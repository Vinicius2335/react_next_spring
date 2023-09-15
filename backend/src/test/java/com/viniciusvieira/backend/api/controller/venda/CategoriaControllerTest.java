package com.viniciusvieira.backend.api.controller.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CategoriaResponse;
import com.viniciusvieira.backend.domain.exception.venda.CategoriaAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.venda.CategoriaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.domain.service.venda.CrudCategoriaService;
import com.viniciusvieira.backend.util.CategoriaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {
    @InjectMocks
    private CategoriaController underTest;

    @Mock
    private CrudCategoriaService crudCategoriaServiceMock;

    private final Categoria categoria = CategoriaCreator.createCategoria();
    private final CategoriaResponse categoriaResponse = CategoriaCreator.createCategoriaResponse();

    @Test
    @DisplayName("buscarTodos() return list categoria")
    void whenBuscarTodos_thenReturnListCategoria() {
        // given
        given(crudCategoriaServiceMock.buscarTodos()).willReturn(List.of(categoria));
        // when
        ResponseEntity<List<Categoria>> expected = underTest.buscarTodos();
        // then
        verify(crudCategoriaServiceMock, times(1)).buscarTodos();
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .hasSize(1)
                .contains(categoria);
    }

    @Test
    @DisplayName("buscarPeloId() return categoria")
    void givenId_whenBuscarPeloId_thenCategoriaShouldBeFound() {
        // given
        given(crudCategoriaServiceMock.buscarPeloId(anyLong())).willReturn(categoria);
        // when
        ResponseEntity<Categoria> expected = underTest.buscarPeloId(categoria.getId());
        // then
        verify(crudCategoriaServiceMock, times(1)).buscarPeloId(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(categoria);
    }

    @Test
    @DisplayName("buscarPeloId() Throws CategoriaNaoEncontradException when categoria not found")
    void givenUnregisteredId_whenBuscarPeloId_thenCategoriaNotFoundException() {
        // given
        doThrow(new CategoriaNaoEncontradaException("Categoria não cadastrada"))
                .when(crudCategoriaServiceMock).buscarPeloId(anyLong());
        Long id = categoria.getId();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloId(id))
                .isInstanceOf(CategoriaNaoEncontradaException.class)
                .hasMessageContaining("Categoria não cadastrada");
        // then
        verify(crudCategoriaServiceMock, times(1)).buscarPeloId(anyLong());
    }

    @Test
    @DisplayName("inserir() save categoria and return categoriaResponse")
    void givenCategoriaRequest_whenInserir_thenCategoriaShouldBeInserted() {
        // given
        given(crudCategoriaServiceMock.inserir(any(CategoriaRequest.class))).willReturn(categoriaResponse);
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        // when
        ResponseEntity<CategoriaResponse> expected = underTest.inserir(categoriaRequest);
        // then
        verify(crudCategoriaServiceMock, times(1)).inserir(any(CategoriaRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(categoriaResponse);
    }

    @Test
    @DisplayName("inserir() throws CategoriaAlreadyExistsException when categoria already exists")
    void givenCategoriaRequestAlreadyRegistered_whenInserir_thenThrowsCategoriaAlreadyExistsException() {
        // given
        doThrow(new CategoriaAlreadyExistsException("Já existe uma categoria cadastrada com esse NOME: " +categoria.getNome())).when(crudCategoriaServiceMock).inserir(any(CategoriaRequest.class));
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        // when
        assertThatThrownBy(() -> underTest.inserir(categoriaRequest))
                .isInstanceOf(CategoriaAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma categoria cadastrada com esse NOME: " +categoriaRequest.getNome());
        // then
        verify(crudCategoriaServiceMock, times(1)).inserir(any(CategoriaRequest.class));
    }

    @Test
    @DisplayName("alterar() updated categoria")
    void givenIdAndCategoriaRequest_whenAlterar_thenCategoriaShouldBeUpdated() {
        // given
        given(crudCategoriaServiceMock.alterar(anyLong(), any(CategoriaRequest.class)))
                .willReturn(categoriaResponse);
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        // when
        ResponseEntity<CategoriaResponse> expected = underTest.alterar(categoria.getId(), categoriaRequest);
        // then
        verify(crudCategoriaServiceMock, times(1))
                .alterar(anyLong(), any(CategoriaRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(categoriaResponse);
    }

    @Test
    @DisplayName("alterar() Throws CategoriaNaoEncontradException when categoria not found")
    void givenUnregisteredIdAndCategoriaRequest_whenAlterar_thenThrowsCategoriaNaoEncontradaExceotion() {
        // given
        doThrow(new CategoriaNaoEncontradaException("Categoria não cadastrada"))
                .when(crudCategoriaServiceMock).alterar(anyLong(), any(CategoriaRequest.class));
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        Long id = categoria.getId();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, categoriaRequest))
                .isInstanceOf(CategoriaNaoEncontradaException.class)
                .hasMessageContaining("Categoria não cadastrada");
        // then
        verify(crudCategoriaServiceMock, times(1)).alterar(anyLong(), any(CategoriaRequest.class));
    }

    @Test
    @DisplayName("alterar() Throws CategoriaAlreadyExistsException when categoria already exists")
    void givenIdAndAlreadyExistsCategoriaRequest_whenAlterar_thenThrowsCategoriaAlreadyExistsException() {
        // given
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        doThrow(new CategoriaAlreadyExistsException("Já existe uma categoria cadastrada com esse NOME: "+categoriaRequest.getNome()))
                .when(crudCategoriaServiceMock).alterar(anyLong(), any(CategoriaRequest.class));
        Long id = categoria.getId();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, categoriaRequest))
                .isInstanceOf(CategoriaAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma categoria cadastrada com esse NOME: "+categoriaRequest.getNome());
        // then
        verify(crudCategoriaServiceMock, times(1)).alterar(anyLong(), any(CategoriaRequest.class));
    }

    @Test
    @DisplayName("excluir() removed categoria")
    void givenId_whenExcluir_thenCategoriaShouldBeRemoved() {
        // given
        doNothing().when(crudCategoriaServiceMock).excluir(anyLong());
        // when
        ResponseEntity<Void> expected = underTest.excluir(categoria.getId());
        // then
        verify(crudCategoriaServiceMock, times(1)).excluir(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("excluir() throws CategoriaNaoEncotradaException when categoria not found")
    void givenUnregisteredId_whenExcluir_thenThrowsCategoriaNaoEncontradaExceotion() {
        // given
        doThrow(new CategoriaNaoEncontradaException("Categoria não cadastrada"))
                .when(crudCategoriaServiceMock).excluir(anyLong());
        Long id = categoria.getId();
        // when
        assertThatThrownBy(() -> underTest.excluir(id))
                .isInstanceOf(CategoriaNaoEncontradaException.class)
                .hasMessageContaining("Categoria não cadastrada");
        // then
        verify(crudCategoriaServiceMock, times(1)).excluir(anyLong());
    }
}