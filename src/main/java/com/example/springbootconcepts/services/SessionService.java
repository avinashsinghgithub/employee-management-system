package com.example.springbootconcepts.services;

import com.example.springbootconcepts.domains.UserSession;

import java.util.Optional;

public interface SessionService {
    Optional<UserSession> getSession(String sessionId);
    void invalidateSession(String sessionId);
    String createSession(UserSession session);
}

