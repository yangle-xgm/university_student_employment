package com.example.employment.resume.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateResumeRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "简历标题不能为空")
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
}