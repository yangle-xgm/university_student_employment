package com.example.employment.learning.entity;

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
@TableName("learning_path_resources")
public class LearningPathResource {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("path_id")
    private Long pathId;

    @TableField("resource_id")
    private Long resourceId;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}