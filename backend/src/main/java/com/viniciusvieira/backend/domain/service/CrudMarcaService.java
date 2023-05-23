package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.exception.MarcaNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Marca;
import com.viniciusvieira.backend.domain.repository.MarcaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudMarcaService implements ICrud<Marca, Long> {
    private final MarcaRepository marcaRepository;

    @Override
    public List<Marca> buscarTodos() {
        return marcaRepository.findAll();
    }

    @Override
    public Marca buscarPeloId(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new MarcaNaoEncontradoException("Marca não cadastrada"));
    }

    @Override
    @Transactional
    public Marca inserir(Marca marca) {
        return marcaRepository.saveAndFlush(marca);
    }

    @Override
    @Transactional
    public Marca alterar(Long id, Marca marca) {
        Marca marcaParaAtualizar = buscarPeloId(id);
        marcaParaAtualizar.setNome(marca.getNome());
        return marcaParaAtualizar;
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Marca marcaParaExcluir = buscarPeloId(id);
        marcaRepository.delete(marcaParaExcluir);
    }
}
