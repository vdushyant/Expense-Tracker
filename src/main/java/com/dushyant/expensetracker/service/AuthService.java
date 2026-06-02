package com.dushyant.expensetracker.service;

import com.dushyant.expensetracker.dto.AuthResponse;
import com.dushyant.expensetracker.dto.LoginRequest;
import com.dushyant.expensetracker.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}