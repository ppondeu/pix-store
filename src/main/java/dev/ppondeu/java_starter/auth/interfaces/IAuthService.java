package dev.ppondeu.java_starter.auth.interfaces;

import dev.ppondeu.java_starter.auth.dtos.AuthResponse;
import dev.ppondeu.java_starter.auth.dtos.LoginDTO;
import dev.ppondeu.java_starter.auth.dtos.TokenResponse;
import dev.ppondeu.java_starter.users.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.users.entities.User;

import java.util.UUID;

public interface IAuthService {
    public AuthResponse login(LoginDTO loginDTO);
    public AuthResponse register(UserCreateDTO userCreateDTO);
    public void logout(UUID userId, String refreshToken);
    public AuthResponse refreshToken(UUID userId, String refreshToken);
}
