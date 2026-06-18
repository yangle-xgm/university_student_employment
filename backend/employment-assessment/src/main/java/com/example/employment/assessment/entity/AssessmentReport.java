package com.example.employment.assessment.entity;

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
@TableName("assessment_reports")
public class AssessmentReport {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("record_id")
    private Long recordId;

    @TableField("student_id")
    private Long studentId;

    @TableField("content")
    private String content;

    @TableField("recommendations")
    private String recommendations;

    @TableField("generated_at")
    private LocalDateTime generatedAt;
}