package com.viniciusvieira.backend.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CascadeDeleteService {
    private final CrudEstadoService crudEstadoService;
    private final CrudCidadeService crudCidadeService;
    private final CrudPessoaService crudPessoaService;

    @Transactional
    public void cascadeDeleteEstado(Long estadoId){
        // verifica se o estado existe
        crudEstadoService.buscarPeloId(estadoId);
        // exclui todas as cidades relacionada ao estado ID
        crudCidadeService.excluirTodasCidadesRelacionadosEstadoId(estadoId);
        // exclui estado
        crudEstadoService.excluir(estadoId);
    }

    @Transactional
    public void cascadeDeleteCidade(Long cidadeId){
        // verifico se cidade existe
        crudCidadeService.buscarPeloId(cidadeId);
        // exclui todas as pessoas relacionadas com cidadeId
        crudPessoaService.excluirTodasPessoasRelacionadasCidadeId(cidadeId);
        // exclui cidade
        crudCidadeService.excluir(cidadeId);
    }
}
