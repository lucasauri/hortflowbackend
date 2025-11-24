package com.hortifruti.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidade que representa um usuário.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "user_role", nullable = false, length = 50)
    private String role = "USER";

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Construtor padrão.
     */
    public User() {
    }

    /**
     * Define o ID e as datas de criação e atualização antes da persistência.
     */
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Define a data de atualização antes da atualização.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Retorna o ID do usuário.
     * @return O ID do usuário.
     */
    public UUID getId() { return id; }
    /**
     * Define o ID do usuário.
     * @param id O ID do usuário.
     */
    public void setId(UUID id) { this.id = id; }
    /**
     * Retorna o nome do usuário.
     * @return O nome do usuário.
     */
    public String getName() { return name; }
    /**
     * Define o nome do usuário.
     * @param name O nome do usuário.
     */
    public void setName(String name) { this.name = name; }
    /**
     * Retorna o email do usuário.
     * @return O email do usuário.
     */
    public String getEmail() { return email; }
    /**
     * Define o email do usuário.
     * @param email O email do usuário.
     */
    public void setEmail(String email) { this.email = email; }
    /**
     * Retorna o hash da senha do usuário.
     * @return O hash da senha.
     */
    public String getPasswordHash() { return passwordHash; }
    /**
     * Define o hash da senha do usuário.
     * @param passwordHash O hash da senha.
     */
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    /**
     * Retorna o papel do usuário.
     * @return O papel do usuário.
     */
    public String getRole() { return role; }
    /**
     * Define o papel do usuário.
     * @param role O papel do usuário.
     */
    public void setRole(String role) { this.role = role; }
    /**
     * Retorna o status de atividade do usuário.
     * @return true se o usuário estiver ativo, false caso contrário.
     */
    public boolean isActive() { return active; }
    /**
     * Define o status de atividade do usuário.
     * @param active true se o usuário estiver ativo, false caso contrário.
     */
    public void setActive(boolean active) { this.active = active; }
    /**
     * Retorna a data de criação do usuário.
     * @return A data de criação.
     */
    public Instant getCreatedAt() { return createdAt; }
    /**
     * Define a data de criação do usuário.
     * @param createdAt A data de criação.
     */
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    /**
     * Retorna a data da última atualização do usuário.
     * @return A data da última atualização.
     */
    public Instant getUpdatedAt() { return updatedAt; }
    /**
     * Define a data da última atualização do usuário.
     * @param updatedAt A data da última atualização.
     */
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
