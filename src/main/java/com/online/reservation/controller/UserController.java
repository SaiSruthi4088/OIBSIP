package com.online.reservation.controller;

import com.online.reservation.model.User;
import com.online.reservation.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if (existing.isPresent() &&
            existing.get().getPassword().equals(user.getPassword())) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }
}
