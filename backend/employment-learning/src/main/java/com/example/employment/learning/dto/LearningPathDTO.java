package com.example.employment.learning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPathDTO {
    private Long id;
    private String title;
    private String description;
    private String careerGoal;
    private Integer estimatedDuration;
    private String status;
    private LocalDateTime createdAt;
    private List<ResourceDTO> resources;
}