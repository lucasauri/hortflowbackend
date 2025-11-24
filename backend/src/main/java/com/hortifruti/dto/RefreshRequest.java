package com.hortifruti.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object para requisição de atualização de token.
 */
public class RefreshRequest {
    /**
     * Construtor padrão.
     */
    public RefreshRequest() {
    }
    @NotBlank
    private String refreshToken;

    /**
     * Retorna o token de atualização.
     * @return O token de atualização.
     */
    public String getRefreshToken() { return refreshToken; }

    /**
     * Define o token de atualização.
     * @param refreshToken O token de atualização.
     */
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
