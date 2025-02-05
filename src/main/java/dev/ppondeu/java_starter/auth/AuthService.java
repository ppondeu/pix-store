package dev.ppondeu.java_starter.auth;

import dev.ppondeu.java_starter.auth.dtos.AuthResponse;
import dev.ppondeu.java_starter.auth.dtos.LoginDTO;
import dev.ppondeu.java_starter.auth.dtos.TokenResponse;
import dev.ppondeu.java_starter.auth.interfaces.IAuthService;
import dev.ppondeu.java_starter.common.exceptions.BadRequestException;
import dev.ppondeu.java_starter.common.interfaces.IJwtService;
import dev.ppondeu.java_starter.users.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.users.dtos.UserResponse;
import dev.ppondeu.java_starter.users.entities.User;
import dev.ppondeu.java_starter.users.interfaces.IUserService;
import dev.ppondeu.java_starter.users.mappers.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {
    private final IUserService userService;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IUserService userService, IJwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse login(LoginDTO loginDTO) {
        User userExist = userService.getUserByEmail(loginDTO.getEmail());
        if (!Objects.isNull(userExist) && passwordEncoder.matches(loginDTO.getPassword(), userExist.getPassword())) {
            System.out.println(userExist);
            Claims claims = Jwts.claims()
                    .subject(userExist.getId().toString())
                    .build();
            String accessToken = jwtService.GenerateToken(claims, true);
            String refreshToken = jwtService.GenerateToken(claims, false);

//            save refresh token to db
            userService.updateRefreshToken(userExist.getId(), refreshToken);
            TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
            UserResponse userResponse = UserMapper.mapToUserResponse(userExist);
            return new AuthResponse(userResponse, tokenResponse);
        }

        throw new BadRequestException("Invalid Email or Password");
    }

    @Override
    public AuthResponse register(UserCreateDTO userCreateDTO) {
        userCreateDTO.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        User user = userService.createUser(userCreateDTO);
        Claims claims = Jwts.claims()
                .subject(user.getId().toString())
                .build();
        String accessToken = jwtService.GenerateToken(claims, true);
        String refreshToken = jwtService.GenerateToken(claims, false);
//            save refresh token to db
        userService.updateRefreshToken(user.getId(), refreshToken);
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        UserResponse userResponse = UserMapper.mapToUserResponse(user);
        return new AuthResponse(userResponse, tokenResponse);
    }

    @Override
    public void logout(UUID userId, String refreshToken) {
        var userExist = userService.getUserById(userId);
        if (Objects.isNull(userExist) || !userExist.getRefreshToken().equals(refreshToken)) {
            throw new BadRequestException("Invalid Refresh Token");
        }

        userService.updateRefreshToken(userId, null);
    }

    @Override
    public AuthResponse refreshToken(UUID userId, String refreshToken) {
        var userExist = userService.getUserById(userId);
        System.out.println("User" + userExist);
        System.out.println("Refresh Token" + refreshToken);
        System.out.println("User Refresh Token" + userExist.getRefreshToken());
        if (Objects.isNull(userExist) || !userExist.getRefreshToken().equals(refreshToken)) {
            throw new BadRequestException("Invalid Refresh Token");
        }
        Claims claims = Jwts.claims()
                .subject(userExist.getId().toString())
                .build();
        String accessToken = jwtService.GenerateToken(claims, true);
        String newRefreshToken = jwtService.GenerateToken(claims, false);
//            save refresh token to db
        userService.updateRefreshToken(userExist.getId(), newRefreshToken);
        TokenResponse tokenResponse = new TokenResponse(accessToken, newRefreshToken);
        UserResponse userResponse = UserMapper.mapToUserResponse(userExist);
        return new AuthResponse(userResponse, tokenResponse);
    }

}
