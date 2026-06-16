package com.example.employment.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentProfileRequest {
    @Size(max = 100, message = "学校名称不能超过100个字符")
    private String school;

    @Size(max = 100, message = "专业名称不能超过100个字符")
    private String major;

    @Size(max = 20, message = "年级不能超过20个字符")
    private String grade;

    private LocalDate graduationDate;

    @Size(max = 20, message = "学历不能超过20个字符")
    private String education;

    private String skills;

    @Size(max = 255, message = "简历URL不能超过255个字符")
    private String resumeUrl;

    @Size(max = 50, message = "真实姓名不能超过50个字符")
    private String realName;

    @Size(max = 20, message = "手机号不能超过20个字符")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱不能超过100个字符")
    private String email;

    @Size(max = 255, message = "头像URL不能超过255个字符")
    private String avatar;
}