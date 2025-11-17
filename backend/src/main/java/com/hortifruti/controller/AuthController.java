package com.hortifruti.controller;

import com.hortifruti.dto.LoginRequest;
import com.hortifruti.dto.LoginResponse;
import com.hortifruti.dto.RefreshRequest;
import com.hortifruti.dto.UserDto;
import com.hortifruti.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

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
     * @param request Credenciais de login (email e password)
     * @return Resposta com token de autenticação ou erro
     */
    @Operation(summary = "Realizar login", description = "Autentica um usuário e retorna um token de acesso")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var userOpt = authService.authenticate(request);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(new LoginResponse(false, "Credenciais inválidas", null, null, null));
        }
        var user = userOpt.get();
        var tokens = authService.issueTokens(user, "", "");
        var dto = AuthService.toDto(user);
        return ResponseEntity.ok(new LoginResponse(true, "Login efetuado", tokens.accessToken(), tokens.refreshToken(), dto));
    }

    @Operation(summary = "Usuário atual", description = "Retorna o perfil do usuário autenticado")
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        var userId = java.util.UUID.fromString(authentication.getName());
        return authService.findUserById(userId)
                .map(AuthService::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @Operation(summary = "Renovar access token", description = "Gera um novo access token a partir de um refresh token válido")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        var userOpt = authService.validateRefresh(request.getRefreshToken());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(new LoginResponse(false, "Refresh inválido", null, null, null));
        }
        var user = userOpt.get();
        var tokens = authService.issueTokens(user, "", "");
        var dto = AuthService.toDto(user);
        return ResponseEntity.ok(new LoginResponse(true, "Token renovado", tokens.accessToken(), tokens.refreshToken(), dto));
    }

    @Operation(summary = "Logout", description = "Revoga refresh tokens do usuário atual")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        var userId = java.util.UUID.fromString(authentication.getName());
        var user = authService.findUserById(userId);
        user.ifPresent(authService::logoutAll);
        return ResponseEntity.noContent().build();
    }
}