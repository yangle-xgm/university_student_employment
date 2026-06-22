package com.example.employment.resume.service;

import com.example.employment.resume.entity.Resume;

import java.util.List;

public interface ResumeService {
    Resume getResumeById(Long resumeId);
    List<Resume> getResumesByStudentId(Long studentId);
    Resume createResume(Resume resume);
    Resume updateResume(Long resumeId, Resume resume);
    void deleteResume(Long resumeId);
}