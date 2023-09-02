package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.mapper.usuario.PermissaoMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PermissaoResponse;
import com.viniciusvieira.backend.domain.exception.usuario.PermissaoAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.usuario.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.BDDMockito.given;
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
    private PermissaoResponse permissaoResponse;

    @BeforeEach
    void setUp() {
        permissaoResponse = PermissaoCreator.createPermissaoResponse(permissao);
    }

    @Test
    @DisplayName("buscarTodos() return list permissao")
    void whenBuscarTodos_thenReturnListPermissao() {
        // given
        given(permissaoRepositoryMock.findAll()).willReturn(List.of(permissao));
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
        findByIdConfig();
        // when
        Permissao expected = underTest.buscarPeloId(permissao.getId());
        // then
        verify(permissaoRepositoryMock, times(1)).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .isEqualTo(permissao);
    }

    private void findByIdConfig(){
        given(permissaoRepositoryMock.findById(anyLong())).willReturn(Optional.of(permissao));
    }

    @Test
    @DisplayName("buscarPeloId() throwns PermissaoNaoEncontradaException when permissao not found")
    void givenUnregisteredId_WhenBuscarPeloId_thenThrowsPermissaoNaoEncontradaException() {
        // given
        long id = permissao.getId();
        findByIdEmptyConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloId(id))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                        .hasMessageContaining("Permissão não cadastrada");
        // then
        verify(permissaoRepositoryMock, times(1)).findById(anyLong());
    }

    private void findByIdEmptyConfig(){
        given(permissaoRepositoryMock.findById(anyLong())).willReturn(Optional.empty());
    }

    @Test
    @DisplayName("buscarPeloNome() return permissao")
    void givenNome_whenBuscarPeloNome_thenPermissaoShouldBeFound() {
        // given
        findByNomeConfig();
        // when
        Permissao expected = underTest.buscarPeloNome(permissao.getNome());
        // then
        verify(permissaoRepositoryMock, times(1)).findByNome(anyString());
        assertThat(expected)
                .isNotNull()
                .isEqualTo(permissao);
    }

    private void findByNomeConfig(){
        given(permissaoRepositoryMock.findByNome(anyString())).willReturn(Optional.of(permissao));
    }

    @Test
    @DisplayName("buscarPeloNome() throwns PermissaoNaoEncontradaException when permissao not found")
    void givenUnregisteredNome_whenBuscarPeloNome_thenThrowsPermissaoNaoEncontradaException() {
        // given
        String nome = permissao.getNome();
        findByNomeEmptyConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloNome(nome))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                        .hasMessageContaining("Permissão não encontrada");
        // then
        verify(permissaoRepositoryMock, times(1)).findByNome(anyString());
    }

    private void findByNomeEmptyConfig(){
        given(permissaoRepositoryMock.findByNome(anyString())).willReturn(Optional.empty());
    }

    @Test
    @DisplayName("inserir() insert permissao")
    void givenPermissaoRequest_whenInserir_thenPermissaoShouldBeInserted() {
        // given
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        inserirConfig();
        // when
        PermissaoResponse expected = underTest.inserir(permissaoRequest);
        // then
        verify(permissaoRepositoryMock, times(1)).saveAndFlush(any(Permissao.class));
        assertThat(expected)
                .isNotNull()
                .isEqualTo(permissaoResponse);
    }

    private void inserirConfig(){
        findByNomeEmptyConfig();
        mockValidSave();
    }

    private void mockValidSave() {
        given(permissaoMapperMock.toDomainPermissao(any(PermissaoRequest.class))).willReturn(permissao);
        given(permissaoRepositoryMock.saveAndFlush(any(Permissao.class))).willReturn(permissao);
        given(permissaoMapperMock.toPermissaoResponse(any(Permissao.class))).willReturn(permissaoResponse);
    }

    @Test
    @DisplayName("inserir() throws PermissaoAlreadyExistsException when nome permissao in use")
    void givenPermissaoRequestWithAlrealyExistsNome_whenInserir_thenThrowsPermissaoAlreadyExistsException() {
        // given
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        findByNomeConfig();
        // when
        assertThatThrownBy(() -> underTest.inserir(permissaoRequest))
                .isInstanceOf(PermissaoAlreadyExistsException.class)
                        .hasMessageContaining("Já existe uma permissao cadastrada com esse NOME");
        // then
        verify(permissaoRepositoryMock, never()).saveAndFlush(any(Permissao.class));
    }

    @Test
    @DisplayName("alterar() update permissao")
    void givenIdAndPermissaoRequest_whenAlterar_thenPermissaoShouldBeUpdated() {
        // given
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        permissaoRequest.setNome("FUNCIONARIO");
        alterarConfig();
        // when
        PermissaoResponse expected = underTest.alterar(permissao.getId(), permissaoRequest);
        // then
        verify(permissaoRepositoryMock, times(1)).saveAndFlush(any(Permissao.class));
        assertThat(expected)
                .isNotNull()
                .isEqualTo(permissaoResponse);
    }

    private void alterarConfig(){
        findByIdConfig();
        findByNomeEmptyConfig();
        mockValidSave();
    }

    @Test
    @DisplayName("alterar() throws PermissaoNaoEncontradaException when permissao not found")
    void givenUnregisteredIdAndPermissaoRequest_whenAlterar_thenThrowsPermissaoNaoEncontradaException() {
        // given
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        Long id = permissao.getId();
        findByIdEmptyConfig();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, permissaoRequest))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                        .hasMessageContaining("Permissão não cadastrada");
        // then
        verify(permissaoRepositoryMock, never()).saveAndFlush(any(Permissao.class));
    }

    @Test
    @DisplayName("alterar() throws PermissaoAlreadyExistsException when permissao already exists")
    void givenIdAndAlreadyExistsPermissaoRequest_whenAlterar_thenThrowsPermissaoAlreadyExistsException() {
        // given
        PermissaoRequest permissaoRequest = PermissaoCreator.createPermissaoRequest();
        Long id = permissao.getId();
        findByIdConfig();
        findByNomeConfig();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, permissaoRequest))
                .isInstanceOf(PermissaoAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma permissao cadastrada com esse NOME: " +permissaoRequest.getNome());
        // then
        verify(permissaoRepositoryMock, never()).saveAndFlush(any(Permissao.class));
    }

    @Test
    @DisplayName("excluir() remove permissao")
    void givenId_whenExcluir_thenPermissaoShouldBeRemoved() {
        // given
        excluirConfig();
        // when
        underTest.excluir(permissao.getId());
        // then
        verify(permissaoRepositoryMock, times(1)).delete(any(Permissao.class));
    }

    private void excluirConfig(){
        findByIdConfig();
        doNothing().when(permissaoRepositoryMock).delete(any(Permissao.class));
    }

    @Test
    @DisplayName("excluir() throws PermissaoNaoEncontradaException when permissao not found")
    void givenUnregisteredId_whenExcluir_thenThrowsPermissaoNaoEncontradaException() {
        // given
        findByIdEmptyConfig();
        Long id = permissao.getId();
        // when
        assertThatThrownBy(() -> underTest.excluir(id))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                        .hasMessageContaining("são não cadastrada");
        // then
        verify(permissaoRepositoryMock, never()).delete(any(Permissao.class));
    }
}