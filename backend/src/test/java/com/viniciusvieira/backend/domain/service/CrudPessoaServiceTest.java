package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.usuario.PessoaMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.domain.service.usuario.CrudPermissaoService;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import com.viniciusvieira.backend.util.CidadeCreator;
import com.viniciusvieira.backend.util.EstadoCreator;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Teste Unitário para a classe CrudPessoaService")
class CrudPessoaServiceTest {
    @InjectMocks
    private CrudPessoaService crudPessoaService;

    @Mock
    private PessoaRepository mockPessoaRepository;
    @Mock
    private PessoaMapper mockPessoaMapper;
    @Mock
    private CidadeRepository mockCidadeRepository;
    @Mock
    private EstadoRepository mockEstadoRepository;
    @Mock
    private CrudPermissaoService mockCrudPermissaoService;

    private final Pessoa validPessoa = PessoaCreator.mockPessoa();
    private final Pessoa validPessoaComCodigo = PessoaCreator.mockPessoaComCodigo();
    private final PessoaResponse expectedPessoaResponse = PessoaCreator.mockPessoaResponse();
    private final PessoaResponse expectedPessoaUpdated = PessoaCreator.mockPessoaResponseUpdate();
    private final List<Pessoa> expectedListPessoa = List.of(validPessoa);

    @BeforeEach
    void setUp() {

        // EstadoRepository - saveAndFlush
        BDDMockito.when(mockEstadoRepository.saveAndFlush(any(Estado.class))).thenReturn(EstadoCreator.mockEstado());
        // CidadeRepository - saveAndFlush
        BDDMockito.when(mockCidadeRepository.saveAndFlush(any(Cidade.class))).thenReturn(CidadeCreator.mockCidade());

        // PessoaRepository
        // findAll
        BDDMockito.when(mockPessoaRepository.findAll()).thenReturn(expectedListPessoa);
        // findById
        BDDMockito.when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.of(validPessoa));
        // saveAndFlush
        BDDMockito.when(mockPessoaRepository.saveAndFlush(any(Pessoa.class))).thenReturn(validPessoa);
        // delete
        BDDMockito.doNothing().when(mockPessoaRepository).delete(any(Pessoa.class));
        // findAllPessoasByCidadeId
        BDDMockito.when(mockPessoaRepository.findAllPessoasByCidadeId(anyLong())).thenReturn(expectedListPessoa);
        // findByCpf
        BDDMockito.when(mockPessoaRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        // findByEmail
        BDDMockito.when(mockPessoaRepository.findByEmail(anyString())).thenReturn(Optional.of(validPessoa));
        // findByEmailAndCodigoRecuperacaoSenha
        BDDMockito.when(mockPessoaRepository.findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString()))
                .thenReturn(Optional.of(validPessoaComCodigo));

        // PessoaMapper
        // toDomainPessoa
        BDDMockito.when(mockPessoaMapper.toDomainPessoa(any(PessoaRequest.class))).thenReturn(validPessoa);
        // toPessoaResponse
        BDDMockito.when(mockPessoaMapper.toPessoaResponse(any(Pessoa.class))).thenReturn(expectedPessoaResponse);

