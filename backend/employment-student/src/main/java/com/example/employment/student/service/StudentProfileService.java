package com.example.employment.student.service;

public interface StudentProfileService {

    Long getOrCreateStudentProfileId(Long userId, boolean createIfMissing);
}