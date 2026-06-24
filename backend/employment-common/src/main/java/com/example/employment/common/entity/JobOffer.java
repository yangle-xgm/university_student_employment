package com.example.employment.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("job_offers")
public class JobOffer {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("application_id")
    private Long applicationId;

    @TableField("hr_id")
    private Long hrId;

    @TableField("salary")
    private BigDecimal salary;

    @TableField("title")
    private String title;

    @TableField("status")
    private String status;

    @TableField("sent_at")
    private LocalDateTime sentAt;

    @TableField("responded_at")
    private LocalDateTime respondedAt;

    @TableField("notes")
    private String notes;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}