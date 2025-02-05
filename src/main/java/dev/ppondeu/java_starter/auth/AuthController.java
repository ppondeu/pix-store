package dev.ppondeu.java_starter.auth;

import dev.ppondeu.java_starter.auth.dtos.AuthResponse;
import dev.ppondeu.java_starter.auth.dtos.LoginDTO;
import dev.ppondeu.java_starter.auth.interfaces.IAuthService;
import dev.ppondeu.java_starter.common.dtos.APIResponse;
import dev.ppondeu.java_starter.common.exceptions.UnauthorizedException;
import dev.ppondeu.java_starter.common.interfaces.ICookieService;
import dev.ppondeu.java_starter.users.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.users.entities.User;
import jakarta.servlet.http.Cookie;
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
        var response = authService.login(loginDTO);
//        Cookie accessTokenCookie = new Cookie("access_token", response.getToken().getAccessToken());
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(accessTokenMaxAge);
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setSecure(true);
//        Response.addCookie(accessTokenCookie);
//        Cookie refreshTokenCookie = new Cookie("refresh_token", response.getToken().getRefreshToken());
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(refreshTokenMaxAge);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true);
//        Response.addCookie(refreshTokenCookie);

        cookieService.addCookie(httpResponse, "access_token", response.getToken().getAccessToken(), accessTokenMaxAge, true, true);
        cookieService.addCookie(httpResponse, "refresh_token", response.getToken().getRefreshToken(), refreshTokenMaxAge, true, true);
        APIResponse<AuthResponse> apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "Login successfully.",
                Collections.emptyList(),
                response
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<APIResponse<AuthResponse>> Register(@Valid @RequestBody UserCreateDTO userCreateDTO, HttpServletResponse httpResponse) {
        var response = authService.register(userCreateDTO);
//        Cookie accessTokenCookie = new Cookie("access_token", response.getToken().getAccessToken());
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(accessTokenMaxAge);
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setSecure(true);
//        Response.addCookie(accessTokenCookie);
//        Cookie refreshTokenCookie = new Cookie("refresh_token", response.getToken().getRefreshToken());
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(refreshTokenMaxAge);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true);
//        Response.addCookie(refreshTokenCookie);
        cookieService.addCookie(httpResponse, "access_token", response.getToken().getAccessToken(), accessTokenMaxAge, true, true);
        cookieService.addCookie(httpResponse, "refresh_token", response.getToken().getRefreshToken(), refreshTokenMaxAge, true, true);


        APIResponse<AuthResponse> apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "Register successfully.",
                Collections.emptyList(),
                response
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
        System.out.println("refresh token" + refreshToken);
        if (refreshToken == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        System.out.println("refresh token" + refreshToken);
        System.out.println("user id" + user.getId());

        var response = authService.refreshToken(user.getId(), refreshToken);
//        Cookie accessTokenCookie = new Cookie("access_token", response.getToken().getAccessToken());
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(accessTokenMaxAge);
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setSecure(true);
//        Response.addCookie(accessTokenCookie);
//
//        Cookie refreshTokenCookie = new Cookie("refresh_token", response.getToken().getRefreshToken());
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(refreshTokenMaxAge);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true);
//        Response.addCookie(refreshTokenCookie);

        cookieService.addCookie(httpResponse, "access_token", response.getToken().getAccessToken(), accessTokenMaxAge, true, true);
        cookieService.addCookie(httpResponse, "refresh_token", response.getToken().getRefreshToken(), refreshTokenMaxAge, true, true);

        APIResponse<AuthResponse> apiResponse = new APIResponse<>(
                HttpStatus.OK.value(),
                "Refresh Token Succecssfully.",
                Collections.emptyList(),
                response
        );
        return new ResponseEntity<APIResponse<AuthResponse>>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> Logout(HttpServletRequest Request, HttpServletResponse httpResponse) {
//        get user from context
        var user = Request.getAttribute("user");
        System.out.println("user" + user);
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }
//        authService.logout(UUID.randomUUID(), "123");
//        Cookie accessTokenCookie = new Cookie("access_token", null);
//        accessTokenCookie.setMaxAge(0);
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setSecure(true);
//        Response.addCookie(accessTokenCookie);
//
//        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
//        refreshTokenCookie.setMaxAge(0);
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true);
//        Response.addCookie(refreshTokenCookie);

//        Cookie jsessionid = new Cookie("JSESSIONID", null);
//        jsessionid.setMaxAge(0);
//        jsessionid.setPath("/");
//        jsessionid.setHttpOnly(true);
//        jsessionid.setSecure(true);
//        Response.addCookie(jsessionid);

        cookieService.deleteCookie(httpResponse, "access_token");
        cookieService.deleteCookie(httpResponse, "refresh_token");
        cookieService.deleteCookie(httpResponse, "JSESSIONID");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
