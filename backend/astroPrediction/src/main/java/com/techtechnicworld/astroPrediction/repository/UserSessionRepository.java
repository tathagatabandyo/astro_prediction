package com.techtechnicworld.astroPrediction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techtechnicworld.astroPrediction.entity.UserSession;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long>{
    Optional<UserSession> findBySessionId(String sessionId);

    List<UserSession> findByUserIdAndRevokedFalseAndExpiresAtAfter(Long userId, LocalDateTime now);

    @Modifying
    @Query("UPDATE UserSession s SET s.revoked = true, s.revokedAt = :now WHERE s.user.id = :userId AND s.revoked = false")
    void revokeAllByUserId(Long userId, LocalDateTime now);

    @Modifying
    @Query("UPDATE UserSession s SET s.revoked = true, s.revokedAt = :now WHERE s.sessionId = :sessionId")
    void revokeBySessionId(String sessionId, LocalDateTime now);
}
