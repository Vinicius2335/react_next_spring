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

    // mesma coisa que em cima, porem feito pela proprio spring sem Query
    // Um Inner Join, retornando todas as pessoa relacionadas com Cidade Id 'X'
    List<Pessoa> findPessoasByCidadeId(Long cidadeId);

    // Inner Join, retornando todas as pessoas relacionadas com o Estado Id 'X'
    List<Pessoa> findPessoasByCidadeEstadoId(Long estadoId);

    // TEST
    Optional<Pessoa> findByEmail(String email);

    // TEST
    Optional<Pessoa> findByEmailAndCodigoRecuperacaoSenha(String email, String codigoRecuperacaoSenha);

}