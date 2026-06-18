package com.example.employment.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentDTO {
    private Long id;
    private String name;
    private String description;
    private String dimension;
    private Integer duration;
    private String status;
}