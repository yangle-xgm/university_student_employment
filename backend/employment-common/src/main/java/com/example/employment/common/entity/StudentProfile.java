package com.example.employment.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("student_profiles")
public class StudentProfile {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("school")
    private String school;

    @TableField("major")
    private String major;

    @TableField("grade")
    private String grade;

    @TableField("graduation_date")
    private LocalDate graduationDate;

    @TableField("education")
    private String education;

    @TableField("skills")
    private String skills;

    @TableField("resume_url")
    private String resumeUrl;

    @TableField("bio")
    private String bio;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("target_job")
    private String targetJob;

    @TableField("expected_salary")
    private BigDecimal expectedSalary;

    @TableField("job_status")
    private String jobStatus;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}