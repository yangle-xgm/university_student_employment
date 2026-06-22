package com.example.employment.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.learning.entity.LearningPath;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LearningPathMapper extends BaseMapper<LearningPath> {
    List<LearningPath> findByStatus(String status);
}