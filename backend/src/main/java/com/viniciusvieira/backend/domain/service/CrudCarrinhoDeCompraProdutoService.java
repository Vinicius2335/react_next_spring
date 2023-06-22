package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.CarrinhoDeCompraProdutoMapper;
import com.viniciusvieira.backend.api.representation.model.request.CarrinhoDeCompraProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.CarrinhoDeCompraProdutoResponse;
import com.viniciusvieira.backend.domain.model.CarrinhoDeCompraProduto;
import com.viniciusvieira.backend.domain.repository.CarrinhoDeCompraProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudCarrinhoDeCompraProdutoService {
    private final CarrinhoDeCompraProdutoRepository carrinhoDeCompraProdutoRepository;
    private final CarrinhoDeCompraProdutoMapper carrinhoDeCompraProdutoMapper;

    public List<CarrinhoDeCompraProduto> buscarTodos() {
        return carrinhoDeCompraProdutoRepository.findAll();
    }

    @Transactional
    public CarrinhoDeCompraProdutoResponse inserir(CarrinhoDeCompraProdutoRequest carrinhoDeCompraProdutoRequest) {
        CarrinhoDeCompraProduto carrinhoDeCompraProdutoParaInserir = carrinhoDeCompraProdutoMapper
                .toDomainCarrinhoDeCompraProduto(carrinhoDeCompraProdutoRequest);
        CarrinhoDeCompraProduto carrinhoDeCompraProdutoSalvo = carrinhoDeCompraProdutoRepository
                .saveAndFlush(carrinhoDeCompraProdutoParaInserir);
        return carrinhoDeCompraProdutoMapper.toCarrinhoDeCompraProdutoResponse(carrinhoDeCompraProdutoSalvo);
    }

    // TODO - Excluir CarrinhoDeCompraProduto
    // findByCarrinhoDeCompraId -> retorna uma lista de CarrinhoDeCompraProduto
    // exclui todos os elementos da lista de CarrinhoDeCompraProduto
    // pq para cada associação entre CarrinhoDeCompra + Produto -> cria um CarrinhoDeCompraProduto
}
