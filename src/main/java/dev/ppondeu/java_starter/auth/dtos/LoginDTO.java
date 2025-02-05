package dev.ppondeu.java_starter.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
    @NotBlank(message = "email is required")
    @Email(message = "email is invalid format")
    private final String email;

    @NotBlank(message = "password is required")
    private String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public @NotBlank(message = "email is required") @Email(message = "email is invalid format") String getEmail() {
        return email;
    }

    public @NotBlank(message = "password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "password is required") String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
