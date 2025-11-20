package com.hortifruti.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Serviço para criação e validação de JSON Web Tokens (JWT).
 */
@Service
public class JwtService {
    private final Key key;
    private final long accessExpSeconds;
    private final long refreshExpSeconds;

    /**
     * Construtor que inicializa o serviço com as configurações de JWT.
     * @param secret A chave secreta para assinar os tokens
     * @param accessExpSeconds O tempo de expiração do token de acesso em segundos
     * @param refreshExpSeconds O tempo de expiração do token de atualização em segundos
     */
    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access.exp:900}") long accessExpSeconds,
            @Value("${app.jwt.refresh.exp:604800}") long refreshExpSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessExpSeconds = accessExpSeconds;
        this.refreshExpSeconds = refreshExpSeconds;
    }

    /**
     * Gera um token de acesso.
     * @param subject O "subject" do token (geralmente o ID do usuário)
     * @param claims Claims adicionais a serem incluídos no token
     * @return O token de acesso JWT
     */
    public String generateAccessToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessExpSeconds)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Gera um token de atualização.
     * @param subject O "subject" do token (geralmente o ID do usuário)
     * @param jti O ID único do token (JWT ID)
     * @return O token de atualização JWT
     */
    public String generateRefreshToken(String subject, String jti) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject)
                .setId(jti)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(refreshExpSeconds)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Analisa um token JWT e retorna seus claims.
     * @param token O token JWT a ser analisado
     * @return Os claims contidos no token
     */
    public Claims parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
