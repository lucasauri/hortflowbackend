package com.hortifruti.controller;

import com.hortifruti.dto.LoginRequest;
import com.hortifruti.dto.LoginResponse;
import com.hortifruti.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para autenticação.
 * 
 * <p>Fornece endpoints para login e autenticação de usuários.
 * 
 * @author Hortifruti Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "API para autenticação de usuários")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Realiza login no sistema.
     * 
     * @param request Credenciais de login (username e password)
     * @return Resposta com token de autenticação ou erro
     */
    @Operation(summary = "Realizar login", description = "Autentica um usuário e retorna um token de acesso")
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