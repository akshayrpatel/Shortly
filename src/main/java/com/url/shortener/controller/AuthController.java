package com.url.shortener.controller;

import com.url.shortener.dto.LoginRequest;
import com.url.shortener.dto.LoginResponse;
import com.url.shortener.dto.RegisterRequest;
import com.url.shortener.model.User;
import com.url.shortener.security.JwtAuthenticationResponse;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    @PostMapping("/public/register")
    public ResponseEntity<? extends User> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/public/login")
    public ResponseEntity<? extends LoginResponse> loginUser(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }
}
