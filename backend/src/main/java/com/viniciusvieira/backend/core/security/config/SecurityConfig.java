package com.viniciusvieira.backend.core.security.config;

import com.viniciusvieira.backend.core.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final AuthEntryPointConfig authEntryPointConfig;
    private final LogoutHandler logoutHandler;
    private final AccessDeniedHandlerConfig accessDeniedHandlerConfig;

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_GERENTE = "GERENTE";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        AbstractHttpConfigurer::disable
                );

        http
                .cors(
                       cors -> cors
                               .configurationSource(request -> {
                                   CorsConfiguration corsConfiguration = new CorsConfiguration();
                                   corsConfiguration.setAllowedOrigins(List.of("*"));
                                   corsConfiguration.setAllowedHeaders(List.of("*"));
                                   corsConfiguration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE"));
                                   return corsConfiguration;
                               })
                );

        http
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/gerenciamento/**", "/api/auth/**")
                                .permitAll()

                                .requestMatchers("/api/clientes/**")
                                    .hasAnyRole(ROLE_ADMIN, ROLE_GERENTE)
                                .requestMatchers("/api/permissoes/**")
                                    .hasAnyRole(ROLE_ADMIN, ROLE_GERENTE)
                                .requestMatchers("/api/pessoas/**")
                                    .hasAnyRole(ROLE_ADMIN, ROLE_GERENTE)
                                .requestMatchers("/api/carrinhos/**")
                                    .hasAnyRole(ROLE_ADMIN, ROLE_GERENTE)
                                .requestMatchers("/api/carrinho-produto/**")
                                    .hasAnyRole(ROLE_ADMIN, ROLE_GERENTE)
                                .requestMatchers("/api/categorias/**")
                                    .hasAnyRole(ROLE_ADMIN, ROLE_GERENTE)
                                .requestMatchers("/api/marcas/**")
                                    .hasAnyRole(ROLE_ADMIN, ROLE_GERENTE)
                                .requestMatchers("/api/produtos/**")
                                    .hasAnyRole(ROLE_ADMIN, ROLE_GERENTE)
                                .requestMatchers("/api/imagens/**")
                                    .hasAnyRole(ROLE_ADMIN, ROLE_GERENTE)

                                .anyRequest()
                                .authenticated()
                );

        http
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint(authEntryPointConfig)
                                .accessDeniedHandler(accessDeniedHandlerConfig)
                );

        http
                .logout(
                        logout -> logout
                                .logoutUrl("/api/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(
                                        (request, response, authentication) -> SecurityContextHolder.clearContext()
                                )
                );

        return http.build();
    }
}
