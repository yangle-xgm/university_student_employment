package com.example.employment.assessment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.assessment.entity.AssessmentQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssessmentQuestionMapper extends BaseMapper<AssessmentQuestion> {
    List<AssessmentQuestion> findByAssessmentId(Long assessmentId);
}