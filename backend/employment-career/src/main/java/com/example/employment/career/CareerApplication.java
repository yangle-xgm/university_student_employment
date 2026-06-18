package com.example.employment.career;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.example.employment.career", "com.example.employment.common"})
@EnableDiscoveryClient
public class CareerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerApplication.class, args);
    }
}