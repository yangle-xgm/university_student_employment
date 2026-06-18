package com.example.employment.career.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCareerPlanRequest {
    private String title;
    private String goalShort;
    private String goalMedium;
    private String goalLong;
    private String currentStage;
}