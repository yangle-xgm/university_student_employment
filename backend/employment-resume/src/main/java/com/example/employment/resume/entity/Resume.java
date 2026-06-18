package com.example.employment.resume.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("resumes")
public class Resume {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("title")
    private String title;

    @TableField("summary")
    private String summary;

    @TableField("education")
    private String education;

    @TableField("experience")
    private String experience;

    @TableField("skills")
    private String skills;

    @TableField("projects")
    private String projects;

    @TableField("certificates")
    private String certificates;

    @TableField("awards")
    private String awards;

    @TableField("contact_email")
    private String contactEmail;

    @TableField("contact_phone")
    private String contactPhone;

    @TableField("status")
    private String status;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}