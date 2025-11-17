package com.hortifruti.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;
    private UserDto user;

    public LoginResponse() {}

    public LoginResponse(boolean success, String message, String accessToken, String refreshToken, UserDto user) {
        this.success = success;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() { 
        return accessToken; 
    }
    public void setAccessToken(String accessToken) { 
        this.accessToken = accessToken; 
    }
    public String getRefreshToken() { 
        return refreshToken; 
    }
    public void setRefreshToken(String refreshToken) { 
        this.refreshToken = refreshToken; 
    }
    public UserDto getUser() { 
        return user; 
    }
    public void setUser(UserDto user) { 
        this.user = user; 
    }
}