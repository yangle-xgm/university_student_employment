package com.example.employment.learning.service.impl;

import com.example.employment.learning.entity.LearningPath;
import com.example.employment.learning.entity.LearningRecord;
import com.example.employment.learning.entity.LearningResource;
import com.example.employment.learning.mapper.LearningPathMapper;
import com.example.employment.learning.mapper.LearningPathResourceMapper;
import com.example.employment.learning.mapper.LearningRecordMapper;
import com.example.employment.learning.mapper.LearningResourceMapper;
import com.example.employment.learning.service.LearningService;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.binding.BindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {
    private static final Logger log = LoggerFactory.getLogger(LearningServiceImpl.class);

    private final LearningPathMapper learningPathMapper;
    private final LearningPathResourceMapper learningPathResourceMapper;
    private final LearningResourceMapper learningResourceMapper;
    private final LearningRecordMapper learningRecordMapper;
    private final StudentProfileService studentProfileService;

    @Override
    public List<LearningPath> getLearningPathList(String status) {
        try {
            List<LearningPath> paths = learningPathMapper.findByStatus(status);
            if (paths != null) {
                paths.forEach(path -> {
                    try {
                        List<LearningResource> resources = learningPathResourceMapper.findByPathId(path.getId());
                        // 如果 LearningPath 有 resources 字段，设置它；否则前端可单独请求 resources
                        // path.setResources(resources);
                    } catch (BindingException be) {
                        log.warn("findByPathId not available for path {}: {}", path.getId(), be.getMessage());
                    } catch (Exception e) {
                        log.warn("error loading resources for path {}: {}", path.getId(), e.getMessage());
                    }
                });
            }
            return paths != null ? paths : Collections.emptyList();
        } catch (BindingException ex) {
            log.error("Mapper method findByStatus not found: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<LearningRecord> getLearningRecords(Long userId /* user id */) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, false);
        if (studentProfileId == null) {
            return Collections.emptyList();
        }
        try {
            List<LearningRecord> records = learningRecordMapper.findByStudentId(studentProfileId);
            return records != null ? records : Collections.emptyList();
        } catch (BindingException ex) {
            log.error("Mapper method findByStudentId not found: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<LearningResource> getResourcesForPath(Long pathId) {
        try {
            List<LearningResource> resources = learningPathResourceMapper.findByPathId(pathId);
            return resources != null ? resources : Collections.emptyList();
        } catch (BindingException ex) {
            log.error("Mapper method findByPathId not found: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<LearningResource> getResourcesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return learningResourceMapper.selectBatchIds(ids);
    }

    @Override
    @Transactional
    public void updateLearningProgress(Long userId, Long resourceId, Double progress) {
        if (userId == null || resourceId == null || progress == null) {
            throw new BusinessException("参数不完整");
        }
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, true);
        if (studentProfileId == null) {
            throw new BusinessException("无法获取学生档案ID");
        }

        LearningRecord exist = learningRecordMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<LearningRecord>()
                .eq(LearningRecord::getStudentId, studentProfileId)
                .eq(LearningRecord::getResourceId, resourceId)
        );

        if (exist == null) {
            LearningRecord r = new LearningRecord();
            r.setStudentId(studentProfileId);
            r.setResourceId(resourceId);
            r.setProgress(progress);
            r.setCompleted(progress >= 100.0);
            r.setCreatedAt(LocalDateTime.now());
            r.setUpdatedAt(LocalDateTime.now());
            learningRecordMapper.insert(r);
        } else {
            exist.setProgress(progress);
            exist.setCompleted(progress >= 100.0);
            exist.setUpdatedAt(LocalDateTime.now());
            learningRecordMapper.updateById(exist);
        }
    }
}
