package com.example.employment.interview.entity;

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
@TableName("interviews")
public class Interview {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("job_id")
    private Long jobId;

    @TableField("interview_time")
    private LocalDateTime interviewTime;

    @TableField("location")
    private String location;

    @TableField("type")
    private String type;

    @TableField("status")
    private String status;

    @TableField("notes")
    private String notes;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
