package dev.ppondeu.java_starter.filters;

import dev.ppondeu.java_starter.common.dtos.APIResponse;
import dev.ppondeu.java_starter.common.interfaces.IJwtService;
import dev.ppondeu.java_starter.users.interfaces.IUserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AccessTokenFilter implements Filter {

    private final IJwtService jwtService;
    private final IUserService userService;

    public AccessTokenFilter(IJwtService jwtService, IUserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        System.out.println("Access Filter: " + httpRequest.getRequestURI());

        String accessToken = getAccessToken(httpRequest);
        if (accessToken == null) {
            sendUnauthorizedResponse(httpResponse, "No access token found");
            return;
        }

        Claims claims = jwtService.ValidateToken(accessToken, true);
        if (claims == null) {
            sendUnauthorizedResponse(httpResponse, "Invalid token");
            return;
        }

        String userId = claims.getSubject();
        if (userId == null) {
            sendUnauthorizedResponse(httpResponse, "No user ID found in token");
            return;
        }

        var user = userService.getUserById(UUID.fromString(userId));
        if (user == null) {
            sendUnauthorizedResponse(httpResponse, "User not found");
            return;
        }

        httpRequest.setAttribute("user", user);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        List<String> errors = Arrays.asList(message);

        APIResponse<?> apiResponse = new APIResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                errors,
                null
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(apiResponse.toJson());
    }
}
