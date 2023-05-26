package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.MarcaMapper;
import com.viniciusvieira.backend.api.representation.model.request.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.MarcaResponse;
import com.viniciusvieira.backend.domain.exception.MarcaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Marca;
import com.viniciusvieira.backend.domain.repository.MarcaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudMarcaService  {
    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public List<Marca> buscarTodos() {
        return marcaRepository.findAll();
    }

    public Marca buscarPeloId(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new MarcaNaoEncontradaException("Marca não cadastrada"));
    }

    @Transactional
    public MarcaResponse inserir(MarcaRequest marcaRequest) {
        Marca marcaParaInserir = marcaMapper.toDomainMarca(marcaRequest);
        Marca marcaInserida = marcaRepository.saveAndFlush(marcaParaInserir);
        return marcaMapper.toMarcaResponse(marcaInserida);
    }

    @Transactional
    public MarcaResponse alterar(Long id, MarcaRequest marcaRequest) {
        Marca marcaEncontrada = buscarPeloId(id);
        Marca marcaParaAlterar = marcaMapper.toDomainMarca(marcaRequest);
        marcaParaAlterar.setId(marcaEncontrada.getId());

        Marca marcaAlterada = marcaRepository.saveAndFlush(marcaParaAlterar);
        return marcaMapper.toMarcaResponse(marcaAlterada);
    }

    @Transactional
    public void excluir(Long id) {
        Marca marcaParaExcluir = buscarPeloId(id);
        marcaRepository.delete(marcaParaExcluir);
    }
}
