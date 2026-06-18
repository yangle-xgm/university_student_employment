package com.example.employment.interview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.interview.entity.Interview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InterviewMapper extends BaseMapper<Interview> {

    @Select("SELECT * FROM interviews WHERE user_id = #{userId} ORDER BY interview_time DESC")
    List<Interview> findByUserId(Long userId);

    @Select("SELECT * FROM interviews WHERE job_id = #{jobId} ORDER BY interview_time DESC")
    List<Interview> findByJobId(Long jobId);
}
