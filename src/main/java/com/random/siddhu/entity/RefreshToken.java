package com.random.siddhu.entity;

import javax.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user; 
    private String token;
    private Instant expiryDate;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getUser() {  // Change getUser_id to getUser
        return user;
    }

    public void setUser(User user) {  // Change setUser_id to setUser
        this.user = user;
    }
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Instant getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}
	
    
}
