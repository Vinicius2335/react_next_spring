package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.usuario.CpfAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.usuario.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import com.viniciusvieira.backend.util.PermissaoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaControllerTest {
    @InjectMocks
    private PessoaController underTest;

    @Mock
    private CrudPessoaService crudPessoaServiceMock;

    private final Pessoa pessoa = PessoaCreator.createPessoa();
    private final Permissao permissao = PermissaoCreator.createPermissao();
    private PessoaResponse pessoaResponse;

    @BeforeEach
    void setUp() {
        pessoaResponse = PessoaCreator.createPessoaResponse(pessoa);
    }

    @Test
    @DisplayName("buscarTodos() return list pessoas")
    void whenBuscarTodos_thenReturnListPessoas() {
        // given
        given(crudPessoaServiceMock.buscarTodos()).willReturn(List.of(pessoa));
        // when
        ResponseEntity<List<Pessoa>> expected = underTest.buscarTodos();
        // then
        verify(crudPessoaServiceMock, times(1)).buscarTodos();
        assertThat(expected.getBody())
                .isNotNull()
                .hasSize(1)
                .contains(pessoa);
    }

    @Test
    @DisplayName("buscarPermissoes() return list permissoes related to pessoa")
    void giveId_whenBuscarPermissoes_thenReturnListPermissoes(){
        // given
        given(crudPessoaServiceMock.buscarPermissoes(anyLong())).willReturn(List.of(permissao));
        // when
        ResponseEntity<List<Permissao>> expected = underTest.buscarPermissoes(1L);
        // then
        verify(crudPessoaServiceMock, times(1)).buscarPermissoes(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(permissao);
    }

    @Test
    @DisplayName("buscarPermissoes() throws PessoaNaoEncontradaException when pessoa not found by id")
    void giveUnregisteredId_whenBuscarPermissoes_thenThrowsPessoaNaoEncontradaException(){
        // given
        String errorMenssage = "Não existe nenhuma pessoa cadastrada com este ID";
        doThrow(new PessoaNaoEncontradaException(errorMenssage)).when(crudPessoaServiceMock).buscarPermissoes(anyLong());
        // when
        assertThatThrownBy(() -> underTest.buscarPermissoes(1L))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining(errorMenssage);
        // then
        verify(crudPessoaServiceMock, times(1)).buscarPermissoes(anyLong());
    }

    @Test
    @DisplayName("inserir() insert pessoa")
    void givenPessoaRequest_whenInserir_thenReturnNewPessoaInserted() {
        // given
        given(crudPessoaServiceMock.inserir(any(PessoaRequest.class))).willReturn(pessoaResponse);
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        // when
        ResponseEntity<PessoaResponse> expected = underTest.inserir(pessoaRequest);
        // then
        verify(crudPessoaServiceMock, times(1)).inserir(any(PessoaRequest.class));
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(pessoaResponse);
    }
    @Test
    @DisplayName("inserir() throws CpfAlreadExistsException when CPF is already registered")
    void givenPessoaRequestAlreadyRegistered_whenInserir_thenThrowsCpfAlreadyExistsExeption() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        when(crudPessoaServiceMock.inserir(pessoaRequest)).thenThrow(new CpfAlreadyExistsException("Já existe uma pessoa cadastrada com esse CPF"));
        // when
        assertThatThrownBy(() -> underTest.inserir(pessoaRequest))
                .isInstanceOf(CpfAlreadyExistsException.class)
                        .hasMessageContaining("Já existe uma pessoa cadastrada com esse CPF");
        // then
        verify(crudPessoaServiceMock, times(1)).inserir(any(PessoaRequest.class));
    }

    @Test
    @DisplayName("alterar() updated pessoa")
    void givenIdAndPessoaRequest_whenAlterar_thenReturnPessoaUpdated() {
        // given
        given(crudPessoaServiceMock.alterar(anyLong(), any(PessoaRequest.class))).willReturn(pessoaResponse);
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        // when
        ResponseEntity<PessoaResponse> expected = underTest.alterar(1L, pessoaRequest);
        // then
        verify(crudPessoaServiceMock, times(1)).alterar(anyLong(), any(PessoaRequest.class));
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(pessoaResponse);
    }

    @Test
    @DisplayName("alterar() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisterdIdAndPessoaRequest_whenAlterar_thenThrowsPessoaNaoEncontradaException() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        when(crudPessoaServiceMock.alterar(anyLong(), any(PessoaRequest.class)))
                .thenThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este ID"));
        // when
        assertThatThrownBy(() -> underTest.alterar(99L, pessoaRequest))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(crudPessoaServiceMock, times(1)).alterar(anyLong(), any(PessoaRequest.class));
    }

    @Test
    @DisplayName("excluirPermissao() removed permissao")
    void givenIdPessoaAndIdPermissao_whenExcluirPermissao_thenPessoaShouldHavePermissaoRemoved() {
        // given
        doNothing().when(crudPessoaServiceMock).excluirPermissao(anyLong(), anyLong());
        // when
        ResponseEntity<Void> expected = underTest.excluirPermissao(1L, 1L);
        // then
        verify(crudPessoaServiceMock, times(1)).excluirPermissao(anyLong(), anyLong());
        assertThat(expected.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("excluirPermissao() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredIdPessoaAndIdPermissao_whenExcluirPermissao_thenThrowsPessoaNaoEncontradaException() {
        // given
        doThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este ID"))
                .when(crudPessoaServiceMock).excluirPermissao(anyLong(), anyLong());
        // when
        assertThatThrownBy(() -> underTest.excluirPermissao(99L, 1L))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(crudPessoaServiceMock, times(1)).excluirPermissao(anyLong(), anyLong());
    }

    @Test
    @DisplayName("excluir() removed pessoa")
    void givenIdPessoa_whenExcluir_thenPessoaShouldBeRemoved(){
        // given
        doNothing().when(crudPessoaServiceMock).excluir(anyLong());
        // when
        ResponseEntity<Void> expected = underTest.excluir(1L);
        // then
        verify(crudPessoaServiceMock, times(1)).excluir(anyLong());
        assertThat(expected.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("excluir() throws PessoaNaoEncontradaException when pessoa not found")
    void givenUnregisteredIdPessoa_whenExcluir_thenThrowsPessoaNaoEncontradaException(){
        // given
        doThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este ID"))
                .when(crudPessoaServiceMock).excluir(anyLong());
        // when
        assertThatThrownBy(() -> underTest.excluir(99L))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(crudPessoaServiceMock, times(1)).excluir(anyLong());
    }
}