package com.example.springbootconcepts.config;

import com.example.springbootconcepts.services.SessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfigWithCookieAuth {

    private final SessionService sessionService;
    private final String cookieName = "bff_session"; // or inject @Value

    public SecurityConfigWithCookieAuth(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                // You must think about CSRF for cookie-auth: enable or disable with care
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/public/**", "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        // Add cookie filter before username-password filter
        http.addFilterBefore(new CookieAuthenticationFilter(sessionService, cookieName),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // corsConfigurationSource bean (from step 2) should be present
}
