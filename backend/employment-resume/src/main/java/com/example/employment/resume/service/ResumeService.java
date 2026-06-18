package com.example.employment.resume.service;

import com.example.employment.resume.dto.request.CreateResumeRequest;
import com.example.employment.resume.dto.request.UpdateResumeRequest;
import com.example.employment.resume.dto.response.ResumeDTO;

import java.util.List;

public interface ResumeService {
    ResumeDTO getResumeById(Long resumeId);
    List<ResumeDTO> getResumesByUserId(Long userId);
    ResumeDTO createResume(CreateResumeRequest request);
    ResumeDTO updateResume(Long resumeId, UpdateResumeRequest request);
    void deleteResume(Long resumeId);
}