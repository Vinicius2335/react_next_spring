package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.usuario.CpfAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.CreateTemplateException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.usuario.SalvarClienteService;
import com.viniciusvieira.backend.util.ClienteCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ClienteControllerTest {
    @InjectMocks
    private ClienteController underTest;

    @Mock
    private SalvarClienteService salvarClienteServiceMock;

    private final Pessoa cliente = PessoaCreator.createPessoa();
    private PessoaResponse pessoaResponse;

    @BeforeEach
    void setUp() {
        pessoaResponse = PessoaCreator.createPessoaResponse(cliente);
    }

    @Test
    @DisplayName("inserir() insert cliente")
    void givenClienteRequest_whenInserir_thenClienteShoulBeInserted() {
        // given
        ClienteRequest clienteRequest = ClienteCreator.createClienteRequest();
        given(salvarClienteServiceMock.inserirCliente(any(ClienteRequest.class)))
                .willReturn(pessoaResponse);
        // when
        ResponseEntity<PessoaResponse> expected = underTest.inserir(clienteRequest);
        // then
        verify(salvarClienteServiceMock, times(1)).inserirCliente(any(ClienteRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(pessoaResponse);
    }

    @Test
    @DisplayName("inserir() Throws CpfAlreadyExistsException when cpf already exists")
    void givenRegisteredClienteRequest_whenInserir_thenThrowsCpfAlreadyExistsException() {
        // given
        ClienteRequest clienteRequest = ClienteCreator.createClienteRequest();
        doThrow(new CpfAlreadyExistsException("Já existe uma pessoa cadastrada com esse CPF"))
                .when(salvarClienteServiceMock).inserirCliente(any(ClienteRequest.class));
        // when
        assertThatThrownBy(() -> underTest.inserir(clienteRequest))
                .isInstanceOf(CpfAlreadyExistsException.class)
                        .hasMessageContaining("Já existe uma pessoa cadastrada com esse CPF");
        // then
        verify(salvarClienteServiceMock, times(1)).inserirCliente(any(ClienteRequest.class));
    }

    @Test
    @DisplayName("inserir() Throws CreateTemplateException when erro while try sending email")
    void givenClienteRequest_whenInserir_thenThrowsCreateTemplateException() {
        // given
        ClienteRequest clienteRequest = ClienteCreator.createClienteRequest();
        doThrow(new CreateTemplateException("Erro ao tentar criar o template para o envio de email."))
                .when(salvarClienteServiceMock).inserirCliente(any(ClienteRequest.class));
        // when
        assertThatThrownBy(() -> underTest.inserir(clienteRequest))
                .isInstanceOf(CreateTemplateException.class)
                .hasMessageContaining("Erro ao tentar criar o template para o envio de email.");
        // then
        verify(salvarClienteServiceMock, times(1)).inserirCliente(any(ClienteRequest.class));
    }
}