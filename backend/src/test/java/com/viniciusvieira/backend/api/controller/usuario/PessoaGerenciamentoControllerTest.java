package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.usuario.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.usuario.PessoaGerenciamentoService;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaGerenciamentoControllerTest {
    @InjectMocks
    private PessoaGerenciamentoController underTest;

    @Mock
    private PessoaGerenciamentoService pessoaGerenciamentoServiceMock;

    private final Pessoa pessoa = PessoaCreator.createPessoa();


    @Test
    @DisplayName("recuperarCodigoViaEmail() send email")
    void givenEmail_whenRecuperarCodigoViaEmail_thenSendingEmail() {
        // given
        doNothing().when(pessoaGerenciamentoServiceMock).solicitarCodigo(anyString());
        String email = pessoa.getEmail();
        // when
        ResponseEntity<Void> expected = underTest.recuperarCodigoViaEmail(email);
        // then
        verify(pessoaGerenciamentoServiceMock, times(1)).solicitarCodigo(anyString());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(expected.getBody()).isNull();
    }

    @Test
    @DisplayName("recuperarCodigoViaEmail() Throws PessoaNaoEncontradaException when pessoa not found by email")
    void givenUnregisteredEmail_whenRecuperarCodigoViaEmail_thenSendingEmail() {
        // given
        doThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este EMAIL"))
                .when(pessoaGerenciamentoServiceMock).solicitarCodigo(anyString());
        String email = pessoa.getEmail();
        // when
        assertThatThrownBy(() -> underTest.recuperarCodigoViaEmail(email))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                        .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este EMAIL");
        // then
        verify(pessoaGerenciamentoServiceMock, times(1)).solicitarCodigo(anyString());
    }

    @Test
    @DisplayName("alterarSenha() update pessoa")
    void givenPessoaGerenciamentoRequest_whenAlterarSenha_thenPessoaShouldBeUpdated() {
        // given
        doNothing().when(pessoaGerenciamentoServiceMock).alterarSenha(any(PessoaGerenciamentoRequest.class));
        PessoaGerenciamentoRequest pessoaGerenciamentoRequest = PessoaCreator.createPessoaGerenciamentoRequest(pessoa);
        // when
        ResponseEntity<Void> expected = underTest.alterarSenha(pessoaGerenciamentoRequest);
        // then
        verify(pessoaGerenciamentoServiceMock, times(1))
                .alterarSenha(any(PessoaGerenciamentoRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(expected.getBody()).isNull();
    }

    @Test
    @DisplayName("alterarSenha() Throws PessoaNaoEncontradaException when pesso not found by emailAndCodigo")
    void givenUnregisteredPessoaGerenciamentoRequest_whenAlterarSenha_thenThrowsPessoaNaoEncontradaException() {
        // given
        doThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este EMAIL e CODIGO"))
                .when(pessoaGerenciamentoServiceMock).alterarSenha(any(PessoaGerenciamentoRequest.class));
        PessoaGerenciamentoRequest pessoaGerenciamentoRequest = PessoaCreator.createPessoaGerenciamentoRequest(pessoa);
        // when
        assertThatThrownBy(() ->  underTest.alterarSenha(pessoaGerenciamentoRequest))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                        .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este EMAIL e CODIGO");
        // then
        verify(pessoaGerenciamentoServiceMock, times(1))
                .alterarSenha(any(PessoaGerenciamentoRequest.class));
    }

    @Test
    @DisplayName("alterarSenha() Throws NegocioException when DataEnvioCodigo expired")
    void givenPessoaGerenciamentoRequest_whenAlterarSenha_thenThrowsNegocioException() {
        // given
        doThrow(new NegocioException("Tempo expirado, solicite um novo código"))
                .when(pessoaGerenciamentoServiceMock).alterarSenha(any(PessoaGerenciamentoRequest.class));
        PessoaGerenciamentoRequest pessoaGerenciamentoRequest = PessoaCreator.createPessoaGerenciamentoRequest(pessoa);
        // when
        assertThatThrownBy(() ->  underTest.alterarSenha(pessoaGerenciamentoRequest))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining("Tempo expirado, solicite um novo código");
        // then
        verify(pessoaGerenciamentoServiceMock, times(1))
                .alterarSenha(any(PessoaGerenciamentoRequest.class));
    }
}