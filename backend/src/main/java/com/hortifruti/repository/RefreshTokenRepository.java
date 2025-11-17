package com.hortifruti.repository;

import com.hortifruti.model.RefreshToken;
import com.hortifruti.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Query("select r from RefreshToken r where r.tokenHash = :tokenHash and (r.revokedAt is null) and r.expiresAt > :now")
    Optional<RefreshToken> findValidByHash(String tokenHash, Instant now);

    @Modifying
    @Query("update RefreshToken r set r.revokedAt = :now where r.user = :user and r.revokedAt is null")
    int revokeAllForUser(User user, Instant now);
}
