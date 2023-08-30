package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.mapper.usuario.PermissaoMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PermissaoResponse;
import com.viniciusvieira.backend.domain.exception.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrudPermissaoServiceTest {
    @InjectMocks
    private CrudPermissaoService underTest;

    @Mock
    private PermissaoRepository permissaoRepositoryMock;
    @Mock
    private PermissaoMapper permissaoMapperMock;

    private final Permissao permissao = PermissaoCreator.createPermissao();
    private final PermissaoResponse permissaoResponse = PermissaoCreator.createPermissaoResponse();

    @Test
    @DisplayName("buscarTodos() return list permissao")
    void whenBuscarTodos_thenReturnListPermissao() {
        // given
        perfectPathConfig();
        // when
        List<Permissao> expected = underTest.buscarTodos();
        // then
        verify(permissaoRepositoryMock, times(1)).findAll();
        assertThat(expected)
                .hasSize(1)
                .contains(permissao);
    }

    @Test
    @DisplayName("buscarPeloId() return permissao")
    void givenId_WhenBuscarPeloId_thenPermissaoShouldBeFound() {
        // given
        perfectPathConfig();
        // when
        Permissao expected = underTest.buscarPeloId(permissao.getId());
        // then
        verify(permissaoRepositoryMock, times(1)).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .isEqualTo(permissao);
    }

    @Test
    @DisplayName("buscarPeloId() throwns PermissaoNaoEncontradaException")
    void givenUnregisteredId_WhenBuscarPeloId_thenThrowsPermissaoNaoEncontradaException() {
        // given
        long id = permissao.getId();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloId(id))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                        .hasMessageContaining("Permissão não cadastrada");
        // then
        verify(permissaoRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("buscarPeloNome() return permissao")
    void givenNome_whenBuscarPeloNome_thenPermissaoShouldBeFound() {
        // given
        perfectPathConfig();
        // when
        Permissao expected = underTest.buscarPeloNome(permissao.getNome());
        // then
        verify(permissaoRepositoryMock, times(1)).findByNome(anyString());
        assertThat(expected)
                .isNotNull()
                .isEqualTo(permissao);
    }

    @Test
    @DisplayName("buscarPeloNome() throwns PermissaoNaoEncontradaException")
    void givenUnregisteredNome_whenBuscarPeloNome_thenThrowsPermissaoNaoEncontradaException() {
        // given
        String nome = permissao.getNome();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloNome(nome))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                        .hasMessageContaining("Permissão não encontrada");
        // then
        verify(permissaoRepositoryMock, times(1)).findByNome(anyString());

    }

    @Test
    @DisplayName("inserir() insert permissao")
    void givenPermissaoRequest_whenInserir_thenPermissaoShouldBeInserted() {
        // given
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        perfectPathConfig();
        // when
        PermissaoResponse expected = underTest.inserir(permissaoRequest);
        // then
        verify(permissaoRepositoryMock, times(1)).saveAndFlush(any(Permissao.class));
        assertThat(expected)
                .isNotNull()
                .isEqualTo(permissaoResponse);
    }

    @Test
    @DisplayName("alterar() update permissao")
    void givenIdAndPermissaoRequest_whenAlterar_thenPermissaoShouldBeUpdated() {
        // given
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        perfectPathConfig();
        // when
        PermissaoResponse expected = underTest.alterar(permissao.getId(), permissaoRequest);
        // then
        verify(permissaoRepositoryMock, times(1)).saveAndFlush(any(Permissao.class));
        assertThat(expected)
                .isNotNull()
                .isEqualTo(permissaoResponse);
    }

    @Test
    @DisplayName("alterar() throws PermissaoNaoEncontradaException")
    void givenUnregisteredIdAndPermissaoRequest_whenAlterar_thenThrowsPermissaoNaoEncontradaException() {
        // given
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        Long id = permissao.getId();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, permissaoRequest))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                        .hasMessageContaining("Permissão não cadastrada");
        // then
        verify(permissaoRepositoryMock, never()).saveAndFlush(any(Permissao.class));

    }

    @Test
    @DisplayName("excluir() remove permissao")
    void givenId_whenExcluir_thenPermissaoShouldBeRemoved() {
        // given
        perfectPathConfig();
        // when
        underTest.excluir(permissao.getId());
        // then
        verify(permissaoRepositoryMock, times(1)).delete(any(Permissao.class));
    }

    @Test
    @DisplayName("excluir() throws PermissaoNaoEncontradaException")
    void givenUnregisteredId_whenExcluir_thenThrowsPermissaoNaoEncontradaException() {
        // given
        failPathConfig();
        Long id = permissao.getId();
        // when
        assertThatThrownBy(() -> underTest.excluir(id))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                        .hasMessageContaining("são não cadastrada");
        // then
        verify(permissaoRepositoryMock, never()).delete(any(Permissao.class));
    }

    private void perfectPathConfig(){
        lenient().when(permissaoRepositoryMock.findAll()).thenReturn(List.of(permissao));
        lenient().when(permissaoRepositoryMock.findById(anyLong())).thenReturn(Optional.of(permissao));
        lenient().when(permissaoRepositoryMock.findByNome(anyString())).thenReturn(Optional.of(permissao));
        lenient().when(permissaoMapperMock.toDomainPermissao(any(PermissaoRequest.class))).thenReturn(permissao);
        lenient().when(permissaoRepositoryMock.saveAndFlush(any(Permissao.class))).thenReturn(permissao);
        lenient().when(permissaoMapperMock.toPermissaoResponse(any(Permissao.class)))
                .thenReturn(permissaoResponse);
        lenient().doNothing().when(permissaoRepositoryMock).delete(any(Permissao.class));
    }

    private void failPathConfig(){
        lenient().when(permissaoRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        lenient().when(permissaoRepositoryMock.findByNome(anyString())).thenReturn(Optional.empty());
    }
}