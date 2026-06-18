package com.example.employment.job.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("jobs")
public class Job {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("company_name")
    private String companyName;

    @TableField("location")
    private String location;

    @TableField("industry")
    private String industry;

    @TableField("description")
    private String description;

    @TableField("responsibilities")
    private String responsibilities;

    @TableField("requirements")
    private String requirements;

    @TableField("min_salary")
    private BigDecimal minSalary;

    @TableField("max_salary")
    private BigDecimal maxSalary;

    @TableField("experience")
    private String experience;

    @TableField("education")
    private String education;

    @TableField("benefits")
    private String benefits;

    @TableField("recruitment_count")
    private Integer recruitmentCount;

    @TableField("status")
    private String status;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}