        // CrudPermissaoService
        // buscarPeloNome
        Permissao permissao = PermissaoCreator.mockPermissao();
        permissao.addNewPessoa(validPessoa);
        BDDMockito.when(mockCrudPermissaoService.buscarPeloNome(anyString())).thenReturn(permissao);

    }

    @Test
    @DisplayName("buscarTodos Return a list of Pessoa When successful")
    void buscarTodos_ReturnListPessoa_WhenSuccessful() {
        List<Pessoa> pessoas = crudPessoaService.buscarTodos();

        assertAll(
                () -> assertNotNull(pessoas),
                () -> assertFalse(pessoas.isEmpty()),
                () -> assertEquals(1, pessoas.size()),
                () -> assertTrue(pessoas.contains(validPessoa))
        );
    }

    @Test
    @DisplayName("buscarPeloId Return pessoa When successful")
    void buscarPeloId_ReturnPessoa_WhenSuccessful() {
        Pessoa pessoa = crudPessoaService.buscarPorId(1L);

        assertAll(
                () -> assertNotNull(pessoa),
                () -> assertEquals(validPessoa, pessoa)
        );
    }

    @Test
    @DisplayName("buscarPeloId Throws PessoaNaoEncontradaException When pessoa not found")
    void buscarPeloId_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound() {
        BDDMockito.when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PessoaNaoEncontradaException.class, () -> crudPessoaService.buscarPorId(99L));
    }

    @Test
    @DisplayName("buscarPeloEmail Return pessoa When successful")
    void buscarPeloEmail_ReturnPessoa_WhenSuccessful(){
        Pessoa pessoaEncontrada = crudPessoaService.buscarPeloEmail(validPessoa.getEmail());

        assertAll(
                () -> assertNotNull(pessoaEncontrada),
                () -> assertEquals(validPessoa, pessoaEncontrada)
        );
    }

    @Test
    @DisplayName("buscarPeloEmail Throws PessoaNaoEncontradaException When pessoa not found")
    void buscarPeloEmail_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound() {
        BDDMockito.when(mockPessoaRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        String email = validPessoa.getEmail();
        assertThrows(PessoaNaoEncontradaException.class, () -> crudPessoaService.buscarPeloEmail(email));
    }

    @Test
    @DisplayName("buscarPeloEmailECodigo Return pessoa When successful")
    void buscarPeloEmailECodigo_ReturnPessoa_WhenSuccessful(){
        String email = validPessoaComCodigo.getEmail();
        String codigo = validPessoaComCodigo.getCodigoRecuperacaoSenha();
        Pessoa pessoaEncontrada = crudPessoaService.buscarPeloEmailECodigo(email, codigo);

        assertAll(
                () -> assertNotNull(pessoaEncontrada),
                () -> assertEquals(validPessoaComCodigo, pessoaEncontrada)
        );
    }

    @Test
    @DisplayName("buscarPeloEmailECodigo Throws PessoaNaoEncontradaException When pessoa not found")
    void buscarPeloEmailECodigo_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound() {
        BDDMockito.when(mockPessoaRepository.findByEmailAndCodigoRecuperacaoSenha(anyString(), anyString()))
                .thenReturn(Optional.empty());

        String email = validPessoaComCodigo.getEmail();
        String codigo = validPessoaComCodigo.getCodigoRecuperacaoSenha();
        assertThrows(PessoaNaoEncontradaException.class, () -> crudPessoaService.buscarPeloEmailECodigo(email, codigo));
    }

    @Test
    @DisplayName("inserir Insert new pessoa When successful")
    void inserir_InsertNewPessoa_WhenSuccessful() {
        PessoaRequest pessoaParaInserir = PessoaCreator.mockPessoaRequestToSave();
        PessoaResponse pessoaInserida = crudPessoaService.inserir(pessoaParaInserir);

        assertAll(
                () -> assertNotNull(pessoaInserida),
                () -> assertEquals(expectedPessoaResponse.getNome(), pessoaInserida.getNome())
        );
    }

    @Test
    @DisplayName("Inserir Throws NegocioException When CPF is in use")
    void inserir_ThorwsNegocioException_WhenCpfInUse(){
        BDDMockito.when(mockPessoaRepository.findByCpf(anyString())).thenReturn(Optional.of(validPessoa));

        PessoaRequest pessoaParaInserir = PessoaCreator.mockPessoaRequestToSave();
        assertThrows(NegocioException.class, () -> crudPessoaService.inserir(pessoaParaInserir));
    }

    @Test
    @DisplayName("alterar Update pessoa when successful")
    void alterar_UpdatePessoa_WhenSuccessul() {
        Pessoa updatePessoa = PessoaCreator.mockPessoaToUpdate(validPessoa.getDataCriacao());
        BDDMockito.when(mockPessoaRepository.saveAndFlush(any(Pessoa.class))).thenReturn(updatePessoa);
        BDDMockito.when(mockPessoaMapper.toPessoaResponse(any(Pessoa.class))).thenReturn(expectedPessoaUpdated);

        PessoaRequest pessoaParaAlterar = PessoaCreator.mockPessoaRequestToUpdate();
        PessoaResponse pessoaAlterada = crudPessoaService.alterar(1L, pessoaParaAlterar);

        assertAll(
                () -> assertNotNull(pessoaAlterada),
                () -> assertEquals(expectedPessoaUpdated.getNome(), pessoaAlterada.getNome())
        );
    }

    @Test
    @DisplayName("alterar Throws PessoaNaoEncontradaException When pessoa not found")
    void alterar_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound() {
        BDDMockito.when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.empty());

        PessoaRequest pessoaParaAlterar = PessoaCreator.mockPessoaRequestToUpdate();

        assertThrows(PessoaNaoEncontradaException.class, () -> crudPessoaService.alterar(99L, pessoaParaAlterar));
    }

    @Test
    @DisplayName("excluir Remove pessoa when successful")
    void excluir_RemovePessoa_WhenSuccessful() {
        assertDoesNotThrow(() -> crudPessoaService.excluir(1L));
    }

    @Test
    @DisplayName("excluir Throws PessoaNaoEncontradaException When pessoa not found")
    void excluir_ThrowsPessoaNaoEncontradaException_WhenPessoaNotFound() {
        BDDMockito.when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PessoaNaoEncontradaException.class, () -> crudPessoaService.excluir(1L));
    }

    @Test
    @DisplayName("excluirTodasPessoasRelacionadasCidadeId When successful")
    void excluirTodasPessoasRelacionadasCidadeId_WhenSuccessful(){
        assertDoesNotThrow(() -> crudPessoaService.excluirTodasPessoasRelacionadasCidadeId(1L));
    }
}