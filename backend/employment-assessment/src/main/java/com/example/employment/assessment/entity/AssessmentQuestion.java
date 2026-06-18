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
@TableName("assessment_questions")
public class AssessmentQuestion {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("assessment_id")
    private Long assessmentId;

    @TableField("question_type")
    private String questionType;

    @TableField("content")
    private String content;

    @TableField("options")
    private String options;

    @TableField("weight")
    private Double weight;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}