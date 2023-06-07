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
    private final CrudMarcaService crudMarcaService;
    private final CrudCategoriaService crudCategoriaService;
    private final CrudProdutoService crudProdutoService;

    @Transactional
    public void cascadeDeleteEstado(Long estadoId){
        // verifica se o estado existe
        crudEstadoService.buscarPeloId(estadoId);
        // exclui todas as permissoes/pessosas relacionadas Estado ID
        crudPessoaService.excluirTodasPessoasRelacionadasEstadoId(estadoId);
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

    @Transactional
    public void cascadeDeleteMarca(Long marcaId){
        crudMarcaService.buscarPeloId(marcaId);
        crudProdutoService.excluirTodosProdutosRelacionadosMarcaId(marcaId);
        crudMarcaService.excluir(marcaId);
    }

    @Transactional
    public void cascadeDeleteCategoria(Long categoriaId){
        crudCategoriaService.buscarPeloId(categoriaId);
        crudProdutoService.excluirTodosProdutosRelacionadosCategoriaId(categoriaId);
        crudCategoriaService.excluir(categoriaId);
    }
}
