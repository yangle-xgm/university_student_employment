package com.example.employment.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.learning.entity.LearningPath;
import com.example.employment.common.service.StudentProfileService;
import com.example.employment.learning.entity.LearningRecord;
import com.example.employment.learning.entity.LearningResource;
import com.example.employment.learning.mapper.LearningPathMapper;
import com.example.employment.learning.mapper.LearningPathResourceMapper;
import com.example.employment.learning.mapper.LearningRecordMapper;
import com.example.employment.learning.mapper.LearningResourceMapper;
import com.example.employment.learning.service.LearningService;
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
    public List<LearningResource> getResourceList(String category) {
        try {
            LambdaQueryWrapper<LearningResource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LearningResource::getStatus, "ACTIVE");
            if (category != null && !category.isEmpty()) {
                wrapper.eq(LearningResource::getCategory, category);
            }
            wrapper.orderByDesc(LearningResource::getCreatedAt);
            return learningResourceMapper.selectList(wrapper);
        } catch (Exception e) {
            log.error("Error getting resource list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<LearningResource> searchResources(String keyword) {
        try {
            LambdaQueryWrapper<LearningResource> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LearningResource::getStatus, "ACTIVE");
            if (keyword != null && !keyword.isEmpty()) {
                wrapper.and(w -> w.like(LearningResource::getTitle, keyword)
                        .or().like(LearningResource::getDescription, keyword));
            }
            wrapper.orderByDesc(LearningResource::getCreatedAt);
            return learningResourceMapper.selectList(wrapper);
        } catch (Exception e) {
            log.error("Error searching resources: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public LearningResource getResourceById(Long resourceId) {
        LearningResource resource = learningResourceMapper.selectById(resourceId);
        if (resource == null) {
            throw new BusinessException("学习资源不存在");
        }
        resource.setViewCount(resource.getViewCount() != null ? resource.getViewCount() + 1 : 1);
        learningResourceMapper.updateById(resource);
        return resource;
    }

    @Override
    public List<LearningPath> getLearningPathList(String status) {
        try {
            String filterStatus = (status != null && !status.isEmpty()) ? status : "ACTIVE";
            List<LearningPath> paths = learningPathMapper.findByStatus(filterStatus);
            return paths != null ? paths : Collections.emptyList();
        } catch (BindingException ex) {
            log.error("Mapper method findByStatus not found: {}", ex.getMessage());
            LambdaQueryWrapper<LearningPath> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LearningPath::getStatus, "ACTIVE");
            wrapper.orderByDesc(LearningPath::getCreatedAt);
            return learningPathMapper.selectList(wrapper);
        } catch (Exception e) {
            log.error("Error getting learning path list: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public LearningPath getLearningPathById(Long pathId) {
        return learningPathMapper.selectById(pathId);
    }

    @Override
    public List<LearningRecord> getLearningRecords(Long userId) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, false);
        if (studentProfileId == null) {
            return Collections.emptyList();
        }
        try {
            List<LearningRecord> records = learningRecordMapper.findByStudentId(studentProfileId);
            return records != null ? records : Collections.emptyList();
        } catch (BindingException ex) {
            log.error("Mapper method findByStudentId not found: {}", ex.getMessage());
            LambdaQueryWrapper<LearningRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LearningRecord::getStudentId, studentProfileId);
            wrapper.orderByDesc(LearningRecord::getCreatedAt);
            return learningRecordMapper.selectList(wrapper);
        } catch (Exception e) {
            log.error("Error getting learning records: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public LearningRecord updateLearningProgress(Long userId, Long resourceId, Double progress) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, true);
        if (studentProfileId == null) {
            throw new BusinessException("无法获取学生档案ID");
        }

        LearningResource resource = learningResourceMapper.selectById(resourceId);
        if (resource == null) {
            throw new BusinessException("学习资源不存在");
        }

        LambdaQueryWrapper<LearningRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningRecord::getStudentId, studentProfileId)
                .eq(LearningRecord::getResourceId, resourceId);
        LearningRecord record = learningRecordMapper.selectOne(wrapper);

        if (record == null) {
            record = LearningRecord.builder()
                    .studentId(studentProfileId)
                    .resourceId(resourceId)
                    .progress(Math.min(100.0, Math.max(0.0, progress)))
                    .completed(progress >= 100)
                    .lastWatchTime(LocalDateTime.now())
                    .build();
            learningRecordMapper.insert(record);
        } else {
            record.setProgress(Math.min(100.0, Math.max(0.0, progress)));
            record.setCompleted(record.getProgress() >= 100);
            record.setLastWatchTime(LocalDateTime.now());
            learningRecordMapper.updateById(record);
        }

        return record;
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
}