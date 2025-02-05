package dev.ppondeu.java_starter.auth.dtos;

import dev.ppondeu.java_starter.users.dtos.UserResponse;

public class AuthResponse {
    private final UserResponse user;

    private final TokenResponse token;

    public AuthResponse(UserResponse user, TokenResponse token) {
        this.user = user;
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public TokenResponse getToken() {
        return token;
    }
}
