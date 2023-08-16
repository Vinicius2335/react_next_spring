package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaGerenciamentoRequest;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import com.viniciusvieira.backend.domain.service.usuario.PessoaGerenciamentoService;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste Unitário para PessoaGerenciamentoService")
class PessoaGerenciamentoServiceTest {
    @InjectMocks
    private PessoaGerenciamentoService pessoaGerenciamentoService;

    @Mock
    private CrudPessoaService mockCrudPessoaService;
    @Mock
    private EmailService mockEmailService;

    private final Pessoa validPessoa = PessoaCreator.mockPessoa();
    private final Pessoa validPessoaComCodigo = PessoaCreator.mockPessoaComCodigo();

    @BeforeEach
    void setUp(){
        // CrudPessoaService
        // buscarPeloEmail
        BDDMockito.when(mockCrudPessoaService.buscarPeloEmail(anyString())).thenReturn(validPessoa);
        // alterarParaGerenciamento
        BDDMockito.doNothing().when(mockCrudPessoaService).alterarParaGerenciamento(any(Pessoa.class));
        // buscarPeloEmailECodigo
        BDDMockito.when(mockCrudPessoaService.buscarPeloEmailECodigo(anyString(), anyString())).thenReturn(validPessoaComCodigo);

        // EmailService
        // sendEmailSimples
        BDDMockito.doNothing().when(mockEmailService).sendEmailSimples(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("solicitarCodigo Update and send email to pessoa when successful")
    void solicitarCodigo_UpdateAndSendEmailPessoa_WhenSuccessful(){
        assertDoesNotThrow(() -> pessoaGerenciamentoService.solicitarCodigo(validPessoa.getEmail()));
    }

    @Test
    @DisplayName("solicitarCodigo Throws PessoaNaoEncontradaException when pessoa not found by email")
    void solicitarCodigo_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound(){
        BDDMockito.when(mockCrudPessoaService.buscarPeloEmail(anyString())).thenThrow(PessoaNaoEncontradaException.class);

        String email = validPessoa.getEmail();
        assertThrows(PessoaNaoEncontradaException.class, () -> pessoaGerenciamentoService.solicitarCodigo(email));
    }

    @Test
    @DisplayName("alterarSenha Update and send email to pessoa When successful")
    void alterarSenha_UpdateAndSendEmailPessoa_WhenSuccessful(){
        PessoaGerenciamentoRequest pessoaGerenciamentoRequest = PessoaCreator.mockPessoaGerenciamentoRequest();
        assertDoesNotThrow(() -> pessoaGerenciamentoService.alterarSenha(pessoaGerenciamentoRequest));
    }

    @Test
    @DisplayName("alterarSenha Throws PessoaNaoEncontradaException when pessoa not found by email")
    void alterarSenha_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound(){
        BDDMockito.when(mockCrudPessoaService.buscarPeloEmailECodigo(anyString(), anyString())).thenThrow(PessoaNaoEncontradaException.class);

        PessoaGerenciamentoRequest pessoaGerenciamentoRequest = PessoaCreator.mockPessoaGerenciamentoRequest();
        assertThrows(PessoaNaoEncontradaException.class, () -> pessoaGerenciamentoService.alterarSenha(pessoaGerenciamentoRequest));
    }

    @Test
    @DisplayName("alterarSenha Throws NegocioException when codigo expirado")
    void alterarSenha_ThrowsNegocioException_WhenCodigoExpirado(){
        Pessoa pessoaComDataExpirada = PessoaCreator.mockPessoaComCodigo();

        Date data = new Date();
        data.setMinutes(data.getMinutes() + 17);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(data.toInstant(), ZoneId.systemDefault());

        pessoaComDataExpirada.setDataEnvioCodigo(localDateTime);
        BDDMockito.when(mockCrudPessoaService.buscarPeloEmailECodigo(anyString(), anyString())).thenReturn(pessoaComDataExpirada);

        PessoaGerenciamentoRequest pessoaGerenciamentoRequest = PessoaCreator.mockPessoaGerenciamentoRequest();
        assertThrows(NegocioException.class, () -> pessoaGerenciamentoService.alterarSenha(pessoaGerenciamentoRequest));
    }
}