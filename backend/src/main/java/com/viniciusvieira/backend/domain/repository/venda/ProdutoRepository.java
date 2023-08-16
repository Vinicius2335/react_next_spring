package com.viniciusvieira.backend.domain.repository.venda;

import com.viniciusvieira.backend.domain.model.venda.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("from Produto p join fetch p.marca m where m.id = :id")
    List<Produto> findAllProdutosByMarcaId(@Param("id") Long marcaId);

    @Query("from Produto p join fetch p.categoria c where c.id = :id")
    List<Produto> findAllProdutosByCategoriaId(@Param("id") Long categoriaId);
}
