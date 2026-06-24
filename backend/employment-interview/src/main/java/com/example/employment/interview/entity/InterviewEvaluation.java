package com.example.employment.interview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("interview_evaluations")
public class InterviewEvaluation {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("interview_id")
    private Long interviewId;

    @TableField("evaluator_id")
    private Long evaluatorId;

    @TableField("score")
    private BigDecimal score;

    @TableField("communication_score")
    private BigDecimal communicationScore;

    @TableField("technical_score")
    private BigDecimal technicalScore;

    @TableField("attitude_score")
    private BigDecimal attitudeScore;

    @TableField("comments")
    private String comments;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}