package com.viniciusvieira.backend.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viniciusvieira.backend.api.representation.model.request.AuthenticationRequest;
import com.viniciusvieira.backend.api.representation.model.response.AuthenticationResponse;
import com.viniciusvieira.backend.core.security.service.JwtService;
import com.viniciusvieira.backend.domain.model.token.TokenModel;
import com.viniciusvieira.backend.domain.model.token.TokenType;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.TokenRepository;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    @Transactional
    public Map<String, Object> login(AuthenticationRequest request) {
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
        String refreshToken = jwtService.generateRefreshToken(pessoa);

        revokedAllPessoaTokens(pessoa);
        savingPessoaToken(jwtToken, pessoa);

        revokedAllPessoaTokens(pessoa);
        savingPessoaToken(jwtToken, pessoa);

        Map<String, Object> response = new HashMap<>();
        response.put("user", pessoa);
        response.put("access-token", jwtToken);
        response.put("refresh-tokne", refreshToken);

        return response;
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

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            Pessoa pessoa = crudPessoaService.buscarPeloEmail(userEmail);

            if (jwtService.isTokenValid(refreshToken, pessoa)){
                String accessToken = jwtService.generateSimpleToken(pessoa);

                revokedAllPessoaTokens(pessoa);
                savingPessoaToken(accessToken, pessoa);

                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
