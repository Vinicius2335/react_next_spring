package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.service.venda.CrudCategoriaService;
import com.viniciusvieira.backend.domain.service.venda.CrudMarcaService;
import com.viniciusvieira.backend.domain.service.venda.CrudProdutoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CascadeDeleteService {
    private final CrudMarcaService crudMarcaService;
    private final CrudCategoriaService crudCategoriaService;
    private final CrudProdutoService crudProdutoService;


    @Transactional
    public void cascadeDeleteMarca(Long marcaId){
        // verifico se marca existe
        crudMarcaService.buscarPeloId(marcaId);
        // exclui todos os produtos relacionados com marcaId
        crudProdutoService.excluirTodosProdutosRelacionadosMarcaId(marcaId);
        // exclui marca
        crudMarcaService.excluir(marcaId);
    }

    @Transactional
    public void cascadeDeleteCategoria(Long categoriaId){
        crudCategoriaService.buscarPeloId(categoriaId);
        crudProdutoService.excluirTodosProdutosRelacionadosCategoriaId(categoriaId);
        crudCategoriaService.excluir(categoriaId);
    }
}
