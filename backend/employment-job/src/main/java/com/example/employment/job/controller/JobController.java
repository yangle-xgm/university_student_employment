package com.example.employment.job.controller;

import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.job.dto.response.JobDTO;
import com.example.employment.job.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "职位管理", description = "职位相关接口")
public class JobController {

    private final JobService jobService;

    @GetMapping
    @Operation(summary = "获取职位列表", description = "获取所有职位（分页/筛选可在 service 层实现）")
    public ResponseEntity<ApiResponse<List<JobDTO>>> getAllJobs() {
        List<JobDTO> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @GetMapping("/{jobId}")
    @Operation(summary = "根据 ID 获取职位详情", description = "返回职位详情（包含 companyName）")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(@PathVariable Long jobId) {
        JobDTO job = jobService.getJobById(jobId);
        return ResponseEntity.ok(ApiResponse.success(job));
    }
}
