package dev.ppondeu.java_starter.common.interfaces;

import io.jsonwebtoken.Claims;

public interface IJwtService {
    public String GenerateToken(Claims claims, boolean isAccess);
    public Claims ValidateToken(String token, boolean isAccess);
}
