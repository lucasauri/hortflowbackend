package com.hortifruti.dto;

/**
 * Data Transfer Object para resposta de login.
 */
public class LoginResponse {
    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;
    private UserDto user;

    /**
     * Construtor padrão.
     */
    public LoginResponse() {}

    /**
     * Construtor com todos os campos.
     * @param success Indica se o login foi bem-sucedido.
     * @param message Mensagem sobre o resultado do login.
     * @param accessToken Token de acesso.
     * @param refreshToken Token de atualização.
     * @param user Dados do usuário.
     */
    public LoginResponse(boolean success, String message, String accessToken, String refreshToken, UserDto user) {
        this.success = success;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    /**
     * Verifica se o login foi bem-sucedido.
     * @return true se o login foi bem-sucedido, false caso contrário.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Define se o login foi bem-sucedido.
     * @param success true se o login foi bem-sucedido, false caso contrário.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Retorna a mensagem sobre o resultado do login.
     * @return A mensagem sobre o resultado do login.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Define a mensagem sobre o resultado do login.
     * @param message A mensagem sobre o resultado do login.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retorna o token de acesso.
     * @return O token de acesso.
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Define o token de acesso.
     * @param accessToken O token de acesso.
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Retorna o token de atualização.
     * @return O token de atualização.
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Define o token de atualização.
     * @param refreshToken O token de atualização.
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Retorna os dados do usuário.
     * @return Os dados do usuário.
     */
    public UserDto getUser() {
        return user;
    }

    /**
     * Define os dados do usuário.
     * @param user Os dados do usuário.
     */
    public void setUser(UserDto user) {
        this.user = user;
    }
}