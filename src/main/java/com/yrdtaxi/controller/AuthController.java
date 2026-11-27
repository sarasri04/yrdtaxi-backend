package com.yrdtaxi.controller;


import com.yrdtaxi.dto.LoginRequest;
import com.yrdtaxi.dto.RegisterRequest;
import com.yrdtaxi.model.User;
import com.yrdtaxi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepo.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        if (request.getRole() == null || request.getRole().isEmpty()) {
            user.setRole("CUSTOMER");
        } else {
            user.setRole(request.getRole());
        }

        User savedUser = userRepo.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Register successful");
        response.put("id", savedUser.getId());
        response.put("name", savedUser.getName());
        response.put("email", savedUser.getEmail());
        response.put("role", savedUser.getRole());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("token", "dummy-token");

        return ResponseEntity.ok(response);
    }
}


