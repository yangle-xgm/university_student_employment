package com.example.employment.interview.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateInterviewRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "职位ID不能为空")
    private Long jobId;

    @NotNull(message = "面试时间不能为空")
    private LocalDateTime interviewTime;

    @NotBlank(message = "面试地点不能为空")
    private String location;

    private String interviewer;

    private String contactPhone;

    private String notes;
}