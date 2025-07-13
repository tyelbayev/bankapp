package com.example.frontui.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain security(ServerHttpSecurity http) {
        http
                .authorizeExchange(ex -> ex
                        .pathMatchers("/", "/signup", "/css/**", "/js/**").permitAll()
                        .anyExchange().authenticated())
                .oauth2Login(Customizer.withDefaults())   // интерактивный вход
                .oauth2Client(Customizer.withDefaults()); // ⬅ нужен для WebClient
        return http.build();
    }
}
