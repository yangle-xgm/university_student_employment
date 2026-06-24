package com.example.employment.hr.controller;

import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.common.entity.Application;
import com.example.employment.common.entity.CompanyHrMember;
import com.example.employment.common.entity.CompanyProfile;
import com.example.employment.common.entity.JobOffer;
import com.example.employment.common.util.SecurityUtils;
import com.example.employment.hr.service.HrApplicationService;
import com.example.employment.hr.service.HrCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('HR', 'ADMIN')")
@Tag(name = "HR企业端", description = "企业HR管理接口")
public class HrController {

    private final HrCompanyService hrCompanyService;
    private final HrApplicationService hrApplicationService;

    @GetMapping("/company")
    @Operation(summary = "获取当前HR所属公司信息")
    public ResponseEntity<ApiResponse<CompanyProfile>> getCompany() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(hrCompanyService.getCompanyByUserId(userId)));
    }

    @GetMapping("/company/members")
    @Operation(summary = "获取公司团队成员列表")
    public ResponseEntity<ApiResponse<List<CompanyHrMember>>> getTeamMembers() {
        Long userId = SecurityUtils.getCurrentUserId();
        CompanyProfile company = hrCompanyService.getCompanyByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(hrCompanyService.getTeamMembers(company.getId())));
    }

    @PostMapping("/company/members")
    @Operation(summary = "邀请HR加入公司")
    public ResponseEntity<ApiResponse<CompanyHrMember>> inviteMember(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "MEMBER") String role) {
        Long operatorUserId = SecurityUtils.getCurrentUserId();
        CompanyProfile company = hrCompanyService.getCompanyByUserId(operatorUserId);
        CompanyHrMember member = hrCompanyService.inviteMember(company.getId(), userId, role);
        return ResponseEntity.ok(ApiResponse.success("邀请成功", member));
    }

    @PutMapping("/company/members/{memberId}/role")
    @Operation(summary = "修改成员角色")
    public ResponseEntity<ApiResponse<String>> updateMemberRole(
            @PathVariable Long memberId,
            @RequestParam String role) {
        hrCompanyService.updateMemberRole(memberId, role);
        return ResponseEntity.ok(ApiResponse.success("角色修改成功"));
    }

    @DeleteMapping("/company/members/{memberId}")
    @Operation(summary = "移除公司成员")
    public ResponseEntity<ApiResponse<String>> removeMember(@PathVariable Long memberId) {
        Long operatorUserId = SecurityUtils.getCurrentUserId();
        hrCompanyService.removeMember(memberId, operatorUserId);
        return ResponseEntity.ok(ApiResponse.success("成员已移除"));
    }

    @GetMapping("/applications")
    @Operation(summary = "获取收到的申请列表")
    public ResponseEntity<ApiResponse<List<Application>>> getApplications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        CompanyProfile company = hrCompanyService.getCompanyByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(
                hrApplicationService.getApplicationsByCompany(company.getId(), status, keyword, page, size)));
    }

    @PutMapping("/applications/{applicationId}/status")
    @Operation(summary = "更新申请状态（APPROVED/REJECTED/OFFER）")
    public ResponseEntity<ApiResponse<Application>> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status) {
        Long hrId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(
                hrApplicationService.updateApplicationStatus(applicationId, status, hrId)));
    }

    @PostMapping("/applications/{applicationId}/notes")
    @Operation(summary = "添加HR内部备注")
    public ResponseEntity<ApiResponse<Application>> addApplicationNote(
            @PathVariable Long applicationId,
            @RequestParam String note) {
        return ResponseEntity.ok(ApiResponse.success(
                hrApplicationService.addApplicationNote(applicationId, note)));
    }

    @PostMapping("/offers")
    @Operation(summary = "发放Offer")
    public ResponseEntity<ApiResponse<JobOffer>> createOffer(
            @RequestParam Long applicationId,
            @RequestParam(required = false) BigDecimal salary,
            @RequestParam(required = false) String title) {
        Long hrId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(
                hrApplicationService.createOffer(applicationId, hrId, salary, title)));
    }

    @GetMapping("/offers")
    @Operation(summary = "获取Offer列表")
    public ResponseEntity<ApiResponse<List<JobOffer>>> getOffers() {
        Long userId = SecurityUtils.getCurrentUserId();
        CompanyProfile company = hrCompanyService.getCompanyByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(hrApplicationService.getOffers(company.getId())));
    }
}