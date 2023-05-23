package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.exception.EstadoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Estado;
import com.viniciusvieira.backend.domain.repository.EstadoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudEstadoService implements ICrud<Estado, Long> {
    private final EstadoRepository estadoRepository;

    @Override
    public List<Estado> buscarTodos(){
        return estadoRepository.findAll();
    }

    @Override
    public Estado buscarPeloId(Long id){
        return estadoRepository.findById(id)
                .orElseThrow(() -> new EstadoNaoEncontradoException("Estado não cadastrado"));
    }

    @Override
    @Transactional
    public Estado inserir(Estado estado){
        return estadoRepository.saveAndFlush(estado);
    }

    @Override
    @Transactional
    public Estado alterar(Long id, Estado estado){
        Estado estadoParaAtualizar = buscarPeloId(id);
        estadoParaAtualizar.setNome(estado.getNome());
        estadoParaAtualizar.setSigla(estado.getSigla());
        return estadoParaAtualizar;
    }

    @Override
    @Transactional
    public void excluir(Long id){
        Estado estado = buscarPeloId(id);
        estadoRepository.delete(estado);
    }
}
