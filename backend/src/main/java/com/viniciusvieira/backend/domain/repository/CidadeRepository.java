package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    @Query("from Cidade c join fetch c.estado e where e.id = :id ")
    List<Cidade> findAllCidadeByIdEstado(Long id);
}
