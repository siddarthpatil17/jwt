package com.random.siddhu.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserSessionentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int userId; // Unique identifier for the user
    private String sessionId; // Unique session identifier
    private boolean isActive; // Indicates if the session is active
    private Long expiryTime;
    @Column(name = "last_active")
    private LocalDateTime lastActive; // Timestamp of the last activity in the session

    private LocalDateTime expirationTime;// Timestamp of when the session expires
    private String token;
    private int company; // Company ID associated with the user session

    // Default constructor
    public UserSessionentity() {
        super();
    }

    // Getters and setters
    
    public Long getId() {
        return id;
    }

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Long expiryTime) {
		this.expiryTime = expiryTime;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) { // Changed parameter name to 'company' for clarity
        this.company = company;
    }
}
