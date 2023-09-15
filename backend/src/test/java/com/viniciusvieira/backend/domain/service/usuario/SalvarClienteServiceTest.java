package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.mapper.usuario.ClienteMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.CreateTemplateException;
import com.viniciusvieira.backend.domain.exception.usuario.CpfAlreadyExistsException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.domain.service.EmailService;
import com.viniciusvieira.backend.util.ClienteCreator;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SalvarClienteServiceTest {
    @InjectMocks
    private SalvarClienteService underTest;

    @Mock
    private  PessoaRepository pessoaRepositoryMock;
    @Mock
    private  ClienteMapper clienteMapperMock;
    @Mock
    private  CrudPermissaoService crudPermissaoServiceMock;
    @Mock
    private  EmailService emailServiceMock;

    private final Pessoa cliente = PessoaCreator.createPessoa();
    private PessoaResponse pessoaResponse;

    @BeforeEach
    void setUp() {
        pessoaResponse = PessoaCreator.createPessoaResponse(cliente);
        when(clienteMapperMock.toDomainPessoa(any(ClienteRequest.class))).thenReturn(cliente);
        when(pessoaRepositoryMock.findByCpf(anyString())).thenReturn(Optional.empty());
        when(crudPermissaoServiceMock.buscarPeloNome(any())).thenReturn(PermissaoCreator.createPermissao());
        when(pessoaRepositoryMock.saveAndFlush(any(Pessoa.class))).thenReturn(cliente);
        doNothing().when(emailServiceMock).sendEmailTemplate(anyString(), anyString(), anyMap());
        when(clienteMapperMock.toPessoaResponse(any(Pessoa.class))).thenReturn(pessoaResponse);
    }

    @Test
    @DisplayName("inserirCliente() inset client and Send email")
    void givenClienteRequest_whenInserirCliente_thenClienteShouldBeInserted() {
        // given
        ClienteRequest clienteRequest = ClienteCreator.createClienteRequest();
        // when
        PessoaResponse expected = underTest.inserirCliente(clienteRequest);
        // then
        verify(emailServiceMock, times(1)).sendEmailTemplate(anyString(), anyString(), anyMap());
        verify(pessoaRepositoryMock, times(1)).saveAndFlush(any(Pessoa.class));

        assertThat(expected).isEqualTo(pessoaResponse);
    }

    @Test
    @DisplayName("inserirCliente() Throws CpfAlreadyExistsException when cpf already registered")
    void givenRegisteredClienteRequest_whenInserirCliente_thenThrowsCpfAlreadyExistsException() {
        // given
        ClienteRequest clienteRequest = ClienteCreator.createClienteRequest();
        when(pessoaRepositoryMock.findByCpf(anyString())).thenReturn(Optional.of(cliente));
        // when
        assertThatThrownBy(() -> underTest.inserirCliente(clienteRequest))
                .isInstanceOf(CpfAlreadyExistsException.class)
                        .hasMessageContaining("á existe uma pessoa cadastrada com esse CPF");
        // then
        verify(emailServiceMock, never()).sendEmailTemplate(anyString(), anyString(), anyMap());
        verify(pessoaRepositoryMock, never()).saveAndFlush(any(Pessoa.class));
    }

    @Test
    @DisplayName("inserirCliente() Throws CreateTemplateException When an error occurs while sending an email")
    void givenClienteRequest_whenInserirCliente_thenThrowsCreateTemplateException() {
        // given
        ClienteRequest clienteRequest = ClienteCreator.createClienteRequest();
        doThrow(new CreateTemplateException("Erro ao tentar criar o template para o envio de email."))
                .when(emailServiceMock).sendEmailTemplate(anyString(), anyString(), anyMap());
        // when
        assertThatThrownBy(() -> underTest.inserirCliente(clienteRequest))
                .isInstanceOf(CreateTemplateException.class)
                .hasMessageContaining("Erro ao tentar criar o template para o envio de email.");
        // then
        verify(emailServiceMock, times(1))
                .sendEmailTemplate(anyString(), anyString(), anyMap());
        verify(pessoaRepositoryMock, times(1)).saveAndFlush(any(Pessoa.class));
    }
}