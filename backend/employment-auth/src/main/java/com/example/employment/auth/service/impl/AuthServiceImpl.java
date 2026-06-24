package com.example.employment.auth.service.impl;

import com.example.employment.auth.dto.LoginRequest;
import com.example.employment.auth.dto.LoginResponse;
import com.example.employment.auth.dto.RegisterRequest;
import com.example.employment.auth.service.AuthService;
import com.example.employment.auth.utils.JwtUtil;
import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.common.entity.User;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatch) {
            throw new BusinessException("用户名或密码错误");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException("用户账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        LoginResponse response = LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .refreshToken(refreshToken)
                .build();

        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<String> register(RegisterRequest request) {
        if (userMapper.countByUsername(request.getUsername()) > 0) {
            throw new BusinessException("用户名已存在");
        }

        if (request.getEmail() != null && userMapper.countByEmail(request.getEmail()) > 0) {
            throw new BusinessException("邮箱已被使用");
        }

        String role = request.getRole().toUpperCase();
        if (!role.equals("STUDENT") && !role.equals("HR") && !role.equals("ADMIN")) {
            throw new BusinessException("无效的角色类型");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(role)
                .status("ACTIVE")
                .build();

        userMapper.insert(user);

        return ApiResponse.success("注册成功");
    }

    @Override
    public ApiResponse<LoginResponse> refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException("无效的刷新令牌");
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        String newToken = jwtUtil.generateToken(userId, username, user.getRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, username);

        LoginResponse response = LoginResponse.builder()
                .userId(userId)
                .username(username)
                .email(user.getEmail())
                .role(user.getRole())
                .token(newToken)
                .refreshToken(newRefreshToken)
                .build();

        return ApiResponse.success(response);
    }
}