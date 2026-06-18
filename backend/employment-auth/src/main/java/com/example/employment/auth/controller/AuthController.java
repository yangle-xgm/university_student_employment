package com.example.employment.auth.controller;

import com.example.employment.auth.dto.LoginRequest;
import com.example.employment.auth.dto.LoginResponse;
import com.example.employment.auth.dto.RegisterRequest;
import com.example.employment.auth.service.AuthService;
import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.common.entity.User;
import com.example.employment.common.repository.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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

    @PostMapping("/reset-password")
    @Operation(summary = "重置密码", description = "重置用户密码")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam String username, @RequestParam String newPassword) {
        try {
            User user = userMapper.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            user.setPassword(passwordEncoder.encode(newPassword));
            int result = userMapper.updateById(user);
            if (result > 0) {
                return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
            } else {
                return ResponseEntity.ok(ApiResponse.error("密码重置失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("重置失败: " + e.getMessage()));
        }
    }

    @GetMapping("/test-password")
    @Operation(summary = "测试密码", description = "测试密码验证")
    public ResponseEntity<ApiResponse<String>> testPassword(@RequestParam String username, @RequestParam String password) {
        try {
            User user = userMapper.findByUsername(username).orElse(null);
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }
            String storedPassword = user.getPassword();
            boolean matches = passwordEncoder.matches(password, storedPassword);

            // 打印调试信息
            System.out.println("=== 密码验证调试 ===");
            System.out.println("用户名: " + username);
            System.out.println("输入密码: " + password);
            System.out.println("存储的密码哈希: " + storedPassword);
            System.out.println("密码长度: " + storedPassword.length());
            System.out.println("匹配结果: " + matches);
            System.out.println("===================");

            if (matches) {
                return ResponseEntity.ok(ApiResponse.success("密码匹配"));
            } else {
                return ResponseEntity.ok(ApiResponse.error("密码不匹配"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error("测试失败: " + e.getMessage()));
        }
    }

    @GetMapping("/encode-password")
    @Operation(summary = "生成密码哈希", description = "生成密码的BCrypt哈希值")
    public ResponseEntity<ApiResponse<String>> encodePassword(@RequestParam String password) {
        try {
            String hash = passwordEncoder.encode(password);
            boolean matches = passwordEncoder.matches(password, hash);
            System.out.println("=== 密码哈希生成 ===");
            System.out.println("输入密码: " + password);
            System.out.println("生成的哈希: " + hash);
            System.out.println("验证匹配: " + matches);
            System.out.println("====================");

            String result = "Hash: " + hash + " | Verify: " + matches;
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error("生成失败: " + e.getMessage()));
        }
    }
}