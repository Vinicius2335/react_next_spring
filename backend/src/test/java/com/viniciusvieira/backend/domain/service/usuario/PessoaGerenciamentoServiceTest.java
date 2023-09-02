package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.usuario.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.EmailService;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaGerenciamentoServiceTest {
    @InjectMocks
    private PessoaGerenciamentoService underTest;
    @Mock
    private CrudPessoaService crudPessoaServiceMock;
    @Mock
    private EmailService emailServiceMock;

    private final Pessoa pessoa = PessoaCreator.createPessoa();

    private final LocalDateTime data = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        try (MockedStatic<LocalDateTime> dateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            dateTimeMockedStatic.when(LocalDateTime::now).thenReturn(data);
        }
    }

    @Test
    @DisplayName("solicitarCodigo() sends the email with the request code to change the password")
    void givenEmail_whenSolicitarCodigo_thenSendEmail() {
        // given
        given(crudPessoaServiceMock.buscarPeloEmail(anyString())).willReturn(pessoa);
        doNothing().when(crudPessoaServiceMock).alterarParaGerenciamento(any(Pessoa.class));
        doNothing().when(emailServiceMock).sendEmailSimples(anyString(), anyString(), anyString());
        // when
        underTest.solicitarCodigo(pessoa.getEmail());
        // then
        verify(emailServiceMock, times(1)).sendEmailSimples(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("solicitarCodigo() throws PessoaNaoEncontradaException when pessoa not found by email")
    void givenUnregisteredEmail_whenSolicitarCodigo_thenThrowsPessoaNaoEncontradaException() {
        // given
        doThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este EMAIL"))
                .when(crudPessoaServiceMock).buscarPeloEmail(anyString());
        String email = pessoa.getEmail();
        // when
        assertThatThrownBy(() -> underTest.solicitarCodigo(email))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                        .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este EMAIL");
        // then
        verify(emailServiceMock, never()).sendEmailSimples(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("solicitarCodigo() Throws NegocioException When an error occurs when you try to send an email")
    void givenEmail_whenSolicitarCodigo_thenThrowsNegocioException() {
        // given
        given(crudPessoaServiceMock.buscarPeloEmail(anyString())).willReturn(pessoa);
        doNothing().when(crudPessoaServiceMock).alterarParaGerenciamento(any(Pessoa.class));
        doThrow(new NegocioException("Erro ao tentar enviar um email ao cliente cadastrado"))
                .when(emailServiceMock).sendEmailSimples(anyString(), anyString(), anyString());
        String email = pessoa.getEmail();
        // when
        assertThatThrownBy(() -> underTest.solicitarCodigo(email))
                .isInstanceOf(NegocioException.class)
                        .hasMessageContaining("Erro ao tentar enviar um email ao cliente cadastrado");
        // then
        verify(emailServiceMock, times(1)).sendEmailSimples(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("alterarSenha() update pessoa")
    void givenPessoaGerencioamentoRequest_whenAlterarSenha_thenPessoaShouldBeUpdate() {
        // given
        given(crudPessoaServiceMock.buscarPeloEmailECodigo(anyString(), anyString())).willReturn(pessoa);
        doNothing().when(crudPessoaServiceMock).alterarParaGerenciamento(any(Pessoa.class));
        pessoa.setDataEnvioCodigo(data);
        pessoa.setCodigoRecuperacaoSenha("teste");
        PessoaGerenciamentoRequest pessoaGerenciamentoRequest = PessoaCreator.createPessoaGerenciamentoRequest(pessoa);
        // when
        underTest.alterarSenha(pessoaGerenciamentoRequest);
        // then
        verify(crudPessoaServiceMock, times(1)).alterarParaGerenciamento(any(Pessoa.class));
    }

    @Test
    @DisplayName("alterarSenha() Throws PessoaNaoEncontradaException when not found pessoa by email and codigo")
    void givenUnregisteredPessoaGerencioamentoRequest_whenAlterarSenha_thenThrowsPessoaNaoEncontradaException() {
        // given
        doThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este EMAIL e CODIGO"))
                .when(crudPessoaServiceMock).buscarPeloEmailECodigo(anyString(), anyString());
        pessoa.setDataEnvioCodigo(data);
        pessoa.setCodigoRecuperacaoSenha("teste");
        PessoaGerenciamentoRequest pessoaGerenciamentoRequest = PessoaCreator.createPessoaGerenciamentoRequest(pessoa);
        // when
        assertThatThrownBy(() -> underTest.alterarSenha(pessoaGerenciamentoRequest))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                        .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este EMAIL e CODIGO");
        // then
        verify(crudPessoaServiceMock, never()).alterarParaGerenciamento(any(Pessoa.class));
    }

    @Test
    @DisplayName("alterarSenha() Throws NegocioException when dataEnvioCodigo expired")
    void givenPessoaGerencioamentoRequest_whenAlterarSenha_thenThrowsNegocioException() {
        // given
        LocalDateTime localDateTime = data.plusHours(2);
        pessoa.setDataEnvioCodigo(localDateTime);
        pessoa.setCodigoRecuperacaoSenha("teste");
        given(crudPessoaServiceMock.buscarPeloEmailECodigo(anyString(), anyString())).willReturn(pessoa);
        PessoaGerenciamentoRequest pessoaGerenciamentoRequest = PessoaCreator.createPessoaGerenciamentoRequest(pessoa);
        // when
        assertThatThrownBy(() -> underTest.alterarSenha(pessoaGerenciamentoRequest))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining("Tempo expirado, solicite um novo código");
        // then
        verify(crudPessoaServiceMock, never()).alterarParaGerenciamento(any(Pessoa.class));
    }
}