package com.phong.mini_banking.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomerSessionService {
    public static final String SESSION_REPLACED_ATTRIBUTE = "customerSessionReplaced";

    private final Map<Long, HttpSession> sessionsByUserId = new HashMap<>();
    private final Map<String, Long> userIdsBySessionId = new HashMap<>();

    public synchronized void register(Long userId, HttpSession session) {
        session.removeAttribute(SESSION_REPLACED_ATTRIBUTE);
        HttpSession previousSession = sessionsByUserId.put(userId, session);
        if (previousSession != null && !previousSession.getId().equals(session.getId())) {
            userIdsBySessionId.remove(previousSession.getId());
            replace(previousSession);
        }

        Long previousUserId = userIdsBySessionId.put(session.getId(), userId);
        if (previousUserId != null && !previousUserId.equals(userId)) {
            sessionsByUserId.remove(previousUserId);
        }
    }

    public synchronized boolean isCurrentSession(Long userId, HttpSession session) {
        return session != null && sessionsByUserId.get(userId) == session;
    }

    public synchronized boolean hasRegisteredSession(Long userId) {
        return sessionsByUserId.containsKey(userId);
    }

    public synchronized void unregister(HttpSession session) {
        Long userId = userIdsBySessionId.remove(session.getId());
        if (userId != null && sessionsByUserId.get(userId) == session) {
            sessionsByUserId.remove(userId);
        }
    }

    private void replace(HttpSession session) {
        try {
            session.setAttribute(SESSION_REPLACED_ATTRIBUTE, Boolean.TRUE);
            session.removeAttribute("user");
        } catch (IllegalStateException ignored) {
            // The session may already have expired.
        }
    }
}
