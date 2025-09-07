package com.example.user.service;

import com.example.user.dto.RegisterRequest;
import com.example.user.model.User;
import com.example.user.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User register(RegisterRequest req) {
        String email = req.getEmail().trim().toLowerCase();
        String username = req.getUsername().trim();

        if (userRepo.existsByEmail(email)) {
            throw new IllegalStateException("Email already registered");
        }
        if (userRepo.existsByUsername(username)) {
            throw new IllegalStateException("Username already taken");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .build();

        return userRepo.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> authenticate(String email, String rawPassword) {
        return userRepo.findByEmail(email.trim().toLowerCase())
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPasswordHash()));
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }
}
