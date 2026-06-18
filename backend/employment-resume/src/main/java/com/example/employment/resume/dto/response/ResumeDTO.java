package com.example.employment.resume.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeDTO {

    private Long id;

    private Long userId;

    private String title;

    private String summary;

    private String education;

    private String experience;

    private String skills;

    private String projects;

    private String certificates;

    private String awards;

    private String contactEmail;

    private String contactPhone;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}