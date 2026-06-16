package com.example.employment.common.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.common.entity.StudentProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentProfileMapper extends BaseMapper<StudentProfile> {
    
    @Select("SELECT * FROM student_profiles WHERE user_id = #{userId}")
    StudentProfile findByUserId(@Param("userId") Long userId);
}