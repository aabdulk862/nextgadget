package com.nextgadget.user.controller;

import com.nextgadget.user.dto.LoginRequest;
import com.nextgadget.user.dto.RegisterRequest;
import com.nextgadget.user.entity.User;
import com.nextgadget.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return userService.registerUser(user)
                .map(msg -> ResponseEntity.ok(msg))
                .orElseGet(() -> ResponseEntity.badRequest().body("Username is already taken"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword())
                .map(token -> ResponseEntity.ok(token))
                .orElseGet(() -> ResponseEntity.status(401).body("Invalid username or password"));
    }
}
