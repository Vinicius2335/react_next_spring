package com.viniciusvieira.backend.domain.repository.venda;

import com.viniciusvieira.backend.domain.model.venda.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
