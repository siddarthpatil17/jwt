package com.random.siddhu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.random.siddhu.entity.UserSessionentity;
import com.random.siddhu.service.SessionService;

@RestController
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping("/users/sessions/active/{userId}")
    public ResponseEntity<Boolean> isUserSessionActive(@PathVariable int userId) {
        boolean isActive = sessionService.isUserSessionActive(userId);
        return ResponseEntity.ok(isActive);
    }
    @GetMapping("/users/sessions")
    public ResponseEntity<List<UserSessionentity>> getAllSessions() {
        List<UserSessionentity> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

}
