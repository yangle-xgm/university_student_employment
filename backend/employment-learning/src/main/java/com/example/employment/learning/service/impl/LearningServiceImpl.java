package com.example.employment.learning.service.impl;

import com.example.employment.learning.dto.LearningPathDTO;
import com.example.employment.learning.dto.LearningRecordDTO;
import com.example.employment.learning.dto.ResourceDTO;
import com.example.employment.learning.entity.LearningPath;
import com.example.employment.learning.entity.LearningPathResource;
import com.example.employment.learning.entity.LearningRecord;
import com.example.employment.learning.entity.LearningResource;
import com.example.employment.learning.mapper.LearningPathMapper;
import com.example.employment.learning.mapper.LearningPathResourceMapper;
import com.example.employment.learning.mapper.LearningRecordMapper;
import com.example.employment.learning.mapper.LearningResourceMapper;
import com.example.employment.learning.service.LearningService;
import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {

    private final LearningResourceMapper resourceMapper;
    private final LearningPathMapper pathMapper;
    private final LearningPathResourceMapper pathResourceMapper;
    private final LearningRecordMapper recordMapper;

    @Override
    public ApiResponse<List<ResourceDTO>> getResourceList(String category) {
        List<LearningResource> resources;
        if (category != null && !category.isEmpty()) {
            resources = resourceMapper.findByCategory(category);
        } else {
            resources = resourceMapper.findByStatus("ACTIVE");
        }
        List<ResourceDTO> dtoList = resources.stream()
                .map(this::convertToResourceDTO)
                .collect(Collectors.toList());
        return ApiResponse.success(dtoList);
    }

    @Override
    public ApiResponse<List<ResourceDTO>> searchResources(String keyword) {
        List<LearningResource> resources = resourceMapper.search("%" + keyword + "%");
        List<ResourceDTO> dtoList = resources.stream()
                .map(this::convertToResourceDTO)
                .collect(Collectors.toList());
        return ApiResponse.success(dtoList);
    }

    @Override
    public ApiResponse<ResourceDTO> getResourceById(Long resourceId) {
        LearningResource resource = resourceMapper.selectById(resourceId);
        if (resource == null) {
            throw new BusinessException("学习资源不存在");
        }
        resource.setViewCount(resource.getViewCount() != null ? resource.getViewCount() + 1 : 1);
        resourceMapper.updateById(resource);
        return ApiResponse.success(convertToResourceDTO(resource));
    }

    @Override
    public ApiResponse<List<LearningPathDTO>> getLearningPathList(String careerGoal) {
        List<LearningPath> paths;
        if (careerGoal != null && !careerGoal.isEmpty()) {
            paths = pathMapper.findByCareerGoal(careerGoal);
        } else {
            paths = pathMapper.findByStatus("ACTIVE");
        }
        List<LearningPathDTO> dtoList = paths.stream()
                .map(this::convertToPathDTO)
                .collect(Collectors.toList());
        return ApiResponse.success(dtoList);
    }

    @Override
    public ApiResponse<LearningPathDTO> getLearningPathById(Long pathId) {
        LearningPath path = pathMapper.selectById(pathId);
        if (path == null) {
            throw new BusinessException("学习路径不存在");
        }
        return ApiResponse.success(convertToPathDTO(path));
    }

    @Override
    public ApiResponse<List<LearningRecordDTO>> getLearningRecords(Long studentId) {
        List<LearningRecord> records = recordMapper.findByStudentId(studentId);
        if (records == null || records.isEmpty()) {
            return ApiResponse.success(Collections.emptyList());
        }
        
        List<Long> resourceIds = records.stream()
                .map(LearningRecord::getResourceId)
                .filter(id -> id != null)
                .collect(Collectors.toList());
        
        final Map<Long, LearningResource> resourceMap;
        if (!resourceIds.isEmpty()) {
            resourceMap = resourceMapper.selectBatchIds(resourceIds)
                    .stream()
                    .collect(Collectors.toMap(LearningResource::getId, r -> r));
        } else {
            resourceMap = Collections.emptyMap();
        }
        
        List<LearningRecordDTO> dtoList = records.stream()
                .map(record -> convertToRecordDTO(record, resourceMap.get(record.getResourceId())))
                .collect(Collectors.toList());
        return ApiResponse.success(dtoList);
    }

    @Override
    @Transactional
    public ApiResponse<LearningRecordDTO> updateLearningProgress(Long studentId, Long resourceId, Double progress) {
        LearningResource resource = resourceMapper.selectById(resourceId);
        if (resource == null) {
            throw new BusinessException("学习资源不存在");
        }

        LearningRecord record = recordMapper.findByStudentIdAndResourceId(studentId, resourceId);
        if (record == null) {
            record = LearningRecord.builder()
                    .studentId(studentId)
                    .resourceId(resourceId)
                    .progress(Math.min(100.0, Math.max(0.0, progress)))
                    .completed(progress >= 100)
                    .lastWatchTime(LocalDateTime.now())
                    .build();
            recordMapper.insert(record);
        } else {
            record.setProgress(Math.min(100.0, Math.max(0.0, progress)));
            record.setCompleted(record.getProgress() >= 100);
            record.setLastWatchTime(LocalDateTime.now());
            recordMapper.updateById(record);
        }

        return ApiResponse.success(convertToRecordDTO(record, resource));
    }

    private LearningPathDTO convertToPathDTO(LearningPath path) {
        List<LearningPathResource> pathResources = pathResourceMapper.findByPathId(path.getId());
        if (pathResources == null || pathResources.isEmpty()) {
            return LearningPathDTO.builder()
                    .id(path.getId())
                    .title(path.getTitle())
                    .description(path.getDescription())
                    .careerGoal(path.getCareerGoal())
                    .estimatedDuration(path.getEstimatedDuration())
                    .status(path.getStatus())
                    .createdAt(path.getCreatedAt())
                    .resources(Collections.emptyList())
                    .build();
        }
        
        List<Long> resourceIds = pathResources.stream()
                .map(LearningPathResource::getResourceId)
                .filter(id -> id != null)
                .collect(Collectors.toList());
        
        final Map<Long, LearningResource> resourceMap;
        if (!resourceIds.isEmpty()) {
            resourceMap = resourceMapper.selectBatchIds(resourceIds)
                    .stream()
                    .collect(Collectors.toMap(LearningResource::getId, r -> r));
        } else {
            resourceMap = Collections.emptyMap();
        }
        
        List<ResourceDTO> resources = pathResources.stream()
                .sorted((a, b) -> {
                    if (a.getSortOrder() == null && b.getSortOrder() == null) return 0;
                    if (a.getSortOrder() == null) return -1;
                    if (b.getSortOrder() == null) return 1;
                    return Integer.compare(a.getSortOrder(), b.getSortOrder());
                })
                .map(pr -> convertToResourceDTO(resourceMap.get(pr.getResourceId())))
                .filter(r -> r != null)
                .collect(Collectors.toList());

        return LearningPathDTO.builder()
                .id(path.getId())
                .title(path.getTitle())
                .description(path.getDescription())
                .careerGoal(path.getCareerGoal())
                .estimatedDuration(path.getEstimatedDuration())
                .status(path.getStatus())
                .createdAt(path.getCreatedAt())
                .resources(resources)
                .build();
    }

    private ResourceDTO convertToResourceDTO(LearningResource resource) {
        if (resource == null) return null;
        return ResourceDTO.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .type(resource.getType())
                .category(resource.getCategory())
                .difficulty(resource.getDifficulty())
                .duration(resource.getDuration())
                .url(resource.getUrl())
                .thumbnailUrl(resource.getThumbnailUrl())
                .description(resource.getDescription())
                .viewCount(resource.getViewCount())
                .status(resource.getStatus())
                .createdAt(resource.getCreatedAt())
                .build();
    }

    private LearningRecordDTO convertToRecordDTO(LearningRecord record, LearningResource resource) {
        return LearningRecordDTO.builder()
                .id(record.getId())
                .studentId(record.getStudentId())
                .resourceId(record.getResourceId())
                .resourceTitle(resource != null ? resource.getTitle() : null)
                .progress(record.getProgress())
                .completed(record.getCompleted())
                .lastWatchTime(record.getLastWatchTime())
                .createdAt(record.getCreatedAt())
                .build();
    }
}