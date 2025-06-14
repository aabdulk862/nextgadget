package com.nextgadget.user.service;

import com.nextgadget.user.entity.User;
import com.nextgadget.user.repository.UserRepository;
import com.nextgadget.user.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return Optional.empty(); // username taken
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default role on registration
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("ROLE_USER");
        }
        userRepository.save(user);

        return Optional.of("User registered successfully");
    }

    public Optional<String> loginUser(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return Optional.empty();
        }

        // If you want to store a single role as string in the token:
        String role = user.getRole();  // assuming this is a single string like "ROLE_ADMIN"

        // If user.getRole() is comma-separated and you want to pick one role or handle multiple,
        // you could adjust this accordingly. For now, just take the first role:
        // String role = user.getRole().split(",")[0].trim();

        // Generate token with the single role string
        String token = jwtUtil.generateToken(user.getUsername(), role);

        return Optional.of(token);
    }
}
