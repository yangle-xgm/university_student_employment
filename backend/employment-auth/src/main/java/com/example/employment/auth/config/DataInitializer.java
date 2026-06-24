package com.example.employment.auth.config;

import com.example.employment.common.entity.User;
import com.example.employment.common.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器 - 确保测试账号的密码可用
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 旧的无效哈希（admin和hr1共享的）
    private static final String OLD_INVALID_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq";

    @Override
    public void run(String... args) {
        resetPasswordIfNeeded("admin", "123456", "管理员");
        resetPasswordIfNeeded("hr1", "123456", "HR");
    }

    private void resetPasswordIfNeeded(String username, String newPassword, String role) {
        try {
            User user = userMapper.findByUsername(username).orElse(null);
            if (user == null) {
                log.info("用户 {} 不存在，跳过密码初始化", username);
                return;
            }
            // 如果密码哈希是已知的无效值，或者密码哈希与当前密码不匹配
            if (OLD_INVALID_HASH.equals(user.getPassword())) {
                String newHash = passwordEncoder.encode(newPassword);
                user.setPassword(newHash);
                userMapper.updateById(user);
                log.info("已重置 {} 用户 {} 的密码为默认值", role, username);
            } else {
                log.info("用户 {} 的密码无需重置", username);
            }
        } catch (Exception e) {
            log.error("初始化用户 {} 密码时出错: {}", username, e.getMessage());
        }
    }
}