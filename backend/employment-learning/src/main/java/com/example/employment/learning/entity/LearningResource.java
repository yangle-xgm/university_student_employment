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
@TableName("learning_resources")
public class LearningResource {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("type")
    private String type;

    @TableField("category")
    private String category;

    @TableField("difficulty")
    private String difficulty;

    @TableField("duration")
    private Integer duration;

    @TableField("url")
    private String url;

    @TableField("thumbnail_url")
    private String thumbnailUrl;

    @TableField("description")
    private String description;

    @TableField("view_count")
    private Integer viewCount;

    @TableField("status")
    private String status;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}