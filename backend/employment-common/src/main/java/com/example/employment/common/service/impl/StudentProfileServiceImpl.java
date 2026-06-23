package com.example.employment.common.service.impl;

import com.example.employment.common.entity.StudentProfile;
import com.example.employment.common.repository.StudentProfileMapper;
import com.example.employment.common.service.StudentProfileService;
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
        // 使用已有 mapper 方法查找（你的 repo 中已有 findByUserId）
        StudentProfile profile = studentProfileMapper.findByUserId(userId);
        if (profile != null) {
            return profile.getId();
        }
        if (!createIfMissing) {
            return null;
        }
        // 自动创建最小化 student_profile（仅 user_id 与时间戳）
        StudentProfile newProfile = new StudentProfile();
        newProfile.setUserId(userId);
        newProfile.setCreatedAt(LocalDateTime.now());
        newProfile.setUpdatedAt(LocalDateTime.now());
        studentProfileMapper.insert(newProfile);
        return newProfile.getId();
    }
}
