package com.viniciusvieira.backend.domain.service.venda;

import com.viniciusvieira.backend.api.mapper.venda.CarrinhoDeCompraProdutoMapper;
import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraProdutoResponse;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompraProduto;
import com.viniciusvieira.backend.domain.repository.venda.CarrinhoDeCompraProdutoRepository;
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
