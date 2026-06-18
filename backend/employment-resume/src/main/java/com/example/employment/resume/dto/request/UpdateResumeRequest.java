package com.example.employment.resume.dto.request;

import lombok.Data;

@Data
public class UpdateResumeRequest {

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
}