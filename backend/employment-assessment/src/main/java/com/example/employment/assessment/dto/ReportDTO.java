package com.example.employment.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Long id;
    private Long recordId;
    private Long studentId;
    private String content;
    private String recommendations;
    private LocalDateTime generatedAt;
}