package com.example.employment.hr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.example.employment.hr", "com.example.employment.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.employment")
@MapperScan({"com.example.employment.hr.mapper", "com.example.employment.common.repository"})
public class HrApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrApplication.class, args);
    }
}