package com.viniciusvieira.backend.domain.service.venda;

import com.viniciusvieira.backend.api.mapper.venda.ProdutoMapper;
import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.exception.ProdutoNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public List<Produto> buscarTodos(){
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id){
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrada"));
    }

    @Transactional
    public ProdutoResponse inserir(ProdutoRequest produtoRequest) {
        Produto produtoParaSalvar = produtoMapper.toDomainProduto(produtoRequest);
        Produto produtoSalva = produtoRepository.saveAndFlush(produtoParaSalvar);

        return produtoMapper.toProdutoResponse(produtoSalva);
    }

    @Transactional
    public ProdutoResponse alterar(Long id, ProdutoRequest produtoRequest) {
        Produto produtoEncontrada = buscarPorId(id);
        Produto produtoParaAlterar = produtoMapper.toDomainProduto(produtoRequest);
        produtoParaAlterar.setId(produtoEncontrada.getId());
        produtoParaAlterar.setDataCriacao(produtoEncontrada.getDataCriacao());

        Produto produtoAlterada = produtoRepository.saveAndFlush(produtoParaAlterar);
        return produtoMapper.toProdutoResponse(produtoAlterada);
    }

    @Transactional
    public void excluir(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }

    public void excluirTodosProdutosRelacionadosMarcaId(Long marcaId){
        List<Produto> produtos = produtoRepository.findAllProdutosByMarcaId(marcaId);
        if (!produtos.isEmpty()){
            produtos.forEach(produtoRepository::delete);
        }
    }

    public void excluirTodosProdutosRelacionadosCategoriaId(Long categoriaId){
        List<Produto> produtos = produtoRepository.findAllProdutosByCategoriaId(categoriaId);
        if (!produtos.isEmpty()){
            produtos.forEach(produtoRepository::delete);
        }
    }
}
