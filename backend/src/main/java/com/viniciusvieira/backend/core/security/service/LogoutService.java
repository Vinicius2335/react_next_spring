package com.viniciusvieira.backend.core.security.service;

import com.viniciusvieira.backend.domain.model.token.TokenModel;
import com.viniciusvieira.backend.domain.repository.token.TokenModelRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenModelRepository tokenModelRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        jwt = authHeader.substring(7);
        TokenModel storedToken = tokenModelRepository.findByToken(jwt).orElse(null);

        if (storedToken != null){
            System.out.println("Dale");
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            tokenModelRepository.saveAndFlush(storedToken);
            //SecurityContextHolder.clearContext();
        }
    }
}
