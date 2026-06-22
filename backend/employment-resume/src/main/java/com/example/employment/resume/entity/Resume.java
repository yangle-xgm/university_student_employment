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

    @TableField("student_id")
    private Long studentId;

    @TableField("template_id")
    private Long templateId;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("file_url")
    private String fileUrl;

    @TableField("is_default")
    private Boolean isDefault;

    @TableField("format_score")
    private Integer formatScore;

    @TableField("content_score")
    private Integer contentScore;

    @TableField("match_score")
    private Integer matchScore;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}