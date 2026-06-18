package com.example.employment.interview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewDTO {

    private Long id;

    private Long userId;

    private Long jobId;

    private LocalDateTime interviewTime;

    private String location;

    private String interviewer;

    private String contactPhone;

    private String notes;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}