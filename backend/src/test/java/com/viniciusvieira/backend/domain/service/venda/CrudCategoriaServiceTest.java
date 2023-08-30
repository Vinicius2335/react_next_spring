package com.viniciusvieira.backend.domain.service.venda;

import com.viniciusvieira.backend.api.mapper.venda.CategoriaMapper;
import com.viniciusvieira.backend.api.representation.model.request.venda.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CategoriaResponse;
import com.viniciusvieira.backend.domain.exception.CategoriaAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.CategoriaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import com.viniciusvieira.backend.util.CategoriaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class CrudCategoriaServiceTest {
    @InjectMocks
    private CrudCategoriaService underTest;

    @Mock
    private CategoriaRepository categoriaRepositoryMock;
    @Mock
    private CategoriaMapper categoriaMapperMock;

    private final Categoria categoria = CategoriaCreator.createCategoria();
    private final CategoriaResponse categoriaResponse = CategoriaCreator.createCategoriaResponse();

    @Test
    @DisplayName("buscarTodos() return list categoria")
    void whenBuscarTodos_thenReturnListCategoria() {
        // given
        perfectPathConfig();
        // when
        List<Categoria> expected = underTest.buscarTodos();
        // then
        verify(categoriaRepositoryMock, times(1)).findAll();
        assertThat(expected)
                .hasSize(1)
                .contains(categoria);
    }

    @Test
    @DisplayName("buscarPeloId() return categoria")
    void givenId_WhenBuscarPeloId_thenCategoriaShouldBeFound() {
        // given
        perfectPathConfig();
        // when
        Categoria expected = underTest.buscarPeloId(categoria.getId());
        // then
        verify(categoriaRepositoryMock, times(1)).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .isEqualTo(categoria);
    }

    @Test
    @DisplayName("buscarPeloId() throwns CategoriaNaoEncontradaException when categoria not found")
    void givenUnregisteredId_WhenBuscarPeloId_thenThrowsCategoriaNaoEncontradaException() {
        // given
        long id = categoria.getId();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloId(id))
                .isInstanceOf(CategoriaNaoEncontradaException.class)
                .hasMessageContaining("Categoria não cadastrada");
        // then
        verify(categoriaRepositoryMock, times(1)).findById(anyLong());
    }


    @Test
    @DisplayName("inserir() insert categoria")
    void givenCategoriaRequest_whenInserir_thenCategoriaShouldBeInserted() {
        // given
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        perfectPathConfig();
        lenient().when(categoriaRepositoryMock.findByNome(anyString())).thenReturn(Optional.empty());
        // when
        CategoriaResponse expected = underTest.inserir(categoriaRequest);
        // then
        verify(categoriaRepositoryMock, times(1)).saveAndFlush(any(Categoria.class));
        assertThat(expected)
                .isNotNull()
                .isEqualTo(categoriaResponse);
    }

    @Test
    @DisplayName("inserir() throws CategoriaAlreadyExistsException when categoria not found")
    void givenCategoriaRequestWithAlrealyExistsNome_whenInserir_thenThrowsCategoriaAlreadyExistsException() {
        // given
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        lenient().when(categoriaRepositoryMock.findByNome(anyString())).thenReturn(Optional.of(categoria));
        // when
        assertThatThrownBy(() -> underTest.inserir(categoriaRequest))
                .isInstanceOf(CategoriaAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma categoria cadastrada com o NOME: " + categoriaRequest.getNome());
        // then
        verify(categoriaRepositoryMock, never()).saveAndFlush(any(Categoria.class));
    }

    @Test
    @DisplayName("alterar() update categoria")
    void givenIdAndCategoriaRequest_whenAlterar_thenCategoriaShouldBeUpdated() {
        // given
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        categoriaRequest.setNome("Msi");
        perfectPathConfig();
        when(categoriaRepositoryMock.findByNome(anyString())).thenReturn(Optional.empty());
        // when
        CategoriaResponse expected = underTest.alterar(categoria.getId(), categoriaRequest);
        // then
        verify(categoriaRepositoryMock, times(1)).saveAndFlush(any(Categoria.class));
        assertThat(expected)
                .isNotNull()
                .isEqualTo(categoriaResponse);
    }

    @Test
    @DisplayName("alterar() throws CategoriaNaoEncontradaException when categoria not found")
    void givenUnregisteredIdAndCategoriaRequest_whenAlterar_thenThrowsCategoriaNaoEncontradaException() {
        // given
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        Long id = categoria.getId();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, categoriaRequest))
                .isInstanceOf(CategoriaNaoEncontradaException.class)
                .hasMessageContaining("Categoria não cadastrada");
        // then
        verify(categoriaRepositoryMock, never()).saveAndFlush(any(Categoria.class));
    }

    @Test
    @DisplayName("alterar() throws CategoriaAlreadyExistsException when categoria already exists")
    void givenIdAndAlreadyExistsCategoriaRequest_whenAlterar_thenThrowsCategoriaAlreadyExistsException() {
        // given
        CategoriaRequest categoriaRequest = CategoriaCreator.createCategoriaRequest();
        Long id = categoria.getId();
        when(categoriaRepositoryMock.findById(anyLong())).thenReturn(Optional.of(categoria));
        when(categoriaRepositoryMock.findByNome(anyString())).thenReturn(Optional.of(categoria));
        // when
        assertThatThrownBy(() -> underTest.alterar(id, categoriaRequest))
                .isInstanceOf(CategoriaAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma categoria cadastrada com o NOME: " + categoriaRequest.getNome());
        // then
        verify(categoriaRepositoryMock, never()).saveAndFlush(any(Categoria.class));
    }

    @Test
    @DisplayName("excluir() remove categoria")
    void givenId_whenExcluir_thenCategoriaShouldBeRemoved() {
        // given
        perfectPathConfig();
        // when
        underTest.excluir(categoria.getId());
        // then
        verify(categoriaRepositoryMock, times(1)).delete(any(Categoria.class));
    }

    @Test
    @DisplayName("excluir() throws CategoriaNaoEncontradaException when categoria not found")
    void givenUnregisteredId_whenExcluir_thenThrowsCategoriaNaoEncontradaException() {
        // given
        failPathConfig();
        Long id = categoria.getId();
        // when
        assertThatThrownBy(() -> underTest.excluir(id))
                .isInstanceOf(CategoriaNaoEncontradaException.class)
                .hasMessageContaining("Categoria não cadastrada");
        // then
        verify(categoriaRepositoryMock, never()).delete(any(Categoria.class));
    }

    private void perfectPathConfig(){
        lenient().when(categoriaRepositoryMock.findAll()).thenReturn(List.of(categoria));
        lenient().when(categoriaRepositoryMock.findById(anyLong())).thenReturn(Optional.of(categoria));
        lenient().when(categoriaRepositoryMock.findByNome(anyString())).thenReturn(Optional.of(categoria));
        lenient().when(categoriaMapperMock.toDomainCategoria(any(CategoriaRequest.class))).thenReturn(categoria);
        lenient().when(categoriaRepositoryMock.saveAndFlush(any(Categoria.class))).thenReturn(categoria);
        lenient().when(categoriaMapperMock.toCategoriaResponse(any(Categoria.class)))
                .thenReturn(categoriaResponse);
        lenient().doNothing().when(categoriaRepositoryMock).delete(any(Categoria.class));
    }

    private void failPathConfig(){
        lenient().when(categoriaRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        lenient().when(categoriaRepositoryMock.findByNome(anyString())).thenReturn(Optional.empty());
    }

}