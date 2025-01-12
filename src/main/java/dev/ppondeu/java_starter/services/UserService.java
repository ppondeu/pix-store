package dev.ppondeu.java_starter.services;

import dev.ppondeu.java_starter.common.exceptions.BadRequestException;
import dev.ppondeu.java_starter.common.exceptions.NotFoundException;
import dev.ppondeu.java_starter.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.dtos.UserUpdateDTO;
import dev.ppondeu.java_starter.entities.User;
import dev.ppondeu.java_starter.interfaces.IUserRepository;
import dev.ppondeu.java_starter.interfaces.IUserService;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Caching(
            evict = {@CacheEvict(value = "users", allEntries = true)}
    )
    @Override
    public User createUser(UserCreateDTO userCreateDTO) {
        CompletableFuture<Boolean> usernameExistsFuture = CompletableFuture.supplyAsync(() ->
            this.userRepository.findByUsername(userCreateDTO.getUsername()).isPresent()
        );

        CompletableFuture<Boolean> emailExistsFuture = CompletableFuture.supplyAsync(() ->
                this.userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()
        );

        Boolean usernameExists = usernameExistsFuture.join();
        if (usernameExists) {
            throw new BadRequestException("Username already exists");
        }

        Boolean emailExists = emailExistsFuture.join();
        if (emailExists) {
            throw new BadRequestException("Email already exists");
        }

        var userObj = new User(userCreateDTO);
        return this.userRepository.save(userObj);
    }

    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    @Override
    public User getUserById(UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user with id: " + id + " not found"));
    }

    @Cacheable(value = "users", unless = "#result.isEmpty()")
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @CachePut(value = "users", key="#id")
    @Override
    public User updateUser(UUID id, UserUpdateDTO userUpdateDTO) {
        var user = this.userRepository.findById(id).orElseThrow(() -> new NotFoundException("user with id: " + id + " not found"));

        if (userUpdateDTO.getUsername() != null) {
            var existUser = this.userRepository.findByUsername(userUpdateDTO.getUsername());
            if (existUser.isPresent() && existUser.get().getId() != id) {
                throw new BadRequestException("username: " + userUpdateDTO.getUsername() + " already exists");
            }
            user.setUsername(userUpdateDTO.getUsername());
        }

        if (userUpdateDTO.getEmail() != null) {
            var existUser = this.userRepository.findByEmail(userUpdateDTO.getEmail());
            if (existUser.isPresent() && existUser.get().getId() != id) {
                throw new BadRequestException("email: " + userUpdateDTO.getEmail() + " already exists");
            }
            user.setEmail(userUpdateDTO.getEmail());
        }

        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(userUpdateDTO.getPassword());
        }

        if (userUpdateDTO.getFirstName() != null) {
            user.setFirstName(userUpdateDTO.getFirstName());
        }

        if (userUpdateDTO.getLastName() != null) {
            user.setLastName(userUpdateDTO.getLastName());
        }

        return this.userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#id")
    @Override
    public void deleteUser(UUID id) {
        if (!this.userRepository.existsById(id)) {
            throw new NotFoundException("user with id: " + id + " not found");
        }

        this.userRepository.deleteById(id);
    }

}
