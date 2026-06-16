package com.example.employment.auth.controller;

import com.example.employment.auth.dto.LoginRequest;
import com.example.employment.auth.dto.LoginResponse;
import com.example.employment.auth.dto.RegisterRequest;
import com.example.employment.auth.service.AuthService;
import com.example.employment.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户注册、登录、令牌刷新")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "通过用户名和密码登录系统")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户账号")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}