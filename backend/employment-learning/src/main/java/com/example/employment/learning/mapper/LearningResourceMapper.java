package com.example.employment.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.learning.entity.LearningResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LearningResourceMapper extends BaseMapper<LearningResource> {
    List<LearningResource> findByStatus(String status);
    List<LearningResource> findByCategory(String category);
    List<LearningResource> search(String keyword);
}