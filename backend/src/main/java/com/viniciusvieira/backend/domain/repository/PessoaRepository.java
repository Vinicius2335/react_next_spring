package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}