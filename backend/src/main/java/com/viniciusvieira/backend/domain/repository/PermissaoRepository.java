package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    Optional<Permissao> findByNome(String nome);
}
