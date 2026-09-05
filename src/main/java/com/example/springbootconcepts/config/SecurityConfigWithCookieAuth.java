package com.example.springbootconcepts.config;

import com.example.springbootconcepts.services.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigWithCookieAuth {

    private final SessionService sessionService;
    private final String cookieName = "bff_session"; // or inject @Value

    public SecurityConfigWithCookieAuth(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookieName("API-XSRF-TOKEN");
        csrfTokenRepository.setHeaderName("X-API-XSRF-TOKEN");
        csrfTokenRepository.setCookiePath("/");
        CsrfTokenRequestAttributeHandler csrfRequestHandler = new CsrfTokenRequestAttributeHandler();

        http.cors().and()
                // You must think about CSRF for cookie-auth: enable or disable with care
            .csrf().csrfTokenRepository(csrfTokenRepository)
                .csrfTokenRequestHandler(csrfRequestHandler).and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/public/**", "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions.accessDeniedHandler(accessDeniedHandler()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        // Add cookie filter before username-password filter
        http.addFilterBefore(new CookieAuthenticationFilter(sessionService, cookieName),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            var authentication = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication();
            org.slf4j.LoggerFactory.getLogger(SecurityConfigWithCookieAuth.class).warn(
                    "Access denied for {} {}. authenticated={}, authorities={}, reason={}",
                    request.getMethod(), request.getRequestURI(),
                    authentication != null,
                    authentication == null ? java.util.List.of() : authentication.getAuthorities(),
                        exception.getMessage());
                    org.slf4j.LoggerFactory.getLogger(SecurityConfigWithCookieAuth.class).warn(
                            "CSRF diagnostic: X-API-XSRF-TOKEN header present={}, API-XSRF-TOKEN cookie present={}",
                            request.getHeader("X-API-XSRF-TOKEN") != null,
                        request.getCookies() != null && java.util.Arrays.stream(request.getCookies())
                                .anyMatch(cookie -> "API-XSRF-TOKEN".equals(cookie.getName())));
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        };
    }

    // corsConfigurationSource bean (from step 2) should be present
}
