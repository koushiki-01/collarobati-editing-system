package com.collab.user.controller;

import com.collab.user.dto.UserRegisterRequest;
import com.collab.user.dto.UserLoginRequest;
import com.collab.user.dto.UserResponse;
import com.collab.user.entity.User;
import com.collab.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());
        User saved = userService.register(user);
        return new ResponseEntity<>(new UserResponse(saved.getUsername(), saved.getEmail()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        Optional<User> user = userService.login(request.getUsername(), request.getPassword());
        return user.isPresent()
                ? ResponseEntity.ok("Login successful")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable String username) {
        return userService.getProfile(username)
                .map(u -> ResponseEntity.ok(new UserResponse(u.getUsername(), u.getEmail())))
                .orElse(ResponseEntity.notFound().build());
    }
}
