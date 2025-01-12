package dev.ppondeu.java_starter.users.interfaces;

import dev.ppondeu.java_starter.users.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.users.dtos.UserUpdateDTO;
import dev.ppondeu.java_starter.users.entities.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    public User createUser(UserCreateDTO dto);
    public User getUserById(UUID id);
    public List<User> getUsers();
    public User updateUser(UUID id, UserUpdateDTO dto);
    public void deleteUser(UUID id);
}
