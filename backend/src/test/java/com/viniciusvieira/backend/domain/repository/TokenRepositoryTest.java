package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.token.TokenModel;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import com.viniciusvieira.backend.util.PessoaCreator;
import com.viniciusvieira.backend.util.TokenCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class TokenRepositoryTest {
    @Autowired
    private TokenRepository underTest;

    @Autowired
    private PessoaRepository pessoaRepository;

    private TokenModel tokenSaved;
    private final Pessoa pessoa = PessoaCreator.createPessoa();

    @BeforeEach
    void setUp() {
        Pessoa pessoaSaved = pessoaRepository.saveAndFlush(pessoa);
        final TokenModel token = TokenCreator.crateToken(pessoaSaved);
        tokenSaved = underTest.saveAndFlush(token);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    @DisplayName("save() add new token")
    void givenToken_whenSaveAndFlush_thenShouldBeInsertedToken(){
        // given
        Pessoa pessoaToSaved = PessoaCreator.createPessoa();
        pessoaToSaved.setId(2L);
        pessoaToSaved.setCpf("949.308.390-09");
        Pessoa pessoaSaved = pessoaRepository.saveAndFlush(pessoaToSaved);
        TokenModel tokenToSaved = TokenCreator.crateToken(pessoaSaved);
        // when
        TokenModel expected = underTest.saveAndFlush(tokenToSaved);
        // then
        assertThat(expected.getPessoa()).isEqualTo(pessoaSaved);
        assertThat(expected.getToken()).isEqualTo(tokenToSaved.getToken());
        assertThat(expected.getTokenType()).isEqualTo(tokenToSaved.getTokenType());
    }

    @Test
    @DisplayName("findAllValidTokensByPessoa() return list token by id pessoa")
    void givenIdPessoa_whenFindAllValidTokensByPessoa_thenShouldBeReturnListToken() {
        // given
        Long idPessoa = tokenSaved.getPessoa().getId();
        // when
        List<TokenModel> expected = underTest.findAllValidTokensByPessoa(idPessoa);
        // then
        assertThat(expected)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(tokenSaved);
    }

    @Test
    @DisplayName("findByToken() return tokenModel by token")
    void givenToken_whenFindByToken_thenShouldBeFindedToken() {
        // given
        String token = tokenSaved.getToken();
        // when
        Optional<TokenModel> expected = underTest.findByToken(token);
        // then
        assertThat(expected)
                .isNotNull()
                .isNotEmpty()
                .contains(tokenSaved);
    }

    @Test
    @DisplayName("findByToken() return empty when token not finded")
    void givenNonExistentToken_whenFindByToken_thenShouldBeEmpty() {
        // given
        String token = "teste";
        // when
        Optional<TokenModel> expected = underTest.findByToken(token);
        // then
        assertThat(expected).isNotPresent();
    }

    @Test
    @DisplayName("deleteOthersTokens() delete others token from pessoa")
    void givenIdTokenAndIdPessoa_whenDeleteOthersTokens_thenShoudBeRemovedOtherToken() {
        // given
        insertOtherToken();
        Long idToken = tokenSaved.getId();
        Long idPessoa = tokenSaved.getPessoa().getId();
        //when
        underTest.deleteOthersTokens(idToken, idPessoa);
        // then
        List<TokenModel> expected = underTest.findAll();
        assertThat(expected)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(tokenSaved);
    }

    @Test
    @DisplayName("deleteByPessoaId() remove token by id pessoa")
    void givenIdPessoa_whenDeleteByPessoaId_thenShouldBeRemovedToken() {
        // given
        Long idPessoa = tokenSaved.getPessoa().getId();
        // when
        underTest.deleteByPessoaId(idPessoa);
        // then
        List<TokenModel> expected = underTest.findAll();
        assertThat(expected).isEmpty();
    }

    private void insertOtherToken(){
        Pessoa pessoaFinded = pessoaRepository.findAll().stream().findFirst().orElse(null);
        final TokenModel token = TokenCreator.crateToken(pessoaFinded);
        token.setId(2L);
        tokenSaved = underTest.saveAndFlush(token);
    }
}