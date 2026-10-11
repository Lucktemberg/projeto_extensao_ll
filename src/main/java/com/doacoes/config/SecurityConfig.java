package com.doacoes.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository) throws Exception {

        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName("_csrf");

        http
                .securityContext(context -> context
                        .securityContextRepository(securityContextRepository)
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(csrfHandler)
                )
                .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/acompanhar",
                                "/style.css",
                                "/app.js",
                                "/sessao.js",
                                "/error"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/doacoes/codigo/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/autenticar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/me").authenticated()
                        .requestMatchers("/usuarios/**", "/tela-usuarios")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(
                                "/doacoes/**",
                                "/tela-doacoes",
                                "/campanhas/**",
                                "/tela-campanhas"
                        ).hasAnyAuthority("ROLE_ADMIN", "DOACAO")
                        .requestMatchers("/distribuicoes/**", "/tela-distribuicoes")
                        .hasAnyAuthority("ROLE_ADMIN", "DISTRIBUICAO")
                        .requestMatchers("/estoques/**", "/tela-estoques")
                        .hasAnyAuthority("ROLE_ADMIN", "ESTOQUE")
                        .requestMatchers("/doadores/**", "/tela-doadores")
                        .hasAnyAuthority("ROLE_ADMIN", "DOADOR")
                        .requestMatchers("/receptores/**", "/tela-receptores")
                        .hasAnyAuthority("ROLE_ADMIN", "RECEPTOR")
                        .requestMatchers("/tipos-doacao/**", "/tela-tipos-doacao")
                        .hasAnyAuthority("ROLE_ADMIN", "TIPO_DOACAO")
                        .requestMatchers("/tamanhos-roupa/**", "/tela-tamanhos-roupa")
                        .hasAnyAuthority("ROLE_ADMIN", "TAMANHOS")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this::naoAutenticado)
                        .accessDeniedHandler(this::acessoNegado)
                )
                .logout(logout -> logout
                        .logoutUrl("/sair")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    private void naoAutenticado(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException exception) throws java.io.IOException {

        if (navegacaoHtml(request)) {
            response.sendRedirect("/login");
            return;
        }

        escreverJson(response, HttpStatus.UNAUTHORIZED, "Não autenticado");
    }

    private void acessoNegado(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.access.AccessDeniedException exception) throws java.io.IOException {

        if (navegacaoHtml(request)) {
            response.sendRedirect("/menu");
            return;
        }

        escreverJson(response, HttpStatus.FORBIDDEN, "Acesso negado");
    }

    private boolean navegacaoHtml(HttpServletRequest request) {
        String caminho = request.getRequestURI();
        return caminho.equals("/menu")
                || caminho.startsWith("/tela-");
    }

    private void escreverJson(
            HttpServletResponse response,
            HttpStatus status,
            String mensagem) throws java.io.IOException {

        response.setStatus(status.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"erro\":\"" + mensagem + "\",\"mensagem\":\"" + mensagem + "\"}"
        );
    }
}
