package com.microservices.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final AutoJwtValidationFilter autoJwtValidationFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos
                .requestMatchers("/api/products/search/**").permitAll()
                .requestMatchers("/api/products/by-status/**").permitAll()
                .requestMatchers("/api/products/by-category/**").permitAll()
                .requestMatchers("/api/products/by-brand/**").permitAll()
                .requestMatchers("/api/products/by-price-range/**").permitAll()
                .requestMatchers("/api/products/active/**").permitAll()
                .requestMatchers("/api/categories/**").permitAll()
                .requestMatchers("/api/brands/**").permitAll()
                .requestMatchers("/api/products/{id}").permitAll()
                .requestMatchers("/api/products").permitAll()
                
                // Endpoints que requieren autenticación
                .requestMatchers("/api/products/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(autoJwtValidationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
