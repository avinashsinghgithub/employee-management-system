package com.example.springbootconcepts.services.impl;

import com.example.springbootconcepts.domains.UserSession;
import com.example.springbootconcepts.services.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisSessionService implements SessionService {
    private static final Logger log = LoggerFactory.getLogger(RedisSessionService.class);

    private static final String KEY_PREFIX = "spring:session:sessions:";
    private static final String AUTHENTICATED_ATTR = "sessionAttr:isAuthenticated";
    private static final String USER_ID_ATTR = "sessionAttr:userId";
    private static final String ROLES_ATTR = "sessionAttr:roles";

    private final RedisTemplate<String, Object> redis;
    private final ObjectMapper mapper;
    private final Duration defaultTtl = Duration.ofHours(2);

    public RedisSessionService(RedisConnectionFactory redisConnectionFactory, ObjectMapper mapper) {
        this.redis = new RedisTemplate<>();
        this.redis.setConnectionFactory(redisConnectionFactory);
        this.redis.setKeySerializer(new StringRedisSerializer());
        this.redis.setHashKeySerializer(new StringRedisSerializer());
        this.redis.setValueSerializer(new JdkSerializationRedisSerializer());
        this.redis.setHashValueSerializer(new JdkSerializationRedisSerializer());
        this.redis.afterPropertiesSet();
        this.mapper = mapper;
    }

    @Override
    public Optional<UserSession> getSession(String sessionId) {
        if (sessionId == null) return Optional.empty();

        for (String candidateSessionId : getCandidateSessionIds(sessionId)) {
            Optional<UserSession> session = getSpringSession(candidateSessionId);
            if (session.isPresent()) {
                return session;
            }
        }

        return Optional.empty();
    }

    private Optional<UserSession> getSpringSession(String sessionId) {
        String k = KEY_PREFIX + sessionId;
        Map<Object, Object> sessionAttributes = redis.opsForHash().entries(k);
        if (sessionAttributes.isEmpty()) {
            log.debug("No Spring Session Redis hash found for key '{}'", k);
            return Optional.empty();
        }

        Object authenticated = sessionAttributes.get(AUTHENTICATED_ATTR);
        if (!Boolean.TRUE.equals(authenticated)) {
            log.debug("Spring Session key '{}' is not authenticated. {}={}", k, AUTHENTICATED_ATTR, authenticated);
            return Optional.empty();
        }

        Object userId = sessionAttributes.get(USER_ID_ATTR);
        String username = userId == null ? "Unknown user" : String.valueOf(userId);
        List<String> roles = extractRoles(sessionAttributes);
        log.debug("Validated Spring Session key '{}' for user '{}' with roles {}", k, username, roles);

        return Optional.of(new UserSession(
                sessionId,
                username,
                roles,
                getExpiresAt(sessionAttributes)
        ));
    }

    private List<String> getCandidateSessionIds(String cookieValue) {
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(cookieValue);
        decodeBase64SessionId(cookieValue).ifPresent(candidates::add);
        return new ArrayList<>(candidates);
    }

    private Optional<String> decodeBase64SessionId(String cookieValue) {
        String paddedCookieValue = cookieValue + "=".repeat((4 - cookieValue.length() % 4) % 4);

        for (Base64.Decoder decoder : List.of(Base64.getDecoder(), Base64.getUrlDecoder())) {
            try {
                String decoded = new String(decoder.decode(paddedCookieValue), StandardCharsets.UTF_8);
                if (!decoded.isBlank()) {
                    return Optional.of(decoded);
                }
            } catch (IllegalArgumentException ignored) {
                // Try the next decoder variant.
            }
        }

        return Optional.empty();
    }

    @Override
    public String createSession(UserSession session) {
        String id = session.getSessionId() != null ? session.getSessionId() : UUID.randomUUID().toString();
        UserSession toStore = session;
        try {
            String json = mapper.writeValueAsString(toStore);
            String k = KEY_PREFIX + id;
            long ttl = defaultTtl.getSeconds();
            // if session has expiresAt, set TTL accordingly
            if (toStore.getExpiresAt() != null) {
                long secs = java.time.Duration.between(java.time.Instant.now(), toStore.getExpiresAt()).getSeconds();
                if (secs > 0) ttl = secs;
            }
            redis.opsForValue().set(k, json, ttl, TimeUnit.SECONDS);
            return id;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize session", e);
        }
    }

    @Override
    public void invalidateSession(String sessionId) {
        if (sessionId == null) return;
        redis.delete(KEY_PREFIX + sessionId);
    }

    private List<String> extractRoles(Map<Object, Object> sessionAttributes) {
        Object roles = sessionAttributes.get(ROLES_ATTR);
        if (roles instanceof List<?> list) {
            return list.stream()
                    .filter(java.util.Objects::nonNull)
                    .map(String::valueOf)
                    .filter(role -> !role.isBlank())
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .toList();
        }
        if (roles instanceof String str && !str.isBlank()) {
            return List.of(str.startsWith("ROLE_") ? str : "ROLE_" + str);
        }
        return List.of("ROLE_USER");
    }

    private Instant getExpiresAt(Map<Object, Object> sessionAttributes) {
        Object lastAccessedTime = sessionAttributes.get("lastAccessedTime");
        Object maxInactiveInterval = sessionAttributes.get("maxInactiveInterval");

        if (!(lastAccessedTime instanceof Long) || !(maxInactiveInterval instanceof Integer)) {
            return null;
        }

        return Instant.ofEpochMilli((Long) lastAccessedTime).plusSeconds((Integer) maxInactiveInterval);
    }
}

