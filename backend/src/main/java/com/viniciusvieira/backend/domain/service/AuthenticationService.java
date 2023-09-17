package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.representation.model.request.AuthenticationRequest;
import com.viniciusvieira.backend.api.representation.model.response.AuthenticationResponse;
import com.viniciusvieira.backend.core.security.service.JwtService;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import com.viniciusvieira.backend.domain.service.usuario.CrudPessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CrudPessoaService crudPessoaService;
    private final JwtService jwtService;

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // TEST
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Pessoa pessoa = crudPessoaService.buscarPeloEmail(request.getEmail());

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", pessoa.getRolesString());
        String jwtToken = jwtService.generateToken(claims, pessoa);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
