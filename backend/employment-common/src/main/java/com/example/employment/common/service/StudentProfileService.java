package com.example.employment.common.service;

public interface StudentProfileService {

    Long getOrCreateStudentProfileId(Long userId, boolean createIfMissing);
}