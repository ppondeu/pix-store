package dev.ppondeu.java_starter.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserCreateDTO {
    @NotBlank(message = "username is required")
    @Length(min=3, max=64, message = "username length must be between 3 and 64")
    private final String username;

    @NotBlank(message = "email is required")
    @Email(message = "email is invalid format")
    private final String email;

    @NotBlank(message = "password is required")
    private String password;

    private final String firstName;

    private final String lastName;

    public UserCreateDTO(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public @NotBlank(message = "username is required") @Length(min = 3, max = 64, message = "username length must be between 3 and 64") String getUsername() {
        return username;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "UserCreateDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
