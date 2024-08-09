package com.random.siddhu.service;

import com.random.siddhu.entity.RefreshToken;
import com.random.siddhu.model.TokenRefreshException;
import com.random.siddhu.repository.RefreshTokenRepository;
import com.random.siddhu.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
	
	  @Value("${jwt.refreshExpirationDateInMs}")
	    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    UserRepo userRepo;

    public RefreshToken createRefreshToken(int userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepo.findById(userId).get());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
       

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void deleteByUserId(int userId) {
        refreshTokenRepository.deleteByUserUserId(userId);
    }

//    public boolean isTokenExpired(RefreshToken token) {
//        return token.getExpiryDate().isBefore(LocalDateTime.now());
//    }
 
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
     
}
