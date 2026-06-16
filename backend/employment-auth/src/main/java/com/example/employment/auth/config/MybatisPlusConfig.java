package com.example.employment.auth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.employment.common.repository")
public class MybatisPlusConfig {
}