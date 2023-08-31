package com.viniciusvieira.backend.domain.service.venda;

import com.viniciusvieira.backend.api.mapper.venda.CategoriaMapper;
import com.viniciusvieira.backend.api.representation.model.request.venda.CategoriaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CategoriaResponse;
import com.viniciusvieira.backend.domain.exception.venda.CategoriaAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.venda.CategoriaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.venda.Categoria;
import com.viniciusvieira.backend.domain.repository.venda.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudCategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public List<Categoria> buscarTodos() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPeloId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException("Categoria não cadastrada"));
    }

    @Transactional
    public CategoriaResponse inserir(CategoriaRequest categoriaRequest) {
        verifyIfCategoriaExistsByNome(categoriaRequest.getNome());

        Categoria categoriaParaInserir = categoriaMapper.toDomainCategoria(categoriaRequest);
        Categoria categoriaInserida = categoriaRepository.saveAndFlush(categoriaParaInserir);
        return categoriaMapper.toCategoriaResponse(categoriaInserida);
    }

    private void verifyIfCategoriaExistsByNome(String categoriaNome) {
        boolean categoriaExists = categoriaRepository.findByNome(categoriaNome).isPresent();

        if (categoriaExists){
            throw new CategoriaAlreadyExistsException("Já existe uma categoria cadastrada com o NOME: " + categoriaNome);
        }
    }

    @Transactional
    public CategoriaResponse alterar(Long id, CategoriaRequest categoriaRequest) {
        Categoria categoriaEncontrada = buscarPeloId(id);
        verifyIfCategoriaExistsByNome(categoriaRequest.getNome());

        Categoria categoriaParaAlterar = categoriaMapper.toDomainCategoria(categoriaRequest);
        categoriaParaAlterar.setId(categoriaEncontrada.getId());
        categoriaParaAlterar.setDataCriacao(categoriaEncontrada.getDataCriacao());

        Categoria categoriaAlterada = categoriaRepository.saveAndFlush(categoriaParaAlterar);
        return categoriaMapper.toCategoriaResponse(categoriaAlterada);
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoriaParaExcluir = buscarPeloId(id);
        categoriaRepository.delete(categoriaParaExcluir);
    }
}
