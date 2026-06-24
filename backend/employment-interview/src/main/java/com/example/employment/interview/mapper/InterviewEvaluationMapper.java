package com.example.employment.interview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.interview.entity.InterviewEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InterviewEvaluationMapper extends BaseMapper<InterviewEvaluation> {

    List<InterviewEvaluation> findByInterviewId(@Param("interviewId") Long interviewId);
}