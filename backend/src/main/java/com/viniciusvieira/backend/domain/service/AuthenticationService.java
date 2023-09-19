package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.representation.model.request.AuthenticationRequest;
import com.viniciusvieira.backend.api.representation.model.response.AuthenticationResponse;
import com.viniciusvieira.backend.core.security.service.JwtService;
import com.viniciusvieira.backend.domain.model.token.TokenModel;
import com.viniciusvieira.backend.domain.model.token.TokenType;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.TokenRepository;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CrudPessoaService crudPessoaService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Pessoa pessoa = crudPessoaService.buscarPeloEmail(request.getEmail());

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", pessoa.getRolesString());
        String jwtToken = jwtService.generateToken(claims, pessoa);

        revokedAllPessoaTokens(pessoa);
        savingPessoaToken(jwtToken, pessoa);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // Salva o Token do usuário no banco de dados
    private void savingPessoaToken(String jwtToken, Pessoa pessoa) {
        TokenModel tokenModel = TokenModel.builder()
                .token(jwtToken)
                .pessoa(pessoa)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(tokenModel);
    }

    // Revogando Todos os tokens do usuário
    private void revokedAllPessoaTokens(Pessoa pessoa){
        List<TokenModel> validsUserTokens = tokenRepository.findAllValidTokensByPessoa(pessoa.getId());
        if (validsUserTokens.isEmpty()){
            // se o usuário nao possui nenhum token registrado, retorna sem fazer nada
            return;
        }

        validsUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validsUserTokens);
    }
}
