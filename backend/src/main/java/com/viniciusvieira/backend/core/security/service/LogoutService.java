package com.viniciusvieira.backend.core.security.service;

import com.viniciusvieira.backend.domain.exception.TokenException;
import com.viniciusvieira.backend.domain.model.token.TokenModel;
import com.viniciusvieira.backend.domain.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new TokenException("Faltando o header Authorization ou token");
        }

        jwt = authHeader.substring(7);
        Optional<TokenModel> storedTokenOpt = tokenRepository.findByToken(jwt);
        if (storedTokenOpt.isPresent()){
            TokenModel storedToken = storedTokenOpt.get();
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);

            tokenRepository.deleteOthersTokens(storedToken.getId(), storedToken.getPessoa().getId());
        }

    }
}
