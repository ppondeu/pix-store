package dev.ppondeu.java_starter.users.interfaces;

import dev.ppondeu.java_starter.users.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.users.dtos.UserUpdateDTO;
import dev.ppondeu.java_starter.users.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    public User createUser(UserCreateDTO dto);
    public User getUserById(UUID id);
    public User getUserByEmail(String email);
    public User getUserByUsername(String username);

    @EntityGraph(attributePaths = {"pictures"})
    public List<User> getUsers();

    public User updateUser(UUID id, UserUpdateDTO dto);
    public void deleteUser(UUID id);

    public void updateRefreshToken(UUID id, String refreshToken);
}
