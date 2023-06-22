package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.CarrinhoDeCompraProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrinhoDeCompraProdutoRepository extends JpaRepository<CarrinhoDeCompraProduto, Long> {
}
