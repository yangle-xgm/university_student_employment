package com.example.employment.interview.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateInterviewRequest {

    private LocalDateTime interviewTime;

    private String location;

    private String interviewer;

    private String contactPhone;

    private String notes;

    private String status;
}