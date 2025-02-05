package dev.ppondeu.java_starter.common.services;

import dev.ppondeu.java_starter.common.interfaces.ICookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService implements ICookieService {
    @Override
    public String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge, boolean isSecure, boolean isHttpOnly) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setSecure(isSecure);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }

    @Override
    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
