package com.random.siddhu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.random.siddhu.entity.User;
import com.random.siddhu.entity.UserSessionentity;
import com.random.siddhu.repository.UserSessionRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private UserService userService; // Ensure UserService is injected

    // Check if a user session is active based on user ID
    public boolean isUserSessionActive(int userId) {
        UserSessionentity session = userSessionRepository.findByUserId(userId);
        return session != null && session.isActive();
    }

    // Validate a session based on username and token
    public boolean isValidSession(String username, String token) {
        UserSessionentity session = userSessionRepository.findBySessionId(token);
        return session != null && session.isActive();
    }

    // Create or update a session
    public void createOrUpdateSession(String username, String token, int company) {
        int userId = getUserIdByUsername(username);
        
        // Check if an existing session exists
        UserSessionentity existingSession = userSessionRepository.findByUserId(userId);

        if (existingSession != null && existingSession.getExpirationTime().isAfter(LocalDateTime.now())) {
            // Update the existing session
            existingSession.setSessionId(token);
            existingSession.setActive(true);
            existingSession.setLastActive(LocalDateTime.now());
            existingSession.setExpirationTime(LocalDateTime.now().plusMinutes(30));
            existingSession.setCompany(company);
            userSessionRepository.save(existingSession);
            System.out.println("Updated existing session for userId: " + userId + ", token: " + token);
        } else {
            // Create a new session if no existing session found or if the session has expired
            UserSessionentity newSession = new UserSessionentity();
            newSession.setUserId(userId);
            newSession.setSessionId(token);
            newSession.setActive(true);
            newSession.setLastActive(LocalDateTime.now());
            newSession.setExpirationTime(LocalDateTime.now().plusMinutes(30));
            newSession.setCompany(company);
            userSessionRepository.save(newSession);
            System.out.println("Created new session for userId: " + userId + ", token: " + token);
        }
    }

    
    public void updateSessionExpiration(int userId, String token, Long newExpiryTime) {
        UserSessionentity session = userSessionRepository.findByUserId(userId);
        if (session != null) {
            session.setSessionId(token); // Set the new token
            session.setExpirationTime(LocalDateTime.ofEpochSecond(newExpiryTime / 1000, 0, ZoneOffset.UTC)); // Convert expiry time to LocalDateTime
            session.setLastActive(LocalDateTime.now());
            session.setActive(true);
            userSessionRepository.save(session);
        } else {
            System.out.println("No active session found for userId: " + userId);
        }
    }

    
    
    // Clean up a session by marking it as inactive
    public void cleanUpSession(String token) {
        UserSessionentity session = userSessionRepository.findBySessionId(token);
        if (session != null) {
            session.setActive(false); // Mark the session as inactive
            userSessionRepository.save(session); // Save the updated session
        }
    }

    // Get all active and inactive sessions
    public List<UserSessionentity> getAllSessions() {
        return userSessionRepository.findAll();
    }

    // Count active sessions
    public long countActiveSessions() {
        return userSessionRepository.findAll().stream().filter(UserSessionentity::isActive).count();
    }

    // Get user ID by username
    private int getUserIdByUsername(String username) {
        User user = userService.getUserByEmail(username);
        return user != null ? user.getUserId() : -1; // Return -1 or handle error appropriately
    }

    // Refresh session expiration time
    public void refreshSession(String token) {
        UserSessionentity session = userSessionRepository.findBySessionId(token);
        if (session != null) {
            session.setLastActive(LocalDateTime.now());
            session.setExpirationTime(LocalDateTime.now().plusMinutes(30)); // Refresh session expiration time
            userSessionRepository.save(session);
        }
    }

    // Clean up inactive sessions
    @Transactional
    public void cleanUpInactiveSessions() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(30);
        userSessionRepository.deleteAllByLastActiveBefore(expirationTime);
    }
}
