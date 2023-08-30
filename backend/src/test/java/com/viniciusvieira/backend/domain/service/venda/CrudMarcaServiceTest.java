package com.viniciusvieira.backend.domain.service.venda;

import com.viniciusvieira.backend.api.mapper.venda.MarcaMapper;
import com.viniciusvieira.backend.api.representation.model.request.venda.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.MarcaResponse;
import com.viniciusvieira.backend.domain.exception.MarcaAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.MarcaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.venda.Marca;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import com.viniciusvieira.backend.util.MarcaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrudMarcaServiceTest {
    @InjectMocks
    private CrudMarcaService underTest;
    
    @Mock
    private MarcaRepository marcaRepositoryMock;
    @Mock
    private MarcaMapper marcaMapperMock;
    
    private final Marca marca = MarcaCreator.createMarca();
    private final MarcaResponse marcaResponse = MarcaCreator.createMarcaResponse();

    @Test
    @DisplayName("buscarTodos() return list marca")
    void whenBuscarTodos_thenReturnListMarca() {
        // given
        perfectPathConfig();
        // when
        List<Marca> expected = underTest.buscarTodos();
        // then
        verify(marcaRepositoryMock, times(1)).findAll();
        assertThat(expected)
                .hasSize(1)
                .contains(marca);
    }

    @Test
    @DisplayName("buscarPeloId() return marca")
    void givenId_WhenBuscarPeloId_thenMarcaShouldBeFound() {
        // given
        perfectPathConfig();
        // when
        Marca expected = underTest.buscarPeloId(marca.getId());
        // then
        verify(marcaRepositoryMock, times(1)).findById(anyLong());
        assertThat(expected)
                .isNotNull()
                .isEqualTo(marca);
    }

    @Test
    @DisplayName("buscarPeloId() throwns MarcaNaoEncontradaException when marca not found")
    void givenUnregisteredId_WhenBuscarPeloId_thenThrowsMarcaNaoEncontradaException() {
        // given
        long id = marca.getId();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.buscarPeloId(id))
                .isInstanceOf(MarcaNaoEncontradaException.class)
                .hasMessageContaining("Marca não cadastrada");
        // then
        verify(marcaRepositoryMock, times(1)).findById(anyLong());
    }


    @Test
    @DisplayName("inserir() insert marca")
    void givenMarcaRequest_whenInserir_thenMarcaShouldBeInserted() {
        // given
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        perfectPathConfig();
        lenient().when(marcaRepositoryMock.findByNome(anyString())).thenReturn(Optional.empty());
        // when
        MarcaResponse expected = underTest.inserir(marcaRequest);
        // then
        verify(marcaRepositoryMock, times(1)).saveAndFlush(any(Marca.class));
        assertThat(expected)
                .isNotNull()
                .isEqualTo(marcaResponse);
    }

    @Test
    @DisplayName("inserir() throws MarcaAlreadyExistsException when marca not found")
    void givenMarcaRequestWithAlrealyExistsNome_whenInserir_thenThrowsMarcaAlreadyExistsException() {
        // given
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        lenient().when(marcaRepositoryMock.findByNome(anyString())).thenReturn(Optional.of(marca));
        // when
        assertThatThrownBy(() -> underTest.inserir(marcaRequest))
                .isInstanceOf(MarcaAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma marca cadastrada com o NOME: " + marcaRequest.getNome());
        // then
        verify(marcaRepositoryMock, never()).saveAndFlush(any(Marca.class));
    }

    @Test
    @DisplayName("alterar() update marca")
    void givenIdAndMarcaRequest_whenAlterar_thenMarcaShouldBeUpdated() {
        // given
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        marcaRequest.setNome("Msi");
        perfectPathConfig();
        when(marcaRepositoryMock.findByNome(anyString())).thenReturn(Optional.empty());
        // when
        MarcaResponse expected = underTest.alterar(marca.getId(), marcaRequest);
        // then
        verify(marcaRepositoryMock, times(1)).saveAndFlush(any(Marca.class));
        assertThat(expected)
                .isNotNull()
                .isEqualTo(marcaResponse);
    }

    @Test
    @DisplayName("alterar() throws MarcaNaoEncontradaException when marca not found")
    void givenUnregisteredIdAndMarcaRequest_whenAlterar_thenThrowsMarcaNaoEncontradaException() {
        // given
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        Long id = marca.getId();
        failPathConfig();
        // when
        assertThatThrownBy(() -> underTest.alterar(id, marcaRequest))
                .isInstanceOf(MarcaNaoEncontradaException.class)
                .hasMessageContaining("Marca não cadastrada");
        // then
        verify(marcaRepositoryMock, never()).saveAndFlush(any(Marca.class));
    }

    @Test
    @DisplayName("alterar() throws MarcaAlreadyExistsException when marca already exists")
    void givenIdAndAlreadyExistsMarcaRequest_whenAlterar_thenThrowsMarcaAlreadyExistsException() {
        // given
        MarcaRequest marcaRequest = MarcaCreator.createMarcaRequest();
        Long id = marca.getId();
        when(marcaRepositoryMock.findById(anyLong())).thenReturn(Optional.of(marca));
        when(marcaRepositoryMock.findByNome(anyString())).thenReturn(Optional.of(marca));
        // when
        assertThatThrownBy(() -> underTest.alterar(id, marcaRequest))
                .isInstanceOf(MarcaAlreadyExistsException.class)
                .hasMessageContaining("Já existe uma marca cadastrada com o NOME: " + marcaRequest.getNome());
        // then
        verify(marcaRepositoryMock, never()).saveAndFlush(any(Marca.class));
    }

    @Test
    @DisplayName("excluir() remove marca")
    void givenId_whenExcluir_thenMarcaShouldBeRemoved() {
        // given
        perfectPathConfig();
        // when
        underTest.excluir(marca.getId());
        // then
        verify(marcaRepositoryMock, times(1)).delete(any(Marca.class));
    }

    @Test
    @DisplayName("excluir() throws MarcaNaoEncontradaException when marca not found")
    void givenUnregisteredId_whenExcluir_thenThrowsMarcaNaoEncontradaException() {
        // given
        failPathConfig();
        Long id = marca.getId();
        // when
        assertThatThrownBy(() -> underTest.excluir(id))
                .isInstanceOf(MarcaNaoEncontradaException.class)
                .hasMessageContaining("Marca não cadastrada");
        // then
        verify(marcaRepositoryMock, never()).delete(any(Marca.class));
    }

    private void perfectPathConfig(){
        lenient().when(marcaRepositoryMock.findAll()).thenReturn(List.of(marca));
        lenient().when(marcaRepositoryMock.findById(anyLong())).thenReturn(Optional.of(marca));
        lenient().when(marcaRepositoryMock.findByNome(anyString())).thenReturn(Optional.of(marca));
        lenient().when(marcaMapperMock.toDomainMarca(any(MarcaRequest.class))).thenReturn(marca);
        lenient().when(marcaRepositoryMock.saveAndFlush(any(Marca.class))).thenReturn(marca);
        lenient().when(marcaMapperMock.toMarcaResponse(any(Marca.class)))
                .thenReturn(marcaResponse);
        lenient().doNothing().when(marcaRepositoryMock).delete(any(Marca.class));
    }

    private void failPathConfig(){
        lenient().when(marcaRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        lenient().when(marcaRepositoryMock.findByNome(anyString())).thenReturn(Optional.empty());
    }
    

}