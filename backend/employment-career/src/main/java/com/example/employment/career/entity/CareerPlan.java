package com.example.employment.career.entity;

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
@TableName("career_plans")
public class CareerPlan {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("student_id")
    private Long studentId;

    @TableField("title")
    private String title;

    @TableField("goal_short")
    private String goalShort;

    @TableField("goal_medium")
    private String goalMedium;

    @TableField("goal_long")
    private String goalLong;

    @TableField("current_stage")
    private String currentStage;

    @TableField("progress")
    private Double progress;

    @TableField("status")
    private String status;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}