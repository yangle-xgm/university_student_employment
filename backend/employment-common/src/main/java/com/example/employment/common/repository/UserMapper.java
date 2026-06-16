package com.example.employment.common.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM users WHERE username = #{username}")
    Optional<User> findByUsername(@Param("username") String username);
    
    @Select("SELECT * FROM users WHERE email = #{email}")
    Optional<User> findByEmail(@Param("email") String email);
    
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(@Param("username") String username);
    
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int countByEmail(@Param("email") String email);
}