package dev.ppondeu.java_starter.users.mappers;


import dev.ppondeu.java_starter.users.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.users.dtos.UserResponse;
import dev.ppondeu.java_starter.users.entities.User;

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
