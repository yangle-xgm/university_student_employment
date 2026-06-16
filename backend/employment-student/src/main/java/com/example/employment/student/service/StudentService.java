package com.example.employment.student.service;

import com.example.employment.common.dto.request.UpdateStudentProfileRequest;
import com.example.employment.common.dto.response.UserDTO;

public interface StudentService {
    UserDTO getStudentInfo(Long userId);
    UserDTO updateStudentProfile(Long userId, UpdateStudentProfileRequest request);
    void deleteStudentProfile(Long userId);
}