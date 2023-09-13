package com.viniciusvieira.backend.domain.repository.venda;

import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoImagemRepository extends JpaRepository<ProdutoImagem, Long> {

    List<ProdutoImagem> findByProdutoId(Long id);
}
