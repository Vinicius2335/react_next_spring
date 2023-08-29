package com.viniciusvieira.backend.api.controller.usuario;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.domain.exception.CpfAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import com.viniciusvieira.backend.util.PessoaCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaControllerTest {
    @InjectMocks
    private PessoaController underTest;

    @Mock
    private CrudPessoaService mockCrudPessoaService;

    @Test
    void whenBuscarTodos_thenReturnListPessoas() {
        // given
        // when
        underTest.buscarTodos();
        // then
        verify(mockCrudPessoaService, times(1)).buscarTodos();
    }

    @Test
    void givenPessoaRequest_whenInserir_thenReturnNewPessoaInserted() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        // when
        underTest.inserir(pessoaRequest);
        // then
        verify(mockCrudPessoaService, times(1)).inserir(any(PessoaRequest.class));
    }
    @Test
    void givenPessoaRequestWithCpfRegistered_whenInserir_thenThrowsCpfAlreadyExistsExeption() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        when(mockCrudPessoaService.inserir(pessoaRequest)).thenThrow(new CpfAlreadyExistsException("Já existe uma pessoa cadastrada com esse CPF"));
        // when
        assertThatThrownBy(() -> underTest.inserir(pessoaRequest))
                .isInstanceOf(CpfAlreadyExistsException.class)
                        .hasMessageContaining("Já existe uma pessoa cadastrada com esse CPF");
        // then
        verify(mockCrudPessoaService, times(1)).inserir(any(PessoaRequest.class));
    }

    @Test
    void givenIdAndPessoaRequest_whenAlterar_thenReturnPessoaUpdated() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        // when
        underTest.alterar(1L, pessoaRequest);
        // then
        verify(mockCrudPessoaService, times(1)).alterar(anyLong(), any(PessoaRequest.class));
    }

    @Test
    void givenUnregisterdIdAndPessoaRequest_whenAlterar_thenThrowsPessoaNaoEncontradaException() {
        // given
        PessoaRequest pessoaRequest = PessoaCreator.createPessoaRequest();
        when(mockCrudPessoaService.alterar(anyLong(), any(PessoaRequest.class)))
                .thenThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este ID"));
        // when
        assertThatThrownBy(() -> underTest.alterar(99L, pessoaRequest))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(mockCrudPessoaService, times(1)).alterar(anyLong(), any(PessoaRequest.class));
    }

    @Test
    void givenIdPessoaAndIdPermissao_whenExcluirPermissao_thenPessoaShouldHavePermissaoRemoved() {
        // given
        // when
        underTest.excluirPermissao(1L, 1L);
        // then
        verify(mockCrudPessoaService, times(1)).excluirPermissao(anyLong(), anyLong());
    }

    @Test
    void givenUnregisteredIdPessoaAndIdPermissao_whenExcluirPermissao_thenThrowsPessoaNaoEncontradaException() {
        // given
        doThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este ID"))
                .when(mockCrudPessoaService).excluirPermissao(anyLong(), anyLong());
        // when
        assertThatThrownBy(() -> underTest.excluirPermissao(99L, 1L))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(mockCrudPessoaService, times(1)).excluirPermissao(anyLong(), anyLong());
    }

    @Test
    void givenIdPessoa_whenExcluir_thenPessoaShouldBeRemoved(){
        // given
        // when
        underTest.excluir(1L);
        // then
        verify(mockCrudPessoaService, times(1)).excluir(anyLong());
    }

    @Test
    void givenUnregisteredIdPessoa_whenExcluir_thenThrowsPessoaNaoEncontradaException(){
        // given
        doThrow(new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este ID"))
                .when(mockCrudPessoaService).excluir(anyLong());
        // when
        assertThatThrownBy(() -> underTest.excluir(99L))
                .isInstanceOf(PessoaNaoEncontradaException.class)
                .hasMessageContaining("Não existe nenhuma pessoa cadastrada com este ID");
        // then
        verify(mockCrudPessoaService, times(1)).excluir(anyLong());
    }
}