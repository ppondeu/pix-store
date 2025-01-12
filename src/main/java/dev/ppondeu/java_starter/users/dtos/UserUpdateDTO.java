package dev.ppondeu.java_starter.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class UserUpdateDTO {
    @Length(min=3, max=64, message = "username length must be between 3 and 64")
    @Pattern(regexp = "^(?=[a-zA-Z0-9._]*[a-zA-Z])[a-zA-Z0-9._]+$", message = "Username must contain only a-z, A-Z, 0-9, ., _")
    private final String username;

    @Email(message = "email is invalid format")
    private final String email;

    private String password;

    private final String firstName;

    private final String lastName;

    public UserUpdateDTO(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName == null ? "" : firstName.strip();
        this.lastName = lastName == null ? "" : lastName.strip();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void SetPassword(String password) {
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
        return "UserUpdateDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
