package com.example.employment.auth.service;

import com.example.employment.auth.dto.LoginRequest;
import com.example.employment.auth.dto.LoginResponse;
import com.example.employment.auth.dto.RegisterRequest;
import com.example.employment.common.dto.response.ApiResponse;

public interface AuthService {
    ApiResponse<LoginResponse> login(LoginRequest request);
    ApiResponse<String> register(RegisterRequest request);
    ApiResponse<LoginResponse> refreshToken(String refreshToken);
}