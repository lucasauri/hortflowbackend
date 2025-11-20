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

/**
 * Repositório para a entidade RefreshToken.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    /**
     * Busca um token de atualização válido pelo hash.
     * @param tokenHash O hash do token.
     * @param now O momento atual.
     * @return Um Optional contendo o token de atualização, se encontrado.
     */
    @Query("select r from RefreshToken r where r.tokenHash = :tokenHash and (r.revokedAt is null) and r.expiresAt > :now")
    Optional<RefreshToken> findValidByHash(String tokenHash, Instant now);

    /**
     * Revoga todos os tokens de atualização de um usuário.
     * @param user O usuário.
     * @param now O momento atual.
     * @return O número de tokens revogados.
     */
    @Modifying
    @Query("update RefreshToken r set r.revokedAt = :now where r.user = :user and r.revokedAt is null")
    int revokeAllForUser(User user, Instant now);
}
