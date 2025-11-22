package com.hortifruti.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de login.
 */
public class LoginRequest {

    /**
     * Construtor padrão.
     */
    public LoginRequest() {}
    /**
     * O email do usuário.
     */
    @NotBlank
    private String email;

    /**
     * A senha do usuário.
     */
    @NotBlank
    private String password;

    /**
     * Retorna o email do usuário.
     * @return o email
     */
    public String getEmail() { return email; }
    /**
     * Define o email do usuário.
     * @param email o email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Retorna a senha do usuário.
     * @return a senha
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define a senha do usuário.
     * @param password a senha
     */
    public void setPassword(String password) {
        this.password = password;
    }
}