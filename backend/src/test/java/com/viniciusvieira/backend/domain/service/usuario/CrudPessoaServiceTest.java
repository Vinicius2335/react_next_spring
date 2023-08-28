package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.mapper.usuario.PessoaMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

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
    private PessoaRepository mockPessoaRepository;
    @Mock
    private PessoaMapper mockPessoaMapper;
    @Mock
    private CrudPermissaoService mockPermissaoService;

    private final Pessoa pessoa = PessoaCreator.createPessoa();

    @Test
    void whenBuscarTodos_thenReturnAllPessoas() {
        // given
        // when
        underTest.buscarTodos();
        // then
        verify(mockPessoaRepository).findAll();
    }

    @Test
    void givenEmail_whenBuscarPeloEmail_thenPessoaShouldBeFound() {
        // given
        String email = pessoa.getEmail();
        given(mockPessoaRepository.findByEmail(email)).willReturn(Optional.of(pessoa));
        // when
        Pessoa expected = underTest.buscarPeloEmail(email);
        // then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockPessoaRepository, times(1)).findByEmail(captor.capture());
        String captorEmailValue = captor.getValue();

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoa);
        assertThat(captorEmailValue).isEqualTo(email);
    }

    @Test
    void givenUnregisteredEmail_whenBuscarPeloEmail_thenThrowsPessoaNaoEncontradaException() {
        // given
        String email = pessoa.getEmail();
        given(mockPessoaRepository.findByEmail(email)).willReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> underTest.buscarPeloEmail(email))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este EMAIL");
        // then
        verify(mockPessoaRepository).findByEmail(anyString());
    }

    @Test
    void givenId_whenBuscarPorId_thenPessoaShouldBeFound() {
        // given
        Long id = pessoa.getId();
        given(mockPessoaRepository.findById(id)).willReturn(Optional.of(pessoa));
        // when
        Pessoa expected = underTest.buscarPorId(id);
        // then
        verify(mockPessoaRepository).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoa);
    }

    @Test
    void givenUnregisteredId_whenBuscarPorId_thenThorwsPessoaNaoEncontradaException() {
        // given
        Long id = pessoa.getId();
        given(mockPessoaRepository.findById(id)).willReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> underTest.buscarPorId(id))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                        .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(mockPessoaRepository).findById(anyLong());
    }

    @Test
    void givenEmailAndCodigo_whenBuscarPeloEmailECodigo_thenPessoaShouldBeFound() {
        // given
        String codigoRecuperacaoSenha = pessoa.getCodigoRecuperacaoSenha();
        String email = pessoa.getEmail();
        given(mockPessoaRepository.findByEmailAndCodigoRecuperacaoSenha(email, codigoRecuperacaoSenha)).willReturn(Optional.of(pessoa));
        // when
        Pessoa expected = underTest.buscarPeloEmailECodigo(email, codigoRecuperacaoSenha);
        // then
        verify(mockPessoaRepository).findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString());
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoa);
    }

    @Test
    void givenPessoa_whenInserir_thenPessoaShouldBeInserted() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        given(mockPermissaoService.buscarPeloNome("CLIENTE")).willReturn(PermissaoCreator.createPermissao());
        given(mockPessoaMapper.toDomainPessoa(pessoaRequest)).willReturn(pessoa);
        given(mockPessoaRepository.findByCpf(pessoaRequest.getCpf())).willReturn(Optional.empty());
        given(mockPessoaRepository.saveAndFlush(pessoa)).willReturn(pessoa);
        given(mockPessoaMapper.toPessoaResponse(pessoa)).willReturn(getPessoaResponse());
        // when
        PessoaResponse expected = underTest.inserir(pessoaRequest);
        // then
        verify(mockPermissaoService).buscarPeloNome(anyString());
        verify(mockPessoaMapper).toDomainPessoa(any(PessoaRequest.class));
        verify(mockPessoaRepository).findByCpf(anyString());
        verify(mockPessoaRepository).saveAndFlush(any(Pessoa.class));
        verify(mockPessoaMapper).toPessoaResponse(any(Pessoa.class));

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(getPessoaResponse());
    }

    @Test
    void givenPessoaWithCpfRegistered_whenInserir_thenThrowsNegocioException() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        given(mockPermissaoService.buscarPeloNome("CLIENTE")).willReturn(PermissaoCreator.createPermissao());
        given(mockPessoaMapper.toDomainPessoa(pessoaRequest)).willReturn(pessoa);
        given(mockPessoaRepository.findByCpf(pessoaRequest.getCpf())).willReturn(Optional.of(pessoa));
        // when
         assertThatThrownBy(() -> underTest.inserir(pessoaRequest))
                 .isInstanceOf(NegocioException.class)
                         .hasMessageContaining("Já existe uma pessoa cadastrada com esse CPF");
        // then
        verify(mockPermissaoService).buscarPeloNome(anyString());
        verify(mockPessoaMapper).toDomainPessoa(any(PessoaRequest.class));
        verify(mockPessoaRepository).findByCpf(anyString());
        verify(mockPessoaRepository, never()).saveAndFlush(any(Pessoa.class));
        verify(mockPessoaMapper, never()).toPessoaResponse(any(Pessoa.class));
    }

    @Test
    void givenPessoa_whenToPessoaResponse_returnPessoaResponse(){
        // given
        PessoaResponse pessoaResponse = getPessoaResponse();
        System.out.println("Response "+pessoaResponse);

        when(mockPessoaMapper.toPessoaResponse(any())).thenReturn(pessoaResponse);
        // when
        PessoaResponse expected = mockPessoaMapper.toPessoaResponse(pessoa);
        System.out.println("expected "+expected);
        // then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("dataCriacao", "dataAtualizacao")
                .isEqualTo(pessoaResponse);
    }

    @Test
    void givenPessoa_whenAlterar_thenPessoaShouldBeUpdated() {
        // given
        Long id = 1L;
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        pessoaRequest.setNome("Vinicius");
        given(mockPessoaRepository.findById(anyLong())).willReturn(Optional.of(pessoa));
        given(mockPessoaMapper.toDomainPessoa(pessoaRequest)).willReturn(pessoa);
        given(mockPessoaRepository.saveAndFlush(pessoa)).willReturn(pessoa);
        given(mockPessoaMapper.toPessoaResponse(pessoa)).willReturn(getPessoaResponse());
        // when
        PessoaResponse expected = underTest.alterar(id, pessoaRequest);
        // then
        verify(mockPessoaRepository).findById(anyLong());
        verify(mockPessoaMapper).toDomainPessoa(any(PessoaRequest.class));
        verify(mockPessoaRepository).saveAndFlush(any(Pessoa.class));
        verify(mockPessoaMapper).toPessoaResponse(any(Pessoa.class));

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "dataCriacao", "dataAtualizacao")
                .isEqualTo(getPessoaResponse());
    }

    @Test
    void givenUnregisteredPessoa_whenAlterar_thenThrowsPessoaNaoEncontradaException() {
        // given
        Long id = 99L;
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        pessoaRequest.setNome("Vinicius");
        given(mockPessoaRepository.findById(anyLong())).willReturn(Optional.empty());
        // when
         assertThatThrownBy(() -> underTest.alterar(id, pessoaRequest))
                 .isInstanceOf(PessoaNaoEncontradaException.class)
                         .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(mockPessoaRepository).findById(anyLong());
        verify(mockPessoaMapper, never()).toDomainPessoa(any(PessoaRequest.class));
        verify(mockPessoaRepository, never()).saveAndFlush(any(Pessoa.class));
        verify(mockPessoaMapper, never()).toPessoaResponse(any(Pessoa.class));
    }

    @Test
    @Disabled
    void alterarParaGerenciamento() {
    }

    @Test
    @Disabled
    void excluir() {
    }

    @Test
    @Disabled
    void excluirPermissao() {
    }

    private Pessoa getPessoaInserted() {
        return mockPessoaRepository.save(pessoa);
    }

    private PessoaResponse getPessoaResponse(){
        return PessoaCreator.createPessoaResponse(pessoa);
    }
}