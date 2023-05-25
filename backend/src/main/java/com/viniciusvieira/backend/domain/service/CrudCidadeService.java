package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.exception.CidadeNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Cidade;
import com.viniciusvieira.backend.domain.repository.CidadeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudCidadeService implements ICrud<Cidade, Long> {
    private final CidadeRepository cidadeRepository;

    @Override
    public List<Cidade> buscarTodos() {
        return cidadeRepository.findAll();
    }

    @Override
    public Cidade buscarPeloId(Long id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new CidadeNaoEncontradaException("Cidade não cadastrada"));
    }

    @Override
    @Transactional
    public Cidade inserir(Cidade cidade) {
        return cidadeRepository.saveAndFlush(cidade);
    }

    @Override
    @Transactional
    public Cidade alterar(Long id, Cidade cidade) {
        Cidade cidadeParaAlterar = buscarPeloId(id);
        cidadeParaAlterar.setNome(cidade.getNome());
        cidadeParaAlterar.setEstado(cidade.getEstado());
        return cidadeRepository.saveAndFlush(cidadeParaAlterar);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Cidade cidadeEncontrada = buscarPeloId(id);
        cidadeRepository.delete(cidadeEncontrada);
    }
}
