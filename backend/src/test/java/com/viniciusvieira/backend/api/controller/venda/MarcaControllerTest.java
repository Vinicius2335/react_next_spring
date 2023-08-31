package com.viniciusvieira.backend.api.controller.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.MarcaResponse;
import com.viniciusvieira.backend.domain.exception.venda.MarcaAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.venda.MarcaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.venda.Marca;
import com.viniciusvieira.backend.domain.service.venda.CrudMarcaService;
import com.viniciusvieira.backend.util.MarcaCreator;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MarcaControllerTest {
    @InjectMocks
    private MarcaController underTest;
    
    @Mock
    private CrudMarcaService crudMarcaServiceMock;
    
    private final Marca marca = MarcaCreator.createMarca();
    private final MarcaResponse marcaResponse = MarcaCreator.createMarcaResponse();

    @Test
    @DisplayName("buscarTodos() return list marca")
    void whenBuscarTodos_thenReturnListMarca() {
        // given
        given(crudMarcaServiceMock.buscarTodos()).willReturn(List.of(marca));
        // when
        ResponseEntity<List<Marca>> expected = underTest.buscarTodos();
        // then
        verify(crudMarcaServiceMock, times(1)).buscarTodos();
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .hasSize(1)
                .contains(marca);
    }

    @Test
    @DisplayName("buscarPeloId() return marca")
    void givenId_whenBuscarPeloId_thenMarcaShouldBeFound() {
        // given
        given(crudMarcaServiceMock.buscarPeloId(anyLong())).willReturn(marca);
        // when
        ResponseEntity<Marca> expected = underTest.buscarPeloId(marca.getId());
        // then
        verify(crudMarcaServiceMock, times(1)).buscarPeloId(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(marca);
    }

    @Test
    @DisplayName("buscarPeloId() Throws MarcaNaoEncontradException when marca not found")
    void givenUnregisteredId_whenBuscarPeloId_thenMarcaNotFoundException() {
        // given
        doThrow(new MarcaNaoEncontradaException("Marca não cadastrada"))
                .when(crudMarcaServiceMock).buscarPeloId(anyLong());
        Long id = marca.getId();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloId(id))
                .isInstanceOf(MarcaNaoEncontradaException.class)
                .hasMessageContaining("Marca não cadastrada");
        // then
        verify(crudMarcaServiceMock, times(1)).buscarPeloId(anyLong());
    }

    @Test
    @DisplayName("inserir() save marca and return marcaResponse")
    void givenMarcaRequest_whenInserir_thenMarcaShouldBeInserted() {
        // given
        given(crudMarcaServiceMock.inserir(any(MarcaRequest.class))).willReturn(marcaResponse);
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        // when
        ResponseEntity<MarcaResponse> expected = underTest.inserir(marcaRequest);
        // then
        verify(crudMarcaServiceMock, times(1)).inserir(any(MarcaRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(marcaResponse);
    }

    @Test
    @DisplayName("inserir() throws MarcaAlreadyExistsException when marca already exists")
    void givenMarcaRequestAlreadyRegistered_whenInserir_thenThrowsMarcaAlreadyExistsException() {
        // given
        doThrow(new MarcaAlreadyExistsException("Já existe uma marca cadastrada com esse NOME: " +marca.getNome())).when(crudMarcaServiceMock).inserir(any(MarcaRequest.class));
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        // when
        assertThatThrownBy(() -> underTest.inserir(marcaRequest))
                .isInstanceOf(MarcaAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma marca cadastrada com esse NOME: " +marcaRequest.getNome());
        // then
        verify(crudMarcaServiceMock, times(1)).inserir(any(MarcaRequest.class));
    }

    @Test
    @DisplayName("alterar() updated marca")
    void givenIdAndMarcaRequest_whenAlterar_thenMarcaShouldBeUpdated() {
        // given
        given(crudMarcaServiceMock.alterar(anyLong(), any(MarcaRequest.class)))
                .willReturn(marcaResponse);
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        // when
        ResponseEntity<MarcaResponse> expected = underTest.alterar(marca.getId(), marcaRequest);
        // then
        verify(crudMarcaServiceMock, times(1))
                .alterar(anyLong(), any(MarcaRequest.class));
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(expected.getBody())
                .isNotNull()
                .isEqualTo(marcaResponse);
    }

    @Test
    @DisplayName("alterar() Throws MarcaNaoEncontradException when marca not found")
    void givenUnregisteredIdAndMarcaRequest_whenAlterar_thenThrowsMarcaNaoEncontradaExceotion() {
        // given
        doThrow(new MarcaNaoEncontradaException("Marca não cadastrada"))
                .when(crudMarcaServiceMock).alterar(anyLong(), any(MarcaRequest.class));
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        Long id = marca.getId();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, marcaRequest))
                .isInstanceOf(MarcaNaoEncontradaException.class)
                .hasMessageContaining("Marca não cadastrada");
        // then
        verify(crudMarcaServiceMock, times(1)).alterar(anyLong(), any(MarcaRequest.class));
    }

    @Test
    @DisplayName("alterar() Throws MarcaAlreadyExistsException when marca already exists")
    void givenIdAndAlreadyExistsMarcaRequest_whenAlterar_thenThrowsMarcaAlreadyExistsException() {
        // given
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        doThrow(new MarcaAlreadyExistsException("Já existe uma marca cadastrada com esse NOME: "+marcaRequest.getNome()))
                .when(crudMarcaServiceMock).alterar(anyLong(), any(MarcaRequest.class));
        Long id = marca.getId();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, marcaRequest))
                .isInstanceOf(MarcaAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma marca cadastrada com esse NOME: "+marcaRequest.getNome());
        // then
        verify(crudMarcaServiceMock, times(1)).alterar(anyLong(), any(MarcaRequest.class));
    }

    @Test
    @DisplayName("excluir() removed marca")
    void givenId_whenExcluir_thenMarcaShouldBeRemoved() {
        // given
        doNothing().when(crudMarcaServiceMock).excluir(anyLong());
        // when
        ResponseEntity<Void> expected = underTest.excluir(marca.getId());
        // then
        verify(crudMarcaServiceMock, times(1)).excluir(anyLong());
        assertThat(expected.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("excluir() throws MarcaNaoEncotradaException when marcar not found")
    void givenUnregisteredId_whenExcluir_thenThrowsMarcaNaoEncontradaExceotion() {
        // given
        doThrow(new MarcaNaoEncontradaException("Marca não cadastrada"))
                .when(crudMarcaServiceMock).excluir(anyLong());
        Long id = marca.getId();
        // when
        assertThatThrownBy(() -> underTest.excluir(id))
                .isInstanceOf(MarcaNaoEncontradaException.class)
                .hasMessageContaining("Marca não cadastrada");
        // then
        verify(crudMarcaServiceMock, times(1)).excluir(anyLong());
    }
}