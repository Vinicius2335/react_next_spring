package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PermissaoResponse;
import com.viniciusvieira.backend.domain.exception.usuario.PermissaoAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.usuario.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.service.usuario.CrudPermissaoService;
import com.viniciusvieira.backend.util.PermissaoCreator;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissaoControllerTest {
    @InjectMocks
    private PermissaoController underTest;

    @Mock
    private CrudPermissaoService crudPermissaoServiceMock;

    private final Permissao permissao = PermissaoCreator.createPermissao();
    private PermissaoResponse permissaoResponse;

    @BeforeEach
    void setUp() {
        permissaoResponse = PermissaoCreator.createPermissaoResponse(permissao);
    }

    @Test
    @DisplayName("buscarTodos() return list permissao")
    void whenBuscarTodos_thenReturnListPermissao() {
        // given
        given(crudPermissaoServiceMock.buscarTodos()).willReturn(List.of(permissao));
        // when
        ResponseEntity<List<Permissao>> expected = underTest.buscarTodos();
        // then
        verify(crudPermissaoServiceMock, times(1)).buscarTodos();
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .hasSize(1)
                .contains(permissao);
    }

    @Test
    @DisplayName("buscarPeloId() return permissao")
    void givenId_whenBuscarPeloId_thenPermissaoShouldBeFound() {
        // given
        given(crudPermissaoServiceMock.buscarPeloId(anyLong())).willReturn(permissao);
        // when
        ResponseEntity<Permissao> expected = underTest.buscarPeloId(permissao.getId());
        // then
        verify(crudPermissaoServiceMock, times(1)).buscarPeloId(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(permissao);
    }

    @Test
    @DisplayName("buscarPeloId() Throws PermissaoNaoEncontradException when permissao not found")
    void givenUnregisteredId_whenBuscarPeloId_thenPermissaoNotFoundException() {
        // given
        doThrow(new PermissaoNaoEncontradaException("Permissão não cadastrada"))
                .when(crudPermissaoServiceMock).buscarPeloId(anyLong());
        Long id = permissao.getId();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloId(id))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                        .hasMessageContaining("Permissão não cadastrada");
        // then
        verify(crudPermissaoServiceMock, times(1)).buscarPeloId(anyLong());
    }

    @Test
    @DisplayName("inserir() save permissao and return permissaoResponse")
    void givenPermissaoRequest_whenInserir_thenPermissaoShouldBeInserted() {
        // given
        given(crudPermissaoServiceMock.inserir(any(PermissaoRequest.class))).willReturn(permissaoResponse);
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        // when
        ResponseEntity<PermissaoResponse> expected = underTest.inserir(permissaoRequest);
        // then
        verify(crudPermissaoServiceMock, times(1)).inserir(any(PermissaoRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(permissaoResponse);
    }

    @Test
    @DisplayName("inserir() throws PermissaoAlreadyExistsException")
    void givenPermissaoRequestAlreadyRegistered_whenInserir_thenThrowsPermissaoAlreadyExistsException() {
        // given
        doThrow(new PermissaoAlreadyExistsException("Já existe uma permissao cadastrada com esse NOME: " +permissao.getNome())).when(crudPermissaoServiceMock).inserir(any(PermissaoRequest.class));
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        // when
        assertThatThrownBy(() -> underTest.inserir(permissaoRequest))
                .isInstanceOf(PermissaoAlreadyExistsException.class)
                        .hasMessageContaining("Já existe uma permissao cadastrada com esse NOME: CLIENTE");
        // then
        verify(crudPermissaoServiceMock, times(1)).inserir(any(PermissaoRequest.class));
    }

    @Test
    @DisplayName("alterar() updated permissao")
    void givenIdAndPermissaoRequest_whenAlterar_thenPermissaoShouldBeUpdated() {
        // given
        given(crudPermissaoServiceMock.alterar(anyLong(), any(PermissaoRequest.class)))
                .willReturn(permissaoResponse);
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        // when
        ResponseEntity<PermissaoResponse> expected = underTest.alterar(permissao.getId(), permissaoRequest);
        // then
        verify(crudPermissaoServiceMock, times(1))
                .alterar(anyLong(), any(PermissaoRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(permissaoResponse);
    }

    @Test
    @DisplayName("alterar() Throws PermissaoNaoEncontradException when permissao not found")
    void givenUnregisteredIdAndPermissaoRequest_whenAlterar_thenThrowsPermissaoNaoEncontradaExceotion() {
        // given
        doThrow(new PermissaoNaoEncontradaException("Permissão não cadastrada"))
                .when(crudPermissaoServiceMock).alterar(anyLong(), any(PermissaoRequest.class));
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        Long id = permissao.getId();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, permissaoRequest))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                .hasMessageContaining("Permissão não cadastrada");
        // then
        verify(crudPermissaoServiceMock, times(1)).alterar(anyLong(), any(PermissaoRequest.class));
    }

    @Test
    @DisplayName("alterar() Throws PermissaoAlreadyExistsException when permissao already exists")
    void givenIdAndAlreadyExistsPermissaoRequest_whenAlterar_thenThrowsPermissaoAlreadyExistsException() {
        // given
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        doThrow(new PermissaoAlreadyExistsException("Já existe uma permissao cadastrada com esse NOME: "+permissaoRequest.getNome()))
                .when(crudPermissaoServiceMock).alterar(anyLong(), any(PermissaoRequest.class));
        Long id = permissao.getId();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, permissaoRequest))
                .isInstanceOf(PermissaoAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma permissao cadastrada com esse NOME: "+permissaoRequest.getNome());
        // then
        verify(crudPermissaoServiceMock, times(1)).alterar(anyLong(), any(PermissaoRequest.class));
    }

    @Test
    @DisplayName("excluir() removed permissao")
    void givenId_whenExcluir_thenPermissaoShouldBeRemoved() {
        // given
        doNothing().when(crudPermissaoServiceMock).excluir(anyLong());
        // when
        ResponseEntity<Void> expected = underTest.excluir(permissao.getId());
        // then
        verify(crudPermissaoServiceMock, times(1)).excluir(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("excluir() throws PermissaoNaoEncotradaException")
    void givenUnregisteredId_whenExcluir_thenThrowsPermissaoNaoEncontradaExceotion() {
        // given
        doThrow(new PermissaoNaoEncontradaException("Permissão não cadastrada"))
                .when(crudPermissaoServiceMock).excluir(anyLong());
        Long id = permissao.getId();
        // when
        assertThatThrownBy(() -> underTest.excluir(id))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                .hasMessageContaining("Permissão não cadastrada");
        // then
        verify(crudPermissaoServiceMock, times(1)).excluir(anyLong());
    }
}