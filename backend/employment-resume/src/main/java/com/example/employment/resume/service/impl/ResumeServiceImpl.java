package com.example.employment.resume.service.impl;

import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.service.StudentProfileService;
import com.example.employment.resume.entity.Resume;
import com.example.employment.resume.repository.ResumeMapper;
import com.example.employment.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;
    private final StudentProfileService studentProfileService;

    @Override
    public Resume getResumeById(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new BusinessException("简历不存在");
        }
        return resume;
    }

    @Override
    public List<Resume> getResumesByStudentId(Long studentId) {
        return resumeMapper.selectByStudentId(studentId);
    }

    @Override
    @Transactional
    public Resume createResume(Resume resume) {
        Long userId = resume.getStudentId();
        if (userId == null) {
            userId = resolveCurrentUserId();
            if (userId == null) {
                throw new BusinessException("无法识别当前用户ID，请先登录或在请求体中提供 studentId");
            }
        }

        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, true);
        if (studentProfileId == null) {
            throw new BusinessException("无法获取学生档案ID");
        }
        resume.setStudentId(studentProfileId);

        if (resume.getCreatedAt() == null) {
            resume.setCreatedAt(LocalDateTime.now());
        }
        resume.setUpdatedAt(LocalDateTime.now());

        resumeMapper.insert(resume);
        return resume;
    }

    @Override
    @Transactional
    public Resume updateResume(Long resumeId, Resume resume) {
        Resume existing = resumeMapper.selectById(resumeId);
        if (existing == null) {
            throw new BusinessException("简历不存在");
        }
        resume.setId(resumeId);
        resumeMapper.updateById(resume);
        return resumeMapper.selectById(resumeId);
    }

    @Override
    @Transactional
    public void deleteResume(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new BusinessException("简历不存在");
        }
        resumeMapper.deleteById(resumeId);
    }

    private Long resolveCurrentUserId() {
        // Keep existing reflection fallback for now; recommended to use SecurityUtils or @AuthenticationPrincipal
        try {
            Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal == null) return null;
            Method getIdMethod = principal.getClass().getMethod("getId");
            Object idObj = getIdMethod.invoke(principal);
            if (idObj instanceof Number) return ((Number) idObj).longValue();
            if (idObj instanceof String) return Long.valueOf((String) idObj);
        } catch (Exception ignored) {}
        return null;
    }
}
