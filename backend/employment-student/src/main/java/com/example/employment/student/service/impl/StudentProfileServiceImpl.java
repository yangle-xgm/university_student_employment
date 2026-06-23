package com.example.employment.student.service.impl;

import com.example.employment.common.entity.StudentProfile;
import com.example.employment.common.repository.StudentProfileMapper;
import com.example.employment.student.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileMapper studentProfileMapper;

    @Override
    @Transactional
    public Long getOrCreateStudentProfileId(Long userId, boolean createIfMissing) {
        if (userId == null) return null;
        StudentProfile profile = studentProfileMapper.findByUserId(userId);
        if (profile != null) {
            return profile.getId();
        }
        if (!createIfMissing) {
            return null;
        }
        StudentProfile newProfile = new StudentProfile();
        newProfile.setUserId(userId);
        newProfile.setCreatedAt(LocalDateTime.now());
        newProfile.setUpdatedAt(LocalDateTime.now());
        studentProfileMapper.insert(newProfile);
        return newProfile.getId();
    }
}