package com.example.employment.career.dto;

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
public class CareerMilestoneDTO {
    private Long id;
    private Long planId;
    private String title;
    private String description;
    private LocalDate targetDate;
    private Double progress;
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}