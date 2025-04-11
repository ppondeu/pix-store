package dev.ppondeu.java_starter.auth;

import dev.ppondeu.java_starter.auth.dtos.AuthResponse;
import dev.ppondeu.java_starter.auth.dtos.LoginDTO;
import dev.ppondeu.java_starter.auth.interfaces.IAuthService;
import dev.ppondeu.java_starter.common.dtos.APIResponse;
import dev.ppondeu.java_starter.common.exceptions.UnauthorizedException;
import dev.ppondeu.java_starter.common.interfaces.ICookieService;
import dev.ppondeu.java_starter.users.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.users.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${application.security.cookie.access-token.maxAge}")
    private int accessTokenMaxAge;

    @Value("${application.security.cookie.refresh-token.maxAge}")
    private int refreshTokenMaxAge;

    private final IAuthService authService;
    private final ICookieService cookieService;
    public AuthController(IAuthService authService, ICookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @PostMapping("login")
    public ResponseEntity<APIResponse<AuthResponse>> Login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse httpResponse) {
        System.out.println("access token maxAge: " + accessTokenMaxAge);
        System.out.println("refresh token maxAge: " + refreshTokenMaxAge);
        var result = authService.login(loginDTO);

        cookieService.addCookie(httpResponse, "access_token", result.getToken().getAccessToken(), accessTokenMaxAge, true, true);
        cookieService.addCookie(httpResponse, "refresh_token", result.getToken().getRefreshToken(), refreshTokenMaxAge, true, true);
        APIResponse<AuthResponse> apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "Login successfully.",
                Collections.emptyList(),
                result
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<APIResponse<AuthResponse>> Register(@Valid @RequestBody UserCreateDTO userCreateDTO, HttpServletResponse httpResponse) {
        var result = authService.register(userCreateDTO);

        cookieService.addCookie(httpResponse, "access_token", result.getToken().getAccessToken(), accessTokenMaxAge, true, true);
        cookieService.addCookie(httpResponse, "refresh_token", result.getToken().getRefreshToken(), refreshTokenMaxAge, true, true);


        APIResponse<AuthResponse> apiResponse = new APIResponse<>(
                HttpStatus.CREATED.value(),
                "Register successfully.",
                Collections.emptyList(),
                result
        );
        return new ResponseEntity<APIResponse<AuthResponse>>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<APIResponse<AuthResponse>> RefreshToken(HttpServletRequest Request, HttpServletResponse httpResponse) {
        var user = (User) Request.getAttribute("user");

        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        var refreshToken = cookieService.getCookie(Request, "refresh_token");
        System.out.println("refresh token from cookie " + refreshToken);
        if (refreshToken == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        System.out.println("user id" + user.getId());

        var result = authService.refreshToken(user.getId(), refreshToken);

        cookieService.addCookie(httpResponse, "access_token", result.getToken().getAccessToken(), accessTokenMaxAge, true, true);
        cookieService.addCookie(httpResponse, "refresh_token", result.getToken().getRefreshToken(), refreshTokenMaxAge, true, true);

        APIResponse<AuthResponse> apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "Refresh Token Succecssfully.",
                Collections.emptyList(),
                result
        );
        return new ResponseEntity<APIResponse<AuthResponse>>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse<?>> Logout(HttpServletRequest Request, HttpServletResponse httpResponse) {
        var user = Request.getAttribute("user");
        System.out.println("user" + user);
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        cookieService.deleteCookie(httpResponse, "access_token");
        cookieService.deleteCookie(httpResponse, "refresh_token");
        cookieService.deleteCookie(httpResponse, "JSESSIONID");

        var apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "logged user out successfully.",
                Collections.emptyList(),
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
