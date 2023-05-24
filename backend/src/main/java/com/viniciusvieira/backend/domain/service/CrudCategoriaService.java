package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.domain.exception.CategoriaNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Categoria;
import com.viniciusvieira.backend.domain.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudCategoriaService implements ICrud<Categoria, Long>{
    private final CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> buscarTodos() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria buscarPeloId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradoException("Categoria não cadastrada"));
    }

    @Override
    @Transactional
    public Categoria inserir(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public Categoria alterar(Long id, Categoria categoria) {
        Categoria categoriaParaAlterar = buscarPeloId(id);
        categoriaParaAlterar.setNome(categoria.getNome());
        return categoriaRepository.saveAndFlush(categoriaParaAlterar);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Categoria categoriaParaExcluir = buscarPeloId(id);
        categoriaRepository.delete(categoriaParaExcluir);
    }
}
