package com.example.employment.learning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningRecordDTO {
    private Long id;
    private Long studentId;
    private Long resourceId;
    private String resourceTitle;
    private Double progress;
    private Boolean completed;
    private LocalDateTime lastWatchTime;
    private LocalDateTime createdAt;
}