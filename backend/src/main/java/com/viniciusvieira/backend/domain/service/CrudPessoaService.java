package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.PessoaMapper;
import com.viniciusvieira.backend.api.representation.model.request.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Pessoa;
import com.viniciusvieira.backend.domain.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudPessoaService {
    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;

    public List<Pessoa> buscarTodos() {
        return pessoaRepository.findAll();
    }

    public Pessoa buscarPorId(Long id){
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException("Pessoa não encontrada"));
    }

    @Transactional
    public PessoaResponse inserir(PessoaRequest pessoaRequest) {
        Pessoa pessoaParaSalvar = pessoaMapper.toDomainPessoa(pessoaRequest);
        Pessoa pessoaSalva = pessoaRepository.saveAndFlush(pessoaParaSalvar);

        return pessoaMapper.toPessoaResponse(pessoaSalva);
    }

    @Transactional
    public PessoaResponse alterar(Long id, PessoaRequest pessoaRequest) {
        Pessoa pessoaEncontrada = buscarPorId(id);
        Pessoa pessoaParaAlterar = pessoaMapper.toDomainPessoa(pessoaRequest);
        pessoaParaAlterar.setId(pessoaEncontrada.getId());
        pessoaParaAlterar.setDataCriacao(pessoaEncontrada.getDataCriacao());

        Pessoa pessoaAlterada = pessoaRepository.saveAndFlush(pessoaParaAlterar);
        return pessoaMapper.toPessoaResponse(pessoaAlterada);
    }

    @Transactional
    public void excluir(Long id) {
        Pessoa pessoa = buscarPorId(id);
        pessoaRepository.delete(pessoa);
    }
}
