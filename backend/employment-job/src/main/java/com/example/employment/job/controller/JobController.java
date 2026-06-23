package com.example.employment.job.controller;

import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.job.dto.response.JobDTO;
import com.example.employment.job.entity.Job;
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
@Tag(name = "职位管理", description = "职位信息管理相关接口")
public class JobController {

    private final JobService jobService;

    @GetMapping("/{jobId}")
    @Operation(summary = "获取职位详情", description = "根据职位ID获取职位详细信息")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(@PathVariable Long jobId) {
        JobDTO job = jobService.getJobById(jobId);
        return ResponseEntity.ok(ApiResponse.success(job));
    }

    @GetMapping
    @Operation(summary = "获取所有职位", description = "获取所有职位列表")
    public ResponseEntity<ApiResponse<List<JobDTO>>> getAllJobs() {
        List<JobDTO> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索职位", description = "根据关键词、地点、行业搜索职位")
    public ResponseEntity<ApiResponse<List<JobDTO>>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String industry) {
        List<JobDTO> jobs = jobService.searchJobs(keyword, location, industry);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @PostMapping
    @Operation(summary = "创建职位", description = "发布新职位")
    public ResponseEntity<ApiResponse<JobDTO>> createJob(@RequestBody Job job) {
        JobDTO created = jobService.createJob(job);
        return ResponseEntity.ok(ApiResponse.success("创建成功", created));
    }

    @PutMapping("/{jobId}")
    @Operation(summary = "更新职位", description = "更新职位信息")
    public ResponseEntity<ApiResponse<JobDTO>> updateJob(
            @PathVariable Long jobId,
            @RequestBody Job job) {
        JobDTO updated = jobService.updateJob(jobId, job);
        return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
    }

    @DeleteMapping("/{jobId}")
    @Operation(summary = "删除职位", description = "删除职位")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}