package com.example.employment.learning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceDTO {
    private Long id;
    private String title;
    private String type;
    private String category;
    private String difficulty;
    private Integer duration;
    private String url;
    private String thumbnailUrl;
    private String description;
    private Integer viewCount;
    private String status;
    private LocalDateTime createdAt;
}