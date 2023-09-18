package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.representation.model.request.AuthenticationRequest;
import com.viniciusvieira.backend.api.representation.model.response.AuthenticationResponse;
//import com.viniciusvieira.backend.core.security.service.JwtService;
import com.viniciusvieira.backend.core.security.service.JwtService2;
import com.viniciusvieira.backend.domain.model.token.TokenModel;
import com.viniciusvieira.backend.domain.model.token.TokenType;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.repository.token.TokenModelRepository;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import jakarta.transaction.Transactional;
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
    //private final JwtService jwtService;
    private final JwtService2 jwtService;
    private final TokenModelRepository tokenModelRepository;

    @Transactional
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
        savedUserToken(jwtToken, pessoa);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void savedUserToken(String token, Pessoa pessoa){
        TokenModel tokenModel = TokenModel.builder()
                .token(token)
                .pessoa(pessoa)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenModelRepository.save(tokenModel);
    }

    private void revokedAllPessoaTokens(Pessoa pessoa){
        List<TokenModel> allValidTokens = tokenModelRepository.findAllValidTokensByPessoa(pessoa.getId());

        if (allValidTokens.isEmpty()){
            return;
        }

        allValidTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenModelRepository.saveAllAndFlush(allValidTokens);
    }
}
