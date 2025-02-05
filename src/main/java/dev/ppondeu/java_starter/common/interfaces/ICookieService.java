package dev.ppondeu.java_starter.common.interfaces;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ICookieService {
    public String getCookie(HttpServletRequest request, String cookieName);
    void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge, boolean isSecure, boolean isHttpOnly);
    void deleteCookie(HttpServletResponse response, String cookieName);
}
