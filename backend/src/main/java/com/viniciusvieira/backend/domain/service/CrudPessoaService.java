package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.PessoaMapper;
import com.viniciusvieira.backend.api.representation.model.request.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.Permissao;
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
    private final CrudPermissaoService permissaoService;

    public List<Pessoa> buscarTodos() {
        return pessoaRepository.findAll();
    }

    public Pessoa buscarPorId(Long id){
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException("Pessoa não encontrada"));
    }

    @Transactional
    public PessoaResponse inserir(PessoaRequest pessoaRequest) {
        Permissao permissao = permissaoService.buscarPeloNome(pessoaRequest.getNomePermissao());
        Pessoa pessoaParaSalvar = pessoaMapper.toDomainPessoa(pessoaRequest);
        boolean cpfEmUso = pessoaRepository.findByCpf(pessoaParaSalvar.getCpf()).isPresent();

        if (cpfEmUso){
            throw new NegocioException("Já existe uma pessoa cadastrada com esse CPF");
        }

        pessoaParaSalvar.adicionarPermissao(permissao);
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

    @Transactional
    public void excluirPermissao(Long idPessoa, Long idPermissao){
        Pessoa pessoa = buscarPorId(idPessoa);
        pessoa.removerPermissao(idPermissao);
        pessoaRepository.saveAndFlush(pessoa);
    }

    public void excluirTodasPessoasRelacionadasCidadeId(Long cidadeId){
        List<Pessoa> pessoas = pessoaRepository.findAllPessoasByCidadeId(cidadeId);
        if (!pessoas.isEmpty()){
            pessoas.forEach(pessoaRepository::delete);
        }
    }

    public void excluirTodasPessoasRelacionadasEstadoId(Long estadoId){
        List<Pessoa> pessoas = pessoaRepository.findPessoasByCidadeEstadoId(estadoId);
        if (!pessoas.isEmpty()){
            pessoas.forEach(pessoaRepository::delete);
        }
    }
}
