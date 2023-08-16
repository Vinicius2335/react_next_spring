package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.controller.usuario.ClienteController;
import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.service.usuario.SalvarClienteService;
import com.viniciusvieira.backend.util.ClienteCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@Log4j2
@DisplayName("Teste Unitárip para ClienteController")
class ClienteControllerTest {
    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private SalvarClienteService mockSalvarClienteService;

    @BeforeEach
    void setUp(){
        // inserirCliente
        BDDMockito.when(mockSalvarClienteService.inserirCliente(any(ClienteRequest.class)))
                .thenReturn(ClienteCreator.mockClientePessoaResponse());
    }

    @Test
    @DisplayName("inserir Save a cliente When successful")
    void inserir_SaveCliente_WhenSuccessful(){
        ClienteRequest clienteParaSalvar = ClienteCreator.mockClienteRequest();
        ResponseEntity<PessoaResponse> response = clienteController.inserir(clienteParaSalvar);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(clienteParaSalvar.getCpf(), response.getBody().getCpf())
        );
    }

    @Test
    @DisplayName("inserir Throws NegocioExeception When cpf is in use")
    void inserir_ThrowsNegocioException_WhenCpfIsInUse(){
        BDDMockito.when(mockSalvarClienteService.inserirCliente(any(ClienteRequest.class)))
                .thenThrow(NegocioException.class);
        ClienteRequest clienteParaSalvar = ClienteCreator.mockClienteRequest();

        assertThrows(NegocioException.class, () -> clienteController.inserir(clienteParaSalvar));
    }
}