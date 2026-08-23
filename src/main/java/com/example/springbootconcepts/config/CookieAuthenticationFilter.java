package com.example.springbootconcepts.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import com.example.springbootconcepts.services.SessionService;
import com.example.springbootconcepts.domains.UserSession;

public class CookieAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CookieAuthenticationFilter.class);

    private final SessionService sessionService;
    private final String cookieName;

    public CookieAuthenticationFilter(SessionService sessionService, String cookieName) {
        this.sessionService = sessionService;
        this.cookieName = cookieName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.trace("CookieAuthenticationFilter: current SecurityContext auth={}", SecurityContextHolder.getContext().getAuthentication());

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Cookie cookie = WebUtils.getCookie(request, cookieName);
            if (cookie != null) {
                String sessionId = cookie.getValue();
                log.debug("CookieAuthenticationFilter: found cookie '{}' with value='{}'", cookieName, sessionId);

                Optional<UserSession> maybe = sessionService.getSession(sessionId);
                if (maybe.isPresent()) {
                    UserSession session = maybe.get();
                    log.debug("CookieAuthenticationFilter: loaded session for user='{}'", session.getUsername());

                    List<GrantedAuthority> authorities = session.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            session.getUsername(), null, authorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("CookieAuthenticationFilter: authentication set for user='{}'", session.getUsername());
                } else {
                    log.debug("CookieAuthenticationFilter: no session found for id='{}'", sessionId);
                }
            } else {
                log.trace("CookieAuthenticationFilter: no cookie '{}' present", cookieName);
            }
        }
        filterChain.doFilter(request, response);
    }
}
