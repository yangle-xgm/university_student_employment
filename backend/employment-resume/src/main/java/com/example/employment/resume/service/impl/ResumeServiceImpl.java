package com.example.employment.resume.service.impl;

import com.example.employment.common.exception.BusinessException;
import com.example.employment.resume.dto.request.CreateResumeRequest;
import com.example.employment.resume.dto.request.UpdateResumeRequest;
import com.example.employment.resume.dto.response.ResumeDTO;
import com.example.employment.resume.entity.Resume;
import com.example.employment.resume.repository.ResumeMapper;
import com.example.employment.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;

    @Override
    public ResumeDTO getResumeById(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new BusinessException("简历不存在");
        }
        return convertToDTO(resume);
    }

    @Override
    public List<ResumeDTO> getResumesByUserId(Long userId) {
        List<Resume> resumes = resumeMapper.selectByUserId(userId);
        return resumes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResumeDTO createResume(CreateResumeRequest request) {
        Resume resume = Resume.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .summary(request.getSummary())
                .education(request.getEducation())
                .experience(request.getExperience())
                .skills(request.getSkills())
                .projects(request.getProjects())
                .certificates(request.getCertificates())
                .awards(request.getAwards())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .status("ACTIVE")
                .build();

        resumeMapper.insert(resume);
        return convertToDTO(resume);
    }

    @Override
    @Transactional
    public ResumeDTO updateResume(Long resumeId, UpdateResumeRequest request) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new BusinessException("简历不存在");
        }

        if (StringUtils.hasText(request.getTitle())) {
            resume.setTitle(request.getTitle());
        }
        if (StringUtils.hasText(request.getSummary())) {
            resume.setSummary(request.getSummary());
        }
        if (StringUtils.hasText(request.getEducation())) {
            resume.setEducation(request.getEducation());
        }
        if (StringUtils.hasText(request.getExperience())) {
            resume.setExperience(request.getExperience());
        }
        if (StringUtils.hasText(request.getSkills())) {
            resume.setSkills(request.getSkills());
        }
        if (StringUtils.hasText(request.getProjects())) {
            resume.setProjects(request.getProjects());
        }
        if (StringUtils.hasText(request.getCertificates())) {
            resume.setCertificates(request.getCertificates());
        }
        if (StringUtils.hasText(request.getAwards())) {
            resume.setAwards(request.getAwards());
        }
        if (StringUtils.hasText(request.getContactEmail())) {
            resume.setContactEmail(request.getContactEmail());
        }
        if (StringUtils.hasText(request.getContactPhone())) {
            resume.setContactPhone(request.getContactPhone());
        }
        if (StringUtils.hasText(request.getStatus())) {
            resume.setStatus(request.getStatus());
        }

        resumeMapper.updateById(resume);
        return convertToDTO(resume);
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

    private ResumeDTO convertToDTO(Resume resume) {
        return ResumeDTO.builder()
                .id(resume.getId())
                .userId(resume.getUserId())
                .title(resume.getTitle())
                .summary(resume.getSummary())
                .education(resume.getEducation())
                .experience(resume.getExperience())
                .skills(resume.getSkills())
                .projects(resume.getProjects())
                .certificates(resume.getCertificates())
                .awards(resume.getAwards())
                .contactEmail(resume.getContactEmail())
                .contactPhone(resume.getContactPhone())
                .status(resume.getStatus())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .build();
    }
}