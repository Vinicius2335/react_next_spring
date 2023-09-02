package com.viniciusvieira.backend.domain.service.usuario;

import com.viniciusvieira.backend.api.mapper.usuario.PessoaMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import com.viniciusvieira.backend.domain.exception.usuario.CpfAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.usuario.PessoaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
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

    public Pessoa buscarPeloEmail(String email){
        return pessoaRepository.findByEmail(email)
                .orElseThrow(() -> new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este EMAIL"));
    }

    public Pessoa buscarPorId(Long id){
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este ID"));
    }

    public Pessoa buscarPeloEmailECodigo(String email, String codigo){
        return pessoaRepository.findByEmailAndCodigoRecuperacaoSenha(email, codigo)
                .orElseThrow(() -> new PessoaNaoEncontradaException("Não existe nenhuma pessoa cadastrada com este EMAIL e CODIGO"));
    }

    @Transactional
    public PessoaResponse inserir(PessoaRequest pessoaRequest) {
        Permissao permissao = permissaoService.buscarPeloNome(pessoaRequest.getNomePermissao());
        verifyIfPessoaExistsByCpf(pessoaRequest.getCpf());

        Pessoa pessoaParaSalvar = pessoaMapper.toDomainPessoa(pessoaRequest);
        pessoaParaSalvar.adicionarPermissao(permissao);
        Pessoa pessoaSalva = pessoaRepository.saveAndFlush(pessoaParaSalvar);

        return pessoaMapper.toPessoaResponse(pessoaSalva);
    }

    private void verifyIfPessoaExistsByCpf(String pessoaCpf) {
        boolean cpfEmUso = pessoaRepository.findByCpf(pessoaCpf).isPresent();

        if (cpfEmUso){
            throw new CpfAlreadyExistsException("Já existe uma pessoa cadastrada com esse CPF");
        }
    }

    @Transactional
    public PessoaResponse alterar(Long id, PessoaRequest pessoaRequest) {
        Pessoa pessoaEncontrada = buscarPorId(id);
        verifyIfPessoaExistsByCpf(pessoaRequest.getCpf());

        Pessoa pessoaParaAlterar = pessoaMapper.toDomainPessoa(pessoaRequest);
        pessoaParaAlterar.setId(pessoaEncontrada.getId());
        pessoaParaAlterar.setDataCriacao(pessoaEncontrada.getDataCriacao());

        Pessoa pessoaAlterada = pessoaRepository.saveAndFlush(pessoaParaAlterar);
        return pessoaMapper.toPessoaResponse(pessoaAlterada);
    }

    // COMMENT - Altera pessoa para realizar o gerenciamento sobre a senha
    @Transactional
    public void alterarParaGerenciamento(Pessoa pessoa){
        pessoaRepository.saveAndFlush(pessoa);
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

}
