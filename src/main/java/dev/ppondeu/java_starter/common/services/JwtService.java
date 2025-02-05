package dev.ppondeu.java_starter.common.services;

import dev.ppondeu.java_starter.common.exceptions.UnauthorizedException;
import dev.ppondeu.java_starter.common.interfaces.IJwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import javax.crypto.SecretKey;

@Service
public class JwtService implements IJwtService {
    @Value("${application.security.jwt.access.key}")
    private String accessSecretKey;

    @Value("${application.security.jwt.access.expires}")
    private long accessSecretExpires;

    @Value("${application.security.jwt.refresh.key}")
    private String refreshSecretKey;

    @Value("${application.security.jwt.refresh.expires}")
    private long refreshSecretExpires;

    private SecretKey getSecretKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    @Override
    public String GenerateToken(Claims claims, boolean isAccess) {
        Date now = new Date();
        Date expiryDate = isAccess
                ? new Date(System.currentTimeMillis() + accessSecretExpires)
                : new Date(System.currentTimeMillis() + refreshSecretExpires);
        SecretKey secretKey = getSecretKey(isAccess ? accessSecretKey : refreshSecretKey);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims ValidateToken(String token, boolean isAccess) {
        SecretKey secretKey = getSecretKey(isAccess ? accessSecretKey : refreshSecretKey);

        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return null;
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }

    }

}
