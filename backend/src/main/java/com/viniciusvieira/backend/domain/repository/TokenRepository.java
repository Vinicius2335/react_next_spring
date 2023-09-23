package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.token.TokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenModel, Long> {

    @Query("""
            SELECT t from TokenModel t INNER JOIN Pessoa p ON t.pessoa.id = p.id
            WHERE p.id = :pessoaId AND (t.expired = false OR t.revoked = false)
            """)
    List<TokenModel> findAllValidTokensByPessoa(Long pessoaId);

    Optional<TokenModel> findByToken(String token);

    @Modifying
    @Query("""
            DELETE FROM TokenModel t
            WHERE NOT (t.id = :tokenId) AND (t.pessoa.id = :pessoaId)
            """)
    void deleteOthersTokens(Long tokenId, Long pessoaId);

    void deleteByPessoaId(Long pessoaId);
}
