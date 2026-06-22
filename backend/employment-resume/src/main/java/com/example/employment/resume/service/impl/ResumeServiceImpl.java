package com.example.employment.resume.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.resume.entity.Resume;
import com.example.employment.resume.repository.ResumeMapper;
import com.example.employment.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;

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
}