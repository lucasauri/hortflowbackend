package com.hortifruti.dto;

import java.util.UUID;

/**
 * Data Transfer Object para dados do usuário.
 */
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String role;

    /**
     * Construtor padrão.
     */
    public UserDto() {}

    /**
     * Construtor com todos os campos.
     * @param id O ID do usuário.
     * @param name O nome do usuário.
     * @param email O email do usuário.
     * @param role O papel do usuário.
     */
    public UserDto(UUID id, String name, String email, String role) {
        this.id = id; this.name = name; this.email = email; this.role = role;
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
     * Retorna o papel do usuário.
     * @return O papel do usuário.
     */
    public String getRole() { return role; }

    /**
     * Define o papel do usuário.
     * @param role O papel do usuário.
     */
    public void setRole(String role) { this.role = role; }
}
