package com.example.employment.job.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateJobRequest {

    @NotBlank(message = "职位名称不能为空")
    private String title;

    @NotBlank(message = "公司名称不能为空")
    private String companyName;

    @NotBlank(message = "工作地点不能为空")
    private String location;

    @NotBlank(message = "行业不能为空")
    private String industry;

    @NotBlank(message = "职位描述不能为空")
    private String description;

    @NotBlank(message = "岗位职责不能为空")
    private String responsibilities;

    @NotBlank(message = "任职要求不能为空")
    private String requirements;

    @NotNull(message = "薪资下限不能为空")
    private BigDecimal minSalary;

    @NotNull(message = "薪资上限不能为空")
    private BigDecimal maxSalary;

    @NotBlank(message = "工作经验要求不能为空")
    private String experience;

    @NotBlank(message = "学历要求不能为空")
    private String education;

    private String benefits;

    private Integer recruitmentCount;
}