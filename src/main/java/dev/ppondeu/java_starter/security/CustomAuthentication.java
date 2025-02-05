package dev.ppondeu.java_starter.security;

import dev.ppondeu.java_starter.users.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

public class CustomAuthentication implements Authentication {

    private final User user;

    public CustomAuthentication(User user) {
        super();
        this.user = user;
        setAuthenticated(false);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null; // You can return user credentials here if needed
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user; // This is the custom user object
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object another) {
        return false;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getName() {
        return "";
    }

}
