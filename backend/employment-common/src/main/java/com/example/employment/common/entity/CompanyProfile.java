package com.example.employment.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("company_profiles")
public class CompanyProfile {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("company_name")
    private String companyName;

    @TableField("industry")
    private String industry;

    @TableField("scale")
    private String scale;

    @TableField("location")
    private String location;

    @TableField("description")
    private String description;

    @TableField("verified")
    private Boolean verified;

    @TableField("verified_at")
    private LocalDateTime verifiedAt;

    @TableField("verified_by")
    private Long verifiedBy;

    @TableField("logo_url")
    private String logoUrl;

    @TableField("contact_name")
    private String contactName;

    @TableField("contact_phone")
    private String contactPhone;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}