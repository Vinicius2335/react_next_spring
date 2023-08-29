package com.viniciusvieira.backend.domain.service.venda;

import com.viniciusvieira.backend.api.mapper.venda.CarrinhoDeCompraMapper;
import com.viniciusvieira.backend.api.representation.model.request.venda.CarrinhoDeCompraRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.CarrinhoDeCompraResponse;
import com.viniciusvieira.backend.domain.exception.CarrinhoDeCompraNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.CarrinhoDeCompra;
import com.viniciusvieira.backend.domain.repository.venda.CarrinhoCompraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudCarrinhoDeCompraService {
    private final CarrinhoCompraRepository carrinhoCompraRepository;
    private final CarrinhoDeCompraMapper carrinhoDeCompraMapper;

    public List<CarrinhoDeCompra> buscarTodos() {
        return carrinhoCompraRepository.findAll();
    }

    public CarrinhoDeCompra buscarPeloId(Long id) {
        return carrinhoCompraRepository.findById(id)
                .orElseThrow(() -> new CarrinhoDeCompraNaoEncontradoException("CarrinhoDeCompra não cadastrada"));
    }

    @Transactional
    public CarrinhoDeCompraResponse inserir(CarrinhoDeCompraRequest cidadeRequest) {
        CarrinhoDeCompra cidadeParaInserir = carrinhoDeCompraMapper.toDomainCarrinhoDeCompra(cidadeRequest);
        CarrinhoDeCompra cidadeInserida = carrinhoCompraRepository.saveAndFlush(cidadeParaInserir);
        return carrinhoDeCompraMapper.toCarrinhoDeCompraResponse(cidadeInserida);
    }

    @Transactional
    public CarrinhoDeCompraResponse alterar(Long id, CarrinhoDeCompraRequest cidadeRequest) {
        CarrinhoDeCompra cidadeEncontrada = buscarPeloId(id);
        CarrinhoDeCompra cidadeParaAlterar = carrinhoDeCompraMapper.toDomainCarrinhoDeCompra(cidadeRequest);
        cidadeParaAlterar.setId(cidadeEncontrada.getId());
        cidadeParaAlterar.setDataCriacao(cidadeEncontrada.getDataCriacao());

        CarrinhoDeCompra cidadeAlterada = carrinhoCompraRepository.saveAndFlush(cidadeParaAlterar);
        return carrinhoDeCompraMapper.toCarrinhoDeCompraResponse(cidadeAlterada);
    }

    @Transactional
    public void excluir(Long id) {
        CarrinhoDeCompra cidadeEncontrada = buscarPeloId(id);
        carrinhoCompraRepository.delete(cidadeEncontrada);
    }

}
