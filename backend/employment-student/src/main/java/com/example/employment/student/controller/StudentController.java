package com.example.employment.student.controller;

import com.example.employment.common.dto.request.UpdateStudentProfileRequest;
import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.common.dto.response.UserDTO;
import com.example.employment.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "学生管理", description = "学生信息管理相关接口")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{userId}")
    @Operation(summary = "获取学生信息", description = "根据用户ID获取学生详细信息")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> getStudentInfo(@PathVariable Long userId) {
        UserDTO userDTO = studentService.getStudentInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(userDTO));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "更新学生信息", description = "更新学生的个人资料")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<UserDTO>> updateStudentProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateStudentProfileRequest request) {
        UserDTO userDTO = studentService.updateStudentProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("更新成功", userDTO));
    }

    @DeleteMapping("/{userId}/profile")
    @Operation(summary = "删除学生资料", description = "删除学生的个人资料")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStudentProfile(@PathVariable Long userId) {
        studentService.deleteStudentProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}