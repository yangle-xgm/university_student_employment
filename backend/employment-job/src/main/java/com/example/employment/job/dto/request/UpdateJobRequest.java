package com.example.employment.job.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateJobRequest {

    private String title;

    private String companyName;

    private String location;

    private String industry;

    private String description;

    private String responsibilities;

    private String requirements;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

    private String experience;

    private String education;

    private String benefits;

    private Integer recruitmentCount;

    private String status;
}