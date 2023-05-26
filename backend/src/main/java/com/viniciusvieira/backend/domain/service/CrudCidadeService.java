package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.CidadeMapper;
import com.viniciusvieira.backend.api.representation.model.request.CidadeRequest;
import com.viniciusvieira.backend.api.representation.model.response.CidadeResponse;
import com.viniciusvieira.backend.domain.exception.CidadeNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Cidade;
import com.viniciusvieira.backend.domain.repository.CidadeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudCidadeService {
    private final CidadeRepository cidadeRepository;
    private final CidadeMapper cidadeMapper;

    public List<Cidade> buscarTodos() {
        return cidadeRepository.findAll();
    }

    public Cidade buscarPeloId(Long id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new CidadeNaoEncontradaException("Cidade não cadastrada"));
    }

    @Transactional
    public CidadeResponse inserir(CidadeRequest cidadeRequest) {
        Cidade cidadeParaInserir = cidadeMapper.toDomainCidade(cidadeRequest);
        Cidade cidadeInserida = cidadeRepository.saveAndFlush(cidadeParaInserir);
        return cidadeMapper.toCidadeResponse(cidadeInserida);
    }

    @Transactional
    public CidadeResponse alterar(Long id, CidadeRequest cidadeRequest) {
        Cidade cidadeEncontrada = buscarPeloId(id);
        Cidade cidadeParaAlterar = cidadeMapper.toDomainCidade(cidadeRequest);
        cidadeParaAlterar.setId(cidadeEncontrada.getId());

        Cidade cidadeAlterada = cidadeRepository.saveAndFlush(cidadeParaAlterar);
        return cidadeMapper.toCidadeResponse(cidadeAlterada);
    }

    @Transactional
    public void excluir(Long id) {
        Cidade cidadeEncontrada = buscarPeloId(id);
        cidadeRepository.delete(cidadeEncontrada);
    }
}