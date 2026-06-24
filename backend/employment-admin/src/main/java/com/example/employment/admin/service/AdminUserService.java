package com.example.employment.admin.service;

import com.example.employment.common.entity.User;

import java.util.List;

public interface AdminUserService {

    List<User> getAllUsers(int page, int size);

    User updateUserRole(Long userId, String role);

    void disableUser(Long userId);

    void enableUser(Long userId);

    void resetPassword(Long userId, String newPassword);
}