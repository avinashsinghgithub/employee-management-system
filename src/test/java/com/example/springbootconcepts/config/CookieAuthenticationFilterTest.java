package com.example.springbootconcepts.config;

import com.example.springbootconcepts.domains.UserSession;
import com.example.springbootconcepts.services.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CookieAuthenticationFilterTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldMapAdminRoleToSpringRoleAuthority() throws Exception {
        SessionService sessionService = mock(SessionService.class);
        when(sessionService.getSession("admin-session")).thenReturn(Optional.of(
                new UserSession("admin-session", "admin-user", List.of("ADMIN"), Instant.now().plusSeconds(3600))
        ));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new jakarta.servlet.http.Cookie("bff_session", "admin-session"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> { };

        CookieAuthenticationFilter filter = new CookieAuthenticationFilter(sessionService, "bff_session");

        filter.doFilter(request, response, chain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
        assertEquals("admin-user", authentication.getName());
    }
}
