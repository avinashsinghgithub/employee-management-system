package com.example.springbootconcepts.domains;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * The authenticated user information obtained from a shared Spring Session.
 */
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String sessionId;
    private final String username;
    private final List<String> roles;
    private final Instant expiresAt;

    public UserSession(String sessionId, String username, List<String> roles, Instant expiresAt) {
        this.sessionId = sessionId;
        this.username = username;
        this.roles = roles == null ? List.of() : List.copyOf(roles);
        this.expiresAt = expiresAt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
