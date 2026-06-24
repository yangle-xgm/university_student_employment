package com.example.employment.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.example.employment.admin", "com.example.employment.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.employment")
@MapperScan({"com.example.employment.admin.mapper", "com.example.employment.common.repository"})
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}