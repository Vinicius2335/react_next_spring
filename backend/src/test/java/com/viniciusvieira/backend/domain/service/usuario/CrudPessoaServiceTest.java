package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.mapper.usuario.PessoaMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.usuario.CpfAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.usuario.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.exception.usuario.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.TokenRepository;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrudPessoaServiceTest {
    @InjectMocks
    private CrudPessoaService underTest;

    @Mock
    private PessoaRepository pessoaRepositoryMock;
    @Mock
    private PessoaMapper pessoaMapperMock;
    @Mock
    private CrudPermissaoService permissaoServiceMock;
    @Mock
    private TokenRepository tokenRepositoryMock;

    private final Pessoa pessoa = PessoaCreator.createPessoa();
    private final Permissao permissao = PermissaoCreator.createPermissao();


    @Test
    @DisplayName("buscarTodos() return list pessoa")
    void whenBuscarTodos_thenReturnAllPessoas() {
        // given
        given(pessoaRepositoryMock.findAll()).willReturn(List.of(pessoa));
        // when
        List<Pessoa> expected = underTest.buscarTodos();
        // then
        verify(pessoaRepositoryMock, times(1)).findAll();
        assertThat(expected)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(pessoa);
    }

    @Test
    @DisplayName("buscarPeloEmail() return pessoa")
    void givenEmail_whenBuscarPeloEmail_thenPessoaShouldBeFound() {
        // given
        String email = pessoa.getEmail();
        findByEmailConfig();
        // when
        Pessoa expected = underTest.buscarPeloEmail(email);
        // then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(pessoaRepositoryMock, times(1)).findByEmail(captor.capture());
        String captorEmailValue = captor.getValue();

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoa);
        assertThat(captorEmailValue).isEqualTo(email);
    }

    private void findByEmailConfig() {
        given(pessoaRepositoryMock.findByEmail(anyString())).willReturn(Optional.of(pessoa));
    }

    @Test
    @DisplayName("buscarPeloEmail() throws PessoaNaoEncontradaExecption when pessoa not found")
    void givenUnregisteredEmail_whenBuscarPeloEmail_thenThrowsPessoaNaoEncontradaException() {
        // given
        String email = pessoa.getEmail();
        findByEmailExceptionConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloEmail(email))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este EMAIL");
        // then
        verify(pessoaRepositoryMock, times(1)).findByEmail(anyString());
    }

    private void findByEmailExceptionConfig() {
        given(pessoaRepositoryMock.findByEmail(anyString())).willReturn(Optional.empty());
    }

    @Test
    @DisplayName("buscarPorId() return pessoa")
    void givenId_whenBuscarPorId_thenPessoaShouldBeFound() {
        // given
        Long id = pessoa.getId();
        findByIdConfig();
        // when
        Pessoa expected = underTest.buscarPorId(id);
        // then
        verify(pessoaRepositoryMock, times(1)).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoa);
    }

    private void findByIdConfig() {
        given(pessoaRepositoryMock.findById(anyLong())).willReturn(Optional.of(pessoa));
    }

    @Test
    @DisplayName("buscarPorId() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredId_whenBuscarPorId_thenThorwsPessoaNaoEncontradaException() {
        // given
        Long id = pessoa.getId();
        findByIdExceptionConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPorId(id))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(pessoaRepositoryMock, times(1)).findById(anyLong());
    }

    private void findByIdExceptionConfig() {
        given(pessoaRepositoryMock.findById(anyLong())).willReturn(Optional.empty());
    }

    @Test
    @DisplayName("buscarPermissoes() return list of Permissao related to pessoa found")
    void givenId_whenBuscarPermissoes_thenListPessoasShouldBeFound() {
        // given
        buscarPermissoesConfig();
        // when
        List<Permissao> expected = underTest.buscarPermissoes(1L);
        // then
        verify(pessoaRepositoryMock, times(1)).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(permissao);
    }

    private void buscarPermissoesConfig() {
        findByIdConfig();
        pessoa.getPermissoes().add(permissao);
    }

    @Test
    @DisplayName("buscarPermissoes() throws PessoaNaoEncontradaException when pessoa not found by id")
    void givenUnregisteredId_whenBuscarPermissoes_thenThrowsPessoaNaoEncontradaException() {
        // given
        findByIdExceptionConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPermissoes(1L))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(pessoaRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("buscarPeloEmailECodigo() return pessoa")
    void givenEmailAndCodigo_whenBuscarPeloEmailECodigo_thenPessoaShouldBeFound() {
        // given
        pessoa.setCodigoRecuperacaoSenha("teste");
        String codigoRecuperacaoSenha = pessoa.getCodigoRecuperacaoSenha();
        String email = pessoa.getEmail();
        findByEmailECodigoConfig();
        // when
        Pessoa expected = underTest.buscarPeloEmailECodigo(email, codigoRecuperacaoSenha);
        // then
        verify(pessoaRepositoryMock, times(1)).findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString());

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoa);
    }

    private void findByEmailECodigoConfig() {
        given(pessoaRepositoryMock.findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString()))
                .willReturn(Optional.of(pessoa));
    }

    @Test
    @DisplayName("buscarPeloEmailECodigo() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredEmailAndCodigo_whenBuscarPeloEmailECodigo_thenPessoaNaoEncontradaException() {
        // given
        pessoa.setCodigoRecuperacaoSenha("teste");
        String codigoRecuperacaoSenha = pessoa.getCodigoRecuperacaoSenha();
        String email = pessoa.getEmail();
        findByEmailECodigoExceptionConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloEmailECodigo(email, codigoRecuperacaoSenha))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este EMAIL e CODIGO");
        // then
        verify(pessoaRepositoryMock, times(1))
                .findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString());
    }

    private void findByEmailECodigoExceptionConfig() {
        given(pessoaRepositoryMock.findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString()))
                .willReturn(Optional.empty());
    }

    @Test
    @DisplayName("inserir() save pessoa and return pessoaResponse")
    void givenPessoa_whenInserir_thenPessoaShouldBeInserted() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        inserirConfig();
        // when
        PessoaResponse expected = underTest.inserir(pessoaRequest);
        // then
        verify(pessoaRepositoryMock, times(1)).saveAndFlush(any(Pessoa.class));

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(getPessoaResponse());
    }

    private void inserirConfig() {
        given(permissaoServiceMock.buscarPeloNome(anyString())).willReturn(PermissaoCreator.createPermissao());
        mockValidSaveConfig();
    }

    private void mockValidSaveConfig() {
        given(pessoaRepositoryMock.findByCpf(anyString())).willReturn(Optional.empty());
        given(pessoaMapperMock.toDomainPessoa(any(PessoaRequest.class))).willReturn(pessoa);
        given(pessoaRepositoryMock.saveAndFlush(any(Pessoa.class))).willReturn(pessoa);
        given(pessoaMapperMock.toPessoaResponse(any(Pessoa.class))).willReturn(getPessoaResponse());
    }

    @Test
    @DisplayName("inserir() Throws PermissaoNaoEncontradaException when permissao not found by id")
    void givenUnregisteredPermissao_whenInserir_thenThrowsPessoaNaoEncontradaException() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        permissaoServiceBuscarPorNomeExceptionConfig();
        // when
        assertThatThrownBy(() -> underTest.inserir(pessoaRequest))
                .isInstanceOf(PermissaoNaoEncontradaException.class)
                .hasMessageContaining("Permissão não encontrada");
        // then
        verify(pessoaRepositoryMock, never()).saveAndFlush(any(Pessoa.class));
    }

    private void permissaoServiceBuscarPorNomeExceptionConfig() {
        doThrow(new PermissaoNaoEncontradaException("Permissão não encontrada"))
                .when(permissaoServiceMock).buscarPeloNome(anyString());
    }

    @Test
    @DisplayName("inserir() Throws CpfAlreadyExistsException when cpf in use")
    void givenCpfInUse_whenInserir_thenThrowsCpfAlreadyExistsException() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        findByCpfConfig();
        // when
        assertThatThrownBy(() -> underTest.inserir(pessoaRequest))
                .isInstanceOf(CpfAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma pessoa cadastrada com esse CPF");
        // then
        verify(pessoaRepositoryMock, never()).saveAndFlush(any(Pessoa.class));
    }

    private void findByCpfConfig() {
        given(pessoaRepositoryMock.findByCpf(anyString())).willReturn(Optional.of(pessoa));
    }

    @Test
    @DisplayName("alterar() update pessoa and return pessoaResponse")
    void givenIdAndPessoaRequest_whenAlterar_thenPessoaShouldBeUpdated() {
        // given
        Long id = 1L;
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        pessoaRequest.setNome("Vinicius");
        alterarConfig();
        // when
        PessoaResponse expected = underTest.alterar(id, pessoaRequest);
        // then
        verify(pessoaRepositoryMock, times(1)).saveAndFlush(any(Pessoa.class));

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(getPessoaResponse());
    }

    private void alterarConfig() {
        findByIdConfig();
        given(permissaoServiceMock.buscarPeloNome(anyString())).willReturn(permissao);
        given(pessoaMapperMock.toDomainPessoa(any(PessoaRequest.class))).willReturn(pessoa);
        given(pessoaRepositoryMock.saveAndFlush(any(Pessoa.class))).willReturn(pessoa);
        given(pessoaMapperMock.toPessoaResponse(any(Pessoa.class))).willReturn(getPessoaResponse());
    }

    @Test
    @DisplayName("alterar() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredIdAndPessoaRequest_whenAlterar_thenThrowsPessoaNaoEncontradaException() {
        // given
        Long id = 99L;
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        pessoaRequest.setNome("Vinicius");
        findByIdExceptionConfig();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, pessoaRequest))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(pessoaRepositoryMock, never()).saveAndFlush(any(Pessoa.class));
    }

    @Test
    @DisplayName("alterarParaGerenciamento() update pessoa")
    void givenPessoa_whenAlterarParaGerenciamento_thenPessoaShouldBeUpdated() {
        // given
        // when
        underTest.alterarParaGerenciamento(pessoa);
        // then
        verify(pessoaRepositoryMock, times(1)).saveAndFlush(any(Pessoa.class));
    }

    @Test
    @DisplayName("excluir() remove pessoa")
    void givenId_whenExcluir_thenPessoaShouldBeRemoved() {
        // given
        excluirConfig();
        // when
        underTest.excluir(pessoa.getId());
        // then
        verify(pessoaRepositoryMock, times(1)).delete(any(Pessoa.class));
    }

    private void excluirConfig() {
        findByIdConfig();
        doNothing().when(tokenRepositoryMock).deleteByPessoaId(anyLong());
        doNothing().when(pessoaRepositoryMock).delete(any(Pessoa.class));
    }

    @Test
    @DisplayName("excluir() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredId_whenExcluir_thenThrowsPessoaNaoEncontradaException() {
        // given
        Long id = pessoa.getId();
        findByIdExceptionConfig();
        // when
        assertThatThrownBy(() -> underTest.excluir(id))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(pessoaRepositoryMock, never()).delete(any(Pessoa.class));
    }

    @Test
    @DisplayName("excluirPermissao() remove permissao related a pessoa")
    void givenIdPessoaAndIdPermissao_whenExcluirPermissao_thenPessoaShouldBeRemovedPermissao() {
        // given
        Permissao permissao = PermissaoCreator.createPermissao();
        pessoa.adicionarPermissao(permissao);
        excluirPermissaoConfig();
        // when
        underTest.excluirPermissao(1L, 1L);
        // then
        verify(pessoaRepositoryMock, times(1)).saveAndFlush(any(Pessoa.class));
        assertThat(pessoa.getPermissoes()).isEmpty();
    }

    private void excluirPermissaoConfig() {
        findByIdConfig();
        given(pessoaRepositoryMock.saveAndFlush(any(Pessoa.class))).willReturn(pessoa);
    }

    @Test
    @DisplayName("excluirPermissao() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredIdPessoaAndIdPermissao_whenExcluirPermissao_thenPessoaNaoEncontradaException() {
        // given
        findByIdExceptionConfig();
        // when
        assertThatThrownBy(() -> underTest.excluirPermissao(20L, 1L))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(pessoaRepositoryMock, never()).saveAndFlush(any(Pessoa.class));
    }

    private PessoaResponse getPessoaResponse() {
        return PessoaCreator.createPessoaResponse(pessoa);
    }
}