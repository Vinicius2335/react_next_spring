package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
}
