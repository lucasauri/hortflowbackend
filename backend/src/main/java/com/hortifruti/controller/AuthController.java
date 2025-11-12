package com.hortifruti.controller;

import com.hortifruti.dto.LoginRequest;
import com.hortifruti.dto.LoginResponse;
import com.hortifruti.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        boolean ok = authService.validateCredentials(request.getUsername(), request.getPassword());
        if (ok) {
            String token = authService.issuePlaceholderToken(request.getUsername());
            return ResponseEntity.ok(new LoginResponse(true, "Login efetuado", token));
        }
        return ResponseEntity.status(401).body(new LoginResponse(false, "Credenciais inválidas", null));
    }
}