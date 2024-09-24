package com.anderson.catalog.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable());
//		http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
//		return http.build();
//	}
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) // Desabilita o CSRF para o H2 Console funcionar
	        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // Desabilita a proteção de frameOptions de maneira correta
	        .authorizeHttpRequests(auth -> {
	            auth.requestMatchers("/h2-console/**").permitAll(); // Libera o acesso ao H2 Console
	            auth.anyRequest().permitAll(); // Protege as outras requisições
	        });
	    return http.build();
	}
    
}