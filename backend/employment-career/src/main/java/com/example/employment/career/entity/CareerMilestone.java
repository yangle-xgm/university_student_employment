package com.example.employment.career.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("career_milestones")
public class CareerMilestone {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("plan_id")
    private Long planId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("target_date")
    private LocalDate targetDate;

    @TableField("progress")
    private Double progress;

    @TableField("completed")
    private Boolean completed;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}