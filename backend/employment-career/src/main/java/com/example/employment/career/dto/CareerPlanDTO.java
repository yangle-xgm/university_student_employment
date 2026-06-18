package com.example.employment.career.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerPlanDTO {
    private Long id;
    private Long studentId;
    private String title;
    private String goalShort;
    private String goalMedium;
    private String goalLong;
    private String currentStage;
    private Double progress;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}