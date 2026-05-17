package com.servitech.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:ServiTechSecretKeyForJWTAuth2026!}")
    private String secret;

    @Value("${jwt.expiration-ms:3600000}")
    private long expirationMs; // 1 hora

    @Value("${jwt.refresh-expiration-ms:86400000}")
    private long refreshExpirationMs; // 24 horas

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(String usuario, String rol) {
        return Jwts.builder()
                .setSubject(usuario)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generarRefreshToken(String usuario) {
        return Jwts.builder()
                .setSubject(usuario)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .claim("tipo", "refresh")
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean esTokenAccesoValido(String token) {
        Claims claims = obtenerClaims(token);
        if (claims == null) return false;
        return claims.get("tipo") == null;
    }

    public boolean esRefreshTokenValido(String token) {
        Claims claims = obtenerClaims(token);
        if (claims == null) return false;
        return "refresh".equals(claims.get("tipo"));
    }

    public String obtenerUsuarioDelToken(String token) {
        Claims claims = obtenerClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String obtenerRolDelToken(String token) {
        Claims claims = obtenerClaims(token);
        return claims != null ? (String) claims.get("rol") : null;
    }

    private Claims obtenerClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
