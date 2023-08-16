package com.viniciusvieira.backend.domain.service.usuario;


import com.viniciusvieira.backend.api.mapper.usuario.PermissaoMapper;
import com.viniciusvieira.backend.api.representation.model.request.usuario.PermissaoRequest;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PermissaoResponse;
import com.viniciusvieira.backend.domain.exception.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudPermissaoService {
    private final PermissaoRepository permissaoRepository;
    private final PermissaoMapper permissaoMapper;
    
    public List<Permissao> buscarTodos() {
        return permissaoRepository.findAll();
    }

    public Permissao buscarPeloId(Long id) {
        return permissaoRepository.findById(id)
                .orElseThrow(() -> new PermissaoNaoEncontradaException("Permissão não cadastrada"));
    }

    public Permissao buscarPeloNome(String nome){
        return permissaoRepository.findByNome(nome)
                .orElseThrow(() -> new PermissaoNaoEncontradaException("Permissão não encontrada"));
    }

    @Transactional
    public PermissaoResponse inserir(PermissaoRequest permissaoRequest) {
        Permissao permissaoParaInserir = permissaoMapper.toDomainPermissao(permissaoRequest);
        Permissao permissaoInserida = permissaoRepository.saveAndFlush(permissaoParaInserir);
        return permissaoMapper.toPermissaoResponse(permissaoInserida);
    }
    
    @Transactional
    public PermissaoResponse alterar(Long id, PermissaoRequest permissaoRequest) {
        Permissao permissaoEncontrada = buscarPeloId(id);
        Permissao permissaoParaAlterar = permissaoMapper.toDomainPermissao(permissaoRequest);
        permissaoParaAlterar.setId(permissaoEncontrada.getId());
        permissaoParaAlterar.setDataCriacao(permissaoEncontrada.getDataCriacao());

        Permissao permissaoAlterada = permissaoRepository.saveAndFlush(permissaoParaAlterar);
        return permissaoMapper.toPermissaoResponse(permissaoAlterada);
    }

    @Transactional
    public void excluir(Long id) {
        Permissao permissaoParaExcluir = buscarPeloId(id);
        permissaoRepository.delete(permissaoParaExcluir);
    }
}
