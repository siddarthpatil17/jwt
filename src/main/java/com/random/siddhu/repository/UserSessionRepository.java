package com.random.siddhu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.random.siddhu.entity.UserSessionentity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessionentity, Long> {
    UserSessionentity findByUserId(int userId);
    UserSessionentity findBySessionId(String sessionId);
    List<UserSessionentity> findByIsActiveFalse(); // New method to find inactive sessions
    List<UserSessionentity> findAll();
//    List<UserSessionentity> deleteAllByExpirationTimeBefore(LocalDateTime time);
    void deleteAllByLastActiveBefore(LocalDateTime expirationTime);
    long countByIsActiveTrue();
    UserSessionentity findByUserIdAndIsActiveTrue(int userId);
}
