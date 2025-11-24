package com.hortifruti.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidade que representa um token de atualização.
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_hash", nullable = false, length = 255)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(name = "ip", length = 100)
    private String ip;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Construtor padrão.
     */
    public RefreshToken() {
    }

    /**
     * Define o ID e a data de criação antes da persistência.
     */
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        this.createdAt = Instant.now();
    }

    /**
     * Retorna o ID do token de atualização.
     * @return O ID do token.
     */
    public UUID getId() { return id; }
    /**
     * Define o ID do token de atualização.
     * @param id O ID do token.
     */
    public void setId(UUID id) { this.id = id; }
    /**
     * Retorna o usuário associado ao token.
     * @return O usuário.
     */
    public User getUser() { return user; }
    /**
     * Define o usuário associado ao token.
     * @param user O usuário.
     */
    public void setUser(User user) { this.user = user; }
    /**
     * Retorna o hash do token.
     * @return O hash do token.
     */
    public String getTokenHash() { return tokenHash; }
    /**
     * Define o hash do token.
     * @param tokenHash O hash do token.
     */
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    /**
     * Retorna a data de expiração do token.
     * @return A data de expiração.
     */
    public Instant getExpiresAt() { return expiresAt; }
    /**
     * Define a data de expiração do token.
     * @param expiresAt A data de expiração.
     */
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    /**
     * Retorna a data de revogação do token.
     * @return A data de revogação.
     */
    public Instant getRevokedAt() { return revokedAt; }
    /**
     * Define a data de revogação do token.
     * @param revokedAt A data de revogação.
     */
    public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }
    /**
     * Retorna o user agent do token.
     * @return O user agent.
     */
    public String getUserAgent() { return userAgent; }
    /**
     * Define o user agent do token.
     * @param userAgent O user agent.
     */
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    /**
     * Retorna o IP associado ao token.
     * @return O IP.
     */
    public String getIp() { return ip; }
    /**
     * Define o IP associado ao token.
     * @param ip O IP.
     */
    public void setIp(String ip) { this.ip = ip; }
    /**
     * Retorna a data de criação do token.
     * @return A data de criação.
     */
    public Instant getCreatedAt() { return createdAt; }
    /**
     * Define a data de criação do token.
     * @param createdAt A data de criação.
     */
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
