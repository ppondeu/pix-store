package dev.ppondeu.java_starter.mappers;

import dev.ppondeu.java_starter.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.dtos.UserResponse;
import dev.ppondeu.java_starter.entities.User;

public class UserMapper {
    public static UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername()
        );
    }

    public static User mapToUser(UserCreateDTO userCreateDTO) {
        return new User(
                userCreateDTO
        );
    }
}
