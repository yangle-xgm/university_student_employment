package com.example.employment.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
    private Long id;
    private Long assessmentId;
    private String questionType;
    private String content;
    private String options;
    private Double weight;
    private Integer sortOrder;
}