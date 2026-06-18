package com.example.employment.job.dto.response;

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
public class JobDTO {

    private Long id;

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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}