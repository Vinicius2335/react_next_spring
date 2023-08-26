package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.usuario.ClienteMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.ClienteRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.domain.service.usuario.CrudPermissaoService;
import com.viniciusvieira.backend.domain.service.usuario.SalvarClienteService;
import com.viniciusvieira.backend.util.ClienteCreator;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@Log4j2
@DisplayName("Teste Unitário para SalvarClienteService")
class SalvarClienteServiceTest {
    @InjectMocks
    private SalvarClienteService salvarClienteService;

    @Mock
    private PessoaRepository mockPessoaRepository;
    @Mock
    private ClienteMapper mockClienteMapper;
    @Mock
    private CrudPermissaoService mockCrudPermissaoService;
    @Mock
    private EmailService mockEmailService;

    private final Pessoa validPessoa = PessoaCreator.mockPessoa();

    @BeforeEach
    void setUp(){
        // PessoaRepository
        // saveAndFlush
        BDDMockito.when(mockPessoaRepository.saveAndFlush(any(Pessoa.class)))
                .thenReturn(PessoaCreator.mockPessoa());
        // findByCpf
        BDDMockito.when(mockPessoaRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        // ClienteMapper
        // toDomainPessoa
        BDDMockito.when(mockClienteMapper.toDomainPessoa(any(ClienteRequest.class)))
                .thenReturn(PessoaCreator.mockPessoa());
        // toPessoaResponse
        BDDMockito.when(mockClienteMapper.toPessoaResponse(any(Pessoa.class)))
                        .thenReturn(ClienteCreator.mockClientePessoaResponse());

        // CrudPermissaoService
        // buscarPeloNome
        Permissao permissao = PermissaoCreator.mockPermissao();
        permissao.addNewPessoa(validPessoa);
        BDDMockito.when(mockCrudPermissaoService.buscarPeloNome(anyString()))
                .thenReturn(permissao);

        // EmailService
        // sendEmailTemplate
        BDDMockito.doNothing().when(mockEmailService).sendEmailTemplate(anyString(), anyString(), anyMap());

    }

    @Test
    @DisplayName("inserirCliente Save new pessoa cliente when successful")
    void inserirCliente_SaveNewPessoa_WhenSuccessful(){
        ClienteRequest clienteParaSalvar = ClienteCreator.mockClienteRequest();
        PessoaResponse clienteSalvo = salvarClienteService.inserirCliente(clienteParaSalvar);

        assertAll(
                () -> assertNotNull(clienteSalvo),
                () -> assertEquals(clienteParaSalvar.getNome(), clienteSalvo.getNome()),
                () -> assertEquals(clienteParaSalvar.getCpf(), clienteSalvo.getCpf())
        );
    }

    @Test
    @DisplayName("inserirCliente Throws NegocioException when cpf in use")
    void inserirCliente_ThrowsNegocioException_WhenCpfInUse(){
        ClienteRequest clienteParaSalvar = ClienteCreator.mockClienteRequest();
        BDDMockito.when(mockPessoaRepository.findByCpf(anyString())).thenReturn(Optional.of(PessoaCreator.mockPessoa()));

        assertThrows(NegocioException.class, () -> salvarClienteService.inserirCliente(clienteParaSalvar));
    }

}