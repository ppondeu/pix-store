package dev.ppondeu.java_starter.controllers;

import dev.ppondeu.java_starter.dtos.UserCreateDTO;
import dev.ppondeu.java_starter.dtos.UserResponse;
import dev.ppondeu.java_starter.dtos.UserUpdateDTO;
import dev.ppondeu.java_starter.interfaces.IUserService;
import dev.ppondeu.java_starter.mappers.UserMapper;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        var users = this.userService.getUsers();

        List<UserResponse> response = users.stream()
                .map(UserMapper::mapToUserResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") UUID id) {
        var user = this.userService.getUserById(id);
        var response = UserMapper.mapToUserResponse(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponse> addUser(@Validated @RequestBody UserCreateDTO userCreateDTO) {
        var user = this.userService.createUser(userCreateDTO);
        var response = UserMapper.mapToUserResponse(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") UUID id, @Validated @RequestBody UserUpdateDTO userUpdateDTO) {
        System.out.println(userUpdateDTO);
        System.out.println("ID : " + id);
        var user = this.userService.updateUser(id, userUpdateDTO);
        var response = UserMapper.mapToUserResponse(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        this.userService.deleteUser(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }
}
