package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.CarrinhoDeCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrinhoCompraRepository extends JpaRepository<CarrinhoDeCompra, Long> {
}
