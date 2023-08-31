package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.mapper.usuario.PessoaMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.usuario.CpfAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.usuario.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.Disabled;
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

    private final Pessoa pessoa = PessoaCreator.createPessoa();


    @Test
    @DisplayName("buscarTodos() return list pessoa")
    void whenBuscarTodos_thenReturnAllPessoas() {
        // given
        perfectPathConfig();
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
        perfectPathConfig();
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

    @Test
    @DisplayName("buscarPeloEmail() throws PessoaNaoEncontradaExecption when pessoa not found")
    void givenUnregisteredEmail_whenBuscarPeloEmail_thenThrowsPessoaNaoEncontradaException() {
        // given
        String email = pessoa.getEmail();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloEmail(email))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este EMAIL");
        // then
        verify(pessoaRepositoryMock, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("buscarPorId() return pessoa")
    void givenId_whenBuscarPorId_thenPessoaShouldBeFound() {
        // given
        Long id = pessoa.getId();
        perfectPathConfig();
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

    @Test
    @DisplayName("buscarPorId() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredId_whenBuscarPorId_thenThorwsPessoaNaoEncontradaException() {
        // given
        Long id = pessoa.getId();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPorId(id))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                        .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(pessoaRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("buscarPeloEmailECodigo() return pessoa")
    void givenEmailAndCodigo_whenBuscarPeloEmailECodigo_thenPessoaShouldBeFound() {
        // given
        String codigoRecuperacaoSenha = pessoa.getCodigoRecuperacaoSenha();
        String email = pessoa.getEmail();
        perfectPathConfig();
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

    @Test
    @DisplayName("buscarPeloEmailECodigo() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredEmailAndCodigo_whenBuscarPeloEmailECodigo_thenPessoaNaoEncontradaException() {
        // given
        String codigoRecuperacaoSenha = pessoa.getCodigoRecuperacaoSenha();
        String email = pessoa.getEmail();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloEmailECodigo(email, codigoRecuperacaoSenha))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este EMAIL e CODIGO");
        // then
        verify(pessoaRepositoryMock, times(1))
                .findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString());
    }

    @Test
    @DisplayName("inserir() save pessoa and return pessoaResponse")
    void givenPessoa_whenInserir_thenPessoaShouldBeInserted() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        perfectPathConfig();
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

    @Test
    @DisplayName("buscarPeloEmailECodigo() throws CpfAlreadyExistsException when cpf is already registered")
    void givenPessoaWithCpfRegistered_whenInserir_thenCpfAlreadyExistsException() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        failPathConfig();
        // when
         assertThatThrownBy(() -> underTest.inserir(pessoaRequest))
                 .isInstanceOf(CpfAlreadyExistsException.class)
                         .hasMessageContaining("Já existe uma pessoa cadastrada com esse CPF");
        // then
        verify(pessoaRepositoryMock, never()).saveAndFlush(any(Pessoa.class));
    }

    @Test
    void givenPessoa_whenToPessoaResponse_returnPessoaResponse(){
        // given
        PessoaResponse pessoaResponse = getPessoaResponse();
        perfectPathConfig();
        // when
        PessoaResponse expected = pessoaMapperMock.toPessoaResponse(pessoa);
        System.out.println("expected "+expected);
        // then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoaResponse);
    }

    @Test
    @DisplayName("alterar() update pessoa and return pessoaResponse")
    void givenIdAndPessoaRequest_whenAlterar_thenPessoaShouldBeUpdated() {
        // given
        Long id = 1L;
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        pessoaRequest.setNome("Vinicius");
        perfectPathConfig();
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

    @Test
    @DisplayName("alterar() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredIdAndPessoaRequest_whenAlterar_thenThrowsPessoaNaoEncontradaException() {
        // given
        Long id = 99L;
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        pessoaRequest.setNome("Vinicius");
        failPathConfig();
        // when
         assertThatThrownBy(() -> underTest.alterar(id, pessoaRequest))
                 .isInstanceOf(PessoaNaoEncontradaException.class)
                         .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(pessoaRepositoryMock, never()).saveAndFlush(any(Pessoa.class));
    }

    @Test
    @DisplayName("alterar() throws CpfAlreadyExistsException when cpf already exists")
    void givenAlreadyRegisteredPessoaRequest_whenAlterar_thenThrowsCpfAlreadyExistsException() {
        // given
        Long id = 99L;
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        pessoaRequest.setNome("Vinicius");
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, pessoaRequest))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(pessoaRepositoryMock, never()).saveAndFlush(any(Pessoa.class));
    }

    @Test
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
        perfectPathConfig();
        // when
        underTest.excluir(pessoa.getId());
        // then
        verify(pessoaRepositoryMock, times(1)).delete(any(Pessoa.class));
    }

    @Test
    @DisplayName("excluir() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredId_whenExcluir_thenThrowsPessoaNaoEncontradaException() {
        // given
        Long id = pessoa.getId();
        failPathConfig();
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
        perfectPathConfig();
        // when
        underTest.excluirPermissao(1L, 1L);
        // then
        verify(pessoaRepositoryMock, times(1)).saveAndFlush(any(Pessoa.class));
        assertThat(pessoa.getPermissoes()).isEmpty();
    }

    @Test
    @DisplayName("excluirPermissao() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredIdPessoaAndIdPermissao_whenExcluirPermissao_thenPessoaNaoEncontradaException() {
        // given
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.excluirPermissao(20L, 1L))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(pessoaRepositoryMock, never()).saveAndFlush(any(Pessoa.class));
    }

    private PessoaResponse getPessoaResponse(){
        return PessoaCreator.createPessoaResponse(pessoa);
    }

    private void perfectPathConfig(){
        lenient().when(pessoaRepositoryMock.findAll()).thenReturn(List.of(pessoa));
        lenient().when(pessoaRepositoryMock.findByEmail(anyString())).thenReturn(Optional.of(pessoa));
        lenient().when(pessoaRepositoryMock.findById(anyLong())).thenReturn(Optional.of(pessoa));
        lenient().when(pessoaRepositoryMock.findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString())).thenReturn(Optional.of(pessoa));
        lenient().when(permissaoServiceMock.buscarPeloNome(anyString())).thenReturn(PermissaoCreator.createPermissao());
        lenient().when(pessoaMapperMock.toDomainPessoa(any(PessoaRequest.class))).thenReturn(pessoa);
        lenient().when(pessoaRepositoryMock.findByCpf(anyString())).thenReturn(Optional.empty());
        lenient().when(pessoaRepositoryMock.saveAndFlush(any(Pessoa.class))).thenReturn(pessoa);
        lenient().when(pessoaMapperMock.toPessoaResponse(any(Pessoa.class))).thenReturn(getPessoaResponse());
        lenient().doNothing().when(pessoaRepositoryMock).delete(any(Pessoa.class));
    }

    private void failPathConfig(){
        lenient().when(pessoaRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());
        lenient().when(pessoaRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        lenient().when(pessoaRepositoryMock.findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString())).thenReturn(Optional.empty());
        lenient().when(permissaoServiceMock.buscarPeloNome(anyString())).thenReturn(PermissaoCreator.createPermissao());
        lenient().when(pessoaMapperMock.toDomainPessoa(any(PessoaRequest.class))).thenReturn(pessoa);
        lenient().when(pessoaRepositoryMock.findByCpf(anyString())).thenReturn(Optional.of(pessoa));
    }
}