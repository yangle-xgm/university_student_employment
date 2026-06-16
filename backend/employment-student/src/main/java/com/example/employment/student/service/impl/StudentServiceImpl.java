package com.example.employment.student.service.impl;

import com.example.employment.common.dto.request.UpdateStudentProfileRequest;
import com.example.employment.common.dto.response.UserDTO;
import com.example.employment.common.entity.StudentProfile;
import com.example.employment.common.entity.User;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.repository.StudentProfileMapper;
import com.example.employment.common.repository.UserMapper;
import com.example.employment.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final UserMapper userMapper;
    private final StudentProfileMapper studentProfileMapper;

    @Override
    public UserDTO getStudentInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        StudentProfile profile = studentProfileMapper.findByUserId(userId);

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .realName(user.getRealName())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    @Override
    public UserDTO updateStudentProfile(Long userId, UpdateStudentProfileRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (request.getRealName() != null) {
            user.setRealName(request.getRealName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(user.getEmail()) && userMapper.countByEmail(request.getEmail()) > 0) {
                throw new BusinessException("邮箱已被使用");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        StudentProfile profile = studentProfileMapper.findByUserId(userId);
        if (profile == null) {
            profile = new StudentProfile();
            profile.setUserId(userId);
        }

        if (request.getSchool() != null) {
            profile.setSchool(request.getSchool());
        }
        if (request.getMajor() != null) {
            profile.setMajor(request.getMajor());
        }
        if (request.getGrade() != null) {
            profile.setGrade(request.getGrade());
        }
        if (request.getGraduationDate() != null) {
            profile.setGraduationDate(request.getGraduationDate());
        }
        if (request.getEducation() != null) {
            profile.setEducation(request.getEducation());
        }
        if (request.getSkills() != null) {
            profile.setSkills(request.getSkills());
        }
        if (request.getResumeUrl() != null) {
            profile.setResumeUrl(request.getResumeUrl());
        }

        userMapper.updateById(user);
        if (profile.getId() != null) {
            studentProfileMapper.updateById(profile);
        } else {
            studentProfileMapper.insert(profile);
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .realName(user.getRealName())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    @Override
    public void deleteStudentProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        StudentProfile profile = studentProfileMapper.findByUserId(userId);
        if (profile != null) {
            studentProfileMapper.deleteById(profile.getId());
        }
    }
}