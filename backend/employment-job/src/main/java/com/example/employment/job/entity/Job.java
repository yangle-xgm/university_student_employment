package com.example.employment.job.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("jobs")
public class Job {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("company_id")
    private Long companyId;

    @TableField("title")
    private String title;

    @TableField("type")
    private String type;

    @TableField("industry")
    private String industry;

    @TableField("location")
    private String location;

    @TableField("min_salary")
    private BigDecimal minSalary;

    @TableField("max_salary")
    private BigDecimal maxSalary;

    @TableField("education")
    private String education;

    @TableField("experience")
    private String experience;

    @TableField("requirements")
    private String requirements;

    @TableField("responsibilities")
    private String responsibilities;

    @TableField("benefits")
    private String benefits;

    @TableField("deadline")
    private LocalDate deadline;

    @TableField("status")
    private String status;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private String companyName;
}