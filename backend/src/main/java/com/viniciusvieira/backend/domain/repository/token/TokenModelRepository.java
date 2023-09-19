package com.viniciusvieira.backend.domain.repository.token;

import com.viniciusvieira.backend.domain.model.token.TokenModel;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenModelRepository extends JpaRepository<TokenModel, Long> {

    @Query("""
            SELECT t from TokenModel t INNER JOIN Pessoa p ON t.pessoa.id = p.id
            WHERE p.id = :pessoaId AND (t.expired = false OR t.revoked = false)
            """)
    List<TokenModel> findAllValidTokensByPessoa(Long pessoaId);

    void deleteAllByPessoaEmail(String email);

    Optional<TokenModel> findByToken(String token);
}