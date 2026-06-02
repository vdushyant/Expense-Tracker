package com.dushyant.expensetracker.controller;

import com.dushyant.expensetracker.dto.AuthResponse;
import com.dushyant.expensetracker.dto.LoginRequest;
import com.dushyant.expensetracker.dto.RegisterRequest;
import com.dushyant.expensetracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}