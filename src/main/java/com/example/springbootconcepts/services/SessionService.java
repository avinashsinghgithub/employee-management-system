package com.example.springbootconcepts.services;

import com.example.springbootconcepts.domains.UserSession;

import java.util.Optional;

public interface SessionService {
    Optional<UserSession> getSession(String sessionId);
}

