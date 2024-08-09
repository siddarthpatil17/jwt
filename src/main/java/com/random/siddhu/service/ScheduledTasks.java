package com.random.siddhu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private SessionService sessionService;

    @Scheduled(fixedRate = 60000) // Cleanup every minute
    public void cleanUpInactiveSessions() {
        sessionService.cleanUpInactiveSessions();
    }
}
