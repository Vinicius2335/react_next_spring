package com.viniciusvieira.backend.core.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viniciusvieira.backend.domain.model.token.TokenModel;
import com.viniciusvieira.backend.domain.repository.token.TokenModelRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenModelRepository tokenModelRepository;

    @Override
    @Transactional
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
            tokenModelRepository.deleteAllByPessoaEmail(storedToken.getPessoa().getEmail());

            //storedToken.setRevoked(true);
            //storedToken.setExpired(true);
            //tokenModelRepository.saveAndFlush(storedToken);
        }
    }
}
