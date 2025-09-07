package com.example.user.controller;

import com.example.user.dto.AuthRequest;
import com.example.user.dto.RegisterRequest;
import com.example.user.model.User;
import com.example.user.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a user. Returns created user (password hidden).
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User created = userService.register(request);
            return ResponseEntity.ok(created);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(409).body(ex.getMessage());
        }
    }

    /**
     * Simple login - verifies credentials. Returns user (password hidden) on success.
     * (No JWT issued in this version.)
     */
   
    @PostMapping("/login")
public ResponseEntity<User> login(@Valid @RequestBody AuthRequest request) {
    User user = userService.authenticate(request.getEmail(), request.getPassword())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    return ResponseEntity.ok(user);
}


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
