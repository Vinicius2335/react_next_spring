package com.viniciusvieira.backend.core.security.config;

import com.viniciusvieira.backend.domain.repository.usuario.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class UserDetailsServiceConfig {
    private final PessoaRepository pessoaRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return email -> pessoaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Pessoa não encontrada pelo email..."));
    }
}
