package com.example.employment.admin.controller;

import com.example.employment.admin.service.AdminAuditService;
import com.example.employment.admin.service.AdminCompanyService;
import com.example.employment.admin.service.AdminUserService;
import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.common.entity.AuditLog;
import com.example.employment.common.entity.CompanyProfile;
import com.example.employment.common.entity.User;
import com.example.employment.common.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "管理员端", description = "平台管理员接口")
public class AdminController {

    private final AdminUserService adminUserService;
    private final AdminCompanyService adminCompanyService;
    private final AdminAuditService adminAuditService;

    @GetMapping("/users")
    @Operation(summary = "获取用户列表")
    public ResponseEntity<ApiResponse<List<User>>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(adminUserService.getAllUsers(page, size)));
    }

    @PutMapping("/users/{userId}/role")
    @Operation(summary = "修改用户角色")
    public ResponseEntity<ApiResponse<User>> updateUserRole(
            @PathVariable Long userId,
            @RequestParam String role) {
        Long adminId = SecurityUtils.getCurrentUserId();
        User user = adminUserService.updateUserRole(userId, role);
        adminAuditService.recordAudit(adminId, "ADMIN", "UPDATE_USER_ROLE", "USER", userId,
                "角色变更为: " + role);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/users/{userId}/disable")
    @Operation(summary = "禁用用户")
    public ResponseEntity<ApiResponse<String>> disableUser(@PathVariable Long userId) {
        Long adminId = SecurityUtils.getCurrentUserId();
        adminUserService.disableUser(userId);
        adminAuditService.recordAudit(adminId, "ADMIN", "DISABLE_USER", "USER", userId, null);
        return ResponseEntity.ok(ApiResponse.success("用户已禁用"));
    }

    @PutMapping("/users/{userId}/enable")
    @Operation(summary = "启用用户")
    public ResponseEntity<ApiResponse<String>> enableUser(@PathVariable Long userId) {
        Long adminId = SecurityUtils.getCurrentUserId();
        adminUserService.enableUser(userId);
        adminAuditService.recordAudit(adminId, "ADMIN", "ENABLE_USER", "USER", userId, null);
        return ResponseEntity.ok(ApiResponse.success("用户已启用"));
    }

    @PutMapping("/users/{userId}/reset-password")
    @Operation(summary = "重置用户密码")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @PathVariable Long userId,
            @RequestParam String newPassword) {
        Long adminId = SecurityUtils.getCurrentUserId();
        adminUserService.resetPassword(userId, newPassword);
        adminAuditService.recordAudit(adminId, "ADMIN", "RESET_PASSWORD", "USER", userId, null);
        return ResponseEntity.ok(ApiResponse.success("密码已重置"));
    }

    @GetMapping("/companies/unverified")
    @Operation(summary = "获取未审核公司列表")
    public ResponseEntity<ApiResponse<List<CompanyProfile>>> getUnverifiedCompanies() {
        return ResponseEntity.ok(ApiResponse.success(adminCompanyService.getUnverifiedCompanies()));
    }

    @PostMapping("/companies/{companyId}/verify")
    @Operation(summary = "审核通过公司")
    public ResponseEntity<ApiResponse<String>> verifyCompany(@PathVariable Long companyId) {
        Long adminId = SecurityUtils.getCurrentUserId();
        adminCompanyService.verifyCompany(companyId, adminId);
        adminAuditService.recordAudit(adminId, "ADMIN", "VERIFY_COMPANY", "COMPANY", companyId, null);
        return ResponseEntity.ok(ApiResponse.success("公司审核通过"));
    }

    @PostMapping("/companies/{companyId}/reject")
    @Operation(summary = "驳回公司审核")
    public ResponseEntity<ApiResponse<String>> rejectCompany(@PathVariable Long companyId) {
        Long adminId = SecurityUtils.getCurrentUserId();
        adminCompanyService.rejectCompany(companyId);
        adminAuditService.recordAudit(adminId, "ADMIN", "REJECT_COMPANY", "COMPANY", companyId, null);
        return ResponseEntity.ok(ApiResponse.success("公司审核已驳回"));
    }

    @GetMapping("/audit-logs")
    @Operation(summary = "查看审计日志")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs(
            @RequestParam(required = false) Long actorUserId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(ApiResponse.success(
                adminAuditService.getAuditLogs(actorUserId, action, from, to)));
    }
}