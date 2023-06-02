package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Optional<Pessoa> findByCpf(String cpf);

    @Query("from Pessoa p join fetch p.cidade c where c.id = :id")
    List<Pessoa> findAllPessoasByCidadeId(@Param("id") Long cidadeId);

    List<Pessoa> findPessoasByCidadeId(Long cidadeId);
}