package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.EstadoMapper;
import com.viniciusvieira.backend.api.representation.model.request.EstadoRequest;
import com.viniciusvieira.backend.api.representation.model.response.EstadoResponse;
import com.viniciusvieira.backend.domain.exception.EstadoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Estado;
import com.viniciusvieira.backend.domain.repository.EstadoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudEstadoService {
    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;

    public List<Estado> buscarTodos() {
        return estadoRepository.findAll();
    }

    public Estado buscarPeloId(Long id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new EstadoNaoEncontradoException("Estado não cadastrado"));
    }

    @Transactional
    public EstadoResponse inserir(EstadoRequest estadoRequest) {
        Estado estadoParaInserir = estadoMapper.toDomainEstado(estadoRequest);
        Estado estadoInserido = estadoRepository.saveAndFlush(estadoParaInserir);
        return estadoMapper.toEstadoResponse(estadoInserido);
    }

    @Transactional
    public EstadoResponse alterar(Long id, EstadoRequest estadoRequest) {
        Estado estadoEncontrado = buscarPeloId(id);
        Estado estadoParaAlterar = estadoMapper.toDomainEstado(estadoRequest);
        estadoParaAlterar.setId(estadoEncontrado.getId());
        estadoParaAlterar.setDataCriacao(estadoEncontrado.getDataCriacao());

        Estado estadoAlterado = estadoRepository.saveAndFlush(estadoParaAlterar);
        return estadoMapper.toEstadoResponse(estadoAlterado);
    }

    @Transactional
    public void excluir(Long id) {
        Estado estado = buscarPeloId(id);
        estadoRepository.delete(estado);
    }
}
