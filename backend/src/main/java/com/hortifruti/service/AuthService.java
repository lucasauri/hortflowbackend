package com.hortifruti.service;

import com.hortifruti.dto.LoginRequest;
import com.hortifruti.dto.UserDto;
import com.hortifruti.model.RefreshToken;
import com.hortifruti.model.User;
import com.hortifruti.repository.RefreshTokenRepository;
import com.hortifruti.repository.UserRepository;
import com.hortifruti.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshRepo,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshRepo = refreshRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public record Tokens(String accessToken, String refreshToken) {}

    @Transactional
    public Optional<User> authenticate(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(User::isActive)
                .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPasswordHash()));
    }

    @Transactional
    public Tokens issueTokens(User user, String userAgent, String ip) {
        String subject = user.getId().toString();
        String access = jwtService.generateAccessToken(subject, Map.of(
                "email", user.getEmail(),
                "role", user.getRole()
        ));
        String jti = UUID.randomUUID().toString();
        String refresh = jwtService.generateRefreshToken(subject, jti);

        RefreshToken entity = new RefreshToken();
        entity.setUser(user);
        entity.setTokenHash(hash(refresh));
        entity.setExpiresAt(Instant.now().plusSeconds(604800));
        entity.setUserAgent(userAgent);
        entity.setIp(ip);
        refreshRepo.save(entity);

        return new Tokens(access, refresh);
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Optional<User> validateRefresh(String refreshToken) {
        try {
            var claims = jwtService.parse(refreshToken);
            String jti = claims.getId();
            String sub = claims.getSubject();
            var valid = refreshRepo.findValidByHash(hash(refreshToken), Instant.now());
            if (valid.isEmpty()) return Optional.empty();
            UUID userId = UUID.fromString(sub);
            return userRepository.findById(userId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void logoutAll(User user) {
        refreshRepo.revokeAllForUser(user, Instant.now());
    }

    private String hash(String token) {
        // Para simplificar, usar hash BCrypt também (ou SHA-256) — aqui usamos passwordEncoder
        return passwordEncoder.encode(token);
    }

    public static UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail(), u.getRole());
    }
}