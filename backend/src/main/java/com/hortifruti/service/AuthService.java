package com.hortifruti.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${app.auth.username:admin}")
    private String configuredUsername;

    @Value("${app.auth.password:admin}")
    private String configuredPassword;

    public boolean validateCredentials(String username, String password) {
        return configuredUsername.equals(username) && configuredPassword.equals(password);
    }

    public String issuePlaceholderToken(String username) {
        // Placeholder de token até JWT ser implementado
        return "token-" + username + "-placeholder";
    }
